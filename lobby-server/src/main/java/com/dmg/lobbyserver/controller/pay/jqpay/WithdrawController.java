package com.dmg.lobbyserver.controller.pay.jqpay;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.dmg.common.core.util.OrderIdUtil;
import com.dmg.data.client.NettySend;
import com.dmg.data.common.dto.GoldPaySendDto;
import com.dmg.lobbyserver.common.constants.CommonConstants;
import com.dmg.lobbyserver.common.util.HttpServletUtil;
import com.dmg.lobbyserver.dao.AccountChangeLogDao;
import com.dmg.lobbyserver.dao.bean.PlatformRechargeLogBean;
import com.dmg.lobbyserver.dao.bean.SysMallConfigBean;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.dao.bean.WithdrawOrderBean;
import com.dmg.lobbyserver.exception.BusinessException;
import com.dmg.lobbyserver.model.dto.WithdrawOrderDTO;
import com.dmg.lobbyserver.result.Result;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.PlatformRechargeLogService;
import com.dmg.lobbyserver.service.SysMallConfigService;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.lobbyserver.service.WithdrawOrderService;
import com.dmg.lobbyserver.service.feign.FinancePropertiesService;
import com.dmg.server.common.enums.AccountChangeTypeEnum;
import com.dmg.server.common.enums.PayPlatformEnum;
import com.dmg.server.common.enums.PlatformRechargeStatusEnum;
import com.dmg.server.common.enums.WithdrawOrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.dmg.lobbyserver.common.constants.CommonConstants.SECRETKEY;
import static com.dmg.lobbyserver.config.RedisKey.*;


/**
 * @Description
 * @Author mice
 * @Date 2019/12/23 10:03
 * @Version V1.0
 **/
@Slf4j
@RestController
@RequestMapping("jqpay")
public class WithdrawController {

    @Value("${md5.salt}")
    private String salt;

    private static final String SUCCESS = "success";

    private static final String NOTIFY_JQ_URL = "http://gmapi.bigfun.io/api/auth/snotify";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private WithdrawOrderService withdrawOrderService;
    @Autowired
    private NettySend nettySend;
    @Autowired
    private FinancePropertiesService financePropertiesService;
    @Autowired
    private AccountChangeLogDao accountChangeLogDao;

    // 参与签名的key
    private final Set<String> signKey = new HashSet<>();
    {
        signKey.add("code");
        signKey.add("amount");
        signKey.add("realAmount");
        signKey.add("currency");
        signKey.add("outUserId");
        signKey.add("payAccount");
        signKey.add("outOrderNo");
        signKey.add("orderId");
        signKey.add("merchantId");
        signKey.add("appId");
        signKey.add("successTime");
    }

    @PostMapping("/withdraw/createOrder")
    public Result jqWithdrawCreateOrder(@RequestBody WithdrawCreateOrderVO vo){
        UserBean userBean = userService.getUserById(vo.getUserId());
        if (userBean == null){
            throw new BusinessException(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
        }
        /*if (StringUtils.isEmpty(userBean.getPhone())){
            throw new BusinessException(ResultEnum.NO_BIND_PHONE.getCode(),ResultEnum.NO_BIND_PHONE.getMsg());
        }*/
        String password = DigestUtil.md5Hex(vo.getPassword() + salt);
        if (StringUtils.isNotEmpty(userBean.getStrongboxPassword()) && !userBean.getStrongboxPassword().equals(password)) {
            throw new BusinessException(ResultEnum.STRONG_BOX_PASSWORD_ERROR.getCode(),"密码错误");
        }
        if (userBean.getAccountBalance().compareTo(vo.getPayAmount())<=0){
            throw new BusinessException(ResultEnum.ACCOUNT_INSUFFICIENT.getCode(),ResultEnum.ACCOUNT_INSUFFICIENT.getMsg());
        }
        String exit = stringRedisTemplate.opsForValue().get(WITHDEAW_ORDER_REQUEST+":"+vo.getUserId());
        if (StringUtils.isNotEmpty(exit)){
            throw new BusinessException(ResultEnum.WITHDRAW_ORDER_REQUEST_ERROR.getCode(),ResultEnum.WITHDRAW_ORDER_REQUEST_ERROR.getMsg());
        }
        FinancePropertiesVo financePropertiesVo = financePropertiesService.getFinanceProperties();
        if (financePropertiesVo==null){
            throw new BusinessException(ResultEnum.SYSTEM_EXCEPTION.getCode(),ResultEnum.SYSTEM_EXCEPTION.getMsg());
        }
        // 提现暂未开启
        if (financePropertiesVo.isWithdrawalStatus() == false){
            throw new BusinessException(ResultEnum.WITHDRAWAL_NO_OPEN.getCode(),ResultEnum.WITHDRAWAL_NO_OPEN.getMsg());
        }
        // 提现时间
        if (!checkWithdrawDate(financePropertiesVo.getBeginWithdrawal(),financePropertiesVo.getEndWithdrawal())){
            throw new BusinessException(ResultEnum.WITHDRAW_DATE_LIMIT.getCode(),ResultEnum.WITHDRAW_DATE_LIMIT.getMsg());
        }
        // 提现上限
        if (financePropertiesVo.getMaxSysWithdrawal()!=null && financePropertiesVo.getMaxSysWithdrawal().compareTo(BigDecimal.ZERO)>0){
            BigDecimal withdrawTotalToday = withdrawOrderService.getWithdrawTotalToday(vo.getUserId());
            if (financePropertiesVo.getMaxSysWithdrawal().compareTo(vo.getPayAmount().add(withdrawTotalToday))<=0){
                throw new BusinessException(ResultEnum.WITHDRAW_UP_TODAY_LIMIT.getCode(),ResultEnum.WITHDRAW_UP_TODAY_LIMIT.getMsg());
            }
        }
        // 单笔下限
        if (financePropertiesVo.getMinWithdrawal()!=null&& financePropertiesVo.getMinWithdrawal().compareTo(BigDecimal.ZERO)>0){
            if (financePropertiesVo.getMinWithdrawal().compareTo(vo.getPayAmount())>0){
                throw new BusinessException(ResultEnum.WITHDRAW_DOWN_LIMIT.getCode(),ResultEnum.WITHDRAW_DOWN_LIMIT.getMsg());
            }
        }
        // 单笔上限
        if (financePropertiesVo.getMaxWithdrawal()!=null && financePropertiesVo.getMinWithdrawal().compareTo(BigDecimal.ZERO)>0){
            if (financePropertiesVo.getMaxWithdrawal().compareTo(vo.getPayAmount())<0){
                throw new BusinessException(ResultEnum.WITHDRAW_UP_LIMIT.getCode(),ResultEnum.WITHDRAW_UP_LIMIT.getMsg());
            }
        }
        // 每日提现次数上限
        if (financePropertiesVo.getMaxWithdrawalTime()!=null && financePropertiesVo.getMaxWithdrawalTime()>0){
            Integer withdrawalTime = withdrawOrderService.getWithdrawalTimeToday(vo.getUserId());
            if (financePropertiesVo.getMaxWithdrawalTime()<=withdrawalTime){
                throw new BusinessException(ResultEnum.MAX_WITHDRAWAL_TIME.getCode(),ResultEnum.MAX_WITHDRAWAL_TIME.getMsg());
            }
        }
        // 流水限制
        if (financePropertiesVo.getWithdrawalTurnover()!=null && financePropertiesVo.getWithdrawalTurnover().compareTo(BigDecimal.ZERO)>0){
            BigDecimal turnover = accountChangeLogDao.sumTurnover(vo.getUserId());
            if (turnover==null){
                turnover = BigDecimal.ZERO;
            }else {
                turnover = turnover.abs();
            }
            if (financePropertiesVo.getWithdrawalTurnover().compareTo(turnover)<0){
                throw new BusinessException(ResultEnum.WITHDRAW_TURNOVER_LIMIT.getCode(),ResultEnum.WITHDRAW_TURNOVER_LIMIT.getMsg());
            }
        }

        WithdrawOrderBean withdrawOrderBean = WithdrawOrderBean.builder()
                .orderId(OrderIdUtil.withOrderId(vo.getPayType()))
                .userId(userBean.getId())
                .nickName(userBean.getUserName())
                .withdrawAmount(vo.getPayAmount())
                //.serviceCharges(vo.getPayAmount().multiply(COMMISSION_RATE).setScale(2,BigDecimal.ROUND_HALF_UP))
                .replenishAccount(BigDecimal.ZERO)
                .orderStatus(WithdrawOrderStatusEnum.WAITE_REVIEW.getCode())
                .applyDate(new Date())
                .bankType(vo.getPayType())
                .bankCardNum(vo.getPayAccount())
                .build();
        withdrawOrderService.save(withdrawOrderBean);
        // 扣款
        nettySend.goldPayAsync(new GoldPaySendDto(vo.getUserId(),vo.getPayAmount().negate(), AccountChangeTypeEnum.CODE_DRAWING.getCode()));
        stringRedisTemplate.opsForValue().set(WITHDEAW_ORDER_REQUEST+":"+vo.getUserId(),vo.getPayAccount());
        stringRedisTemplate.expire(WITHDEAW_ORDER_REQUEST+":"+vo.getUserId(),10,TimeUnit.SECONDS);
        try{
            this.notifyJQ(userBean.getChannelCode());
        }catch (Exception e){
            log.error("==> 提现通知JQ失败{}",e);
        }
        return Result.success();
    }

    private void notifyJQ(String channelCode){
        StringBuilder sb = new StringBuilder();
        sb.append("channelCode=");
        sb.append(channelCode);
        sb.append("&notifyType=1&key=");
        sb.append(SECRETKEY);
        log.info("签名字符串==>{}",sb.toString());
        String oursign = stringToMD5(sb.toString()).toLowerCase();
        log.info("签名==>{}",oursign);
        TreeMap<String,Object> paramMap = new TreeMap<>();
        paramMap.put("channelCode",channelCode);
        paramMap.put("notifyType",1+"");
        paramMap.put("sign",oursign);
        String result = HttpUtil.post(NOTIFY_JQ_URL,paramMap);
        log.info("通知JQ提现审核结果:"+result);
    }

    private static boolean checkWithdrawDate(String startDate,String endDate){
        Map<String,Integer> map = new HashMap<>();
        String[] startDateStr = startDate.split(":");
        Calendar calendar = Calendar.getInstance();
        // 时
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startDateStr[0]));
        // 分
        calendar.set(Calendar.MINUTE, Integer.parseInt(startDateStr[1]));
        // 秒
        calendar.set(Calendar.SECOND, Integer.parseInt(startDateStr[2]));
        // 毫秒
        calendar.set(Calendar.MILLISECOND, 0);
        Long startTimeMillis = calendar.getTimeInMillis();

        String[] endDateStr = endDate.split(":");
        // 时
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endDateStr[0]));
        // 分
        calendar.set(Calendar.MINUTE, Integer.parseInt(endDateStr[1]));
        // 秒
        calendar.set(Calendar.SECOND, Integer.parseInt(endDateStr[2]));
        // 毫秒
        calendar.set(Calendar.MILLISECOND, 0);
        Long endTimeMillis = calendar.getTimeInMillis();

        Long now = System.currentTimeMillis();
        if (startTimeMillis<now && endTimeMillis>now){
            return true;
        }else {
            return false;
        }
    }

    @RequestMapping("/withdraw/getList")
    public Result getWithdrawOrder(@RequestParam("userId")Long userId){
        UserBean userBean = userService.getUserById(userId);
        if (userBean == null){
            throw new BusinessException(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
        }
        List<WithdrawOrderDTO> withdrawOrderDTOS = withdrawOrderService.getOrderLimit(userId,50);
        return Result.success(withdrawOrderDTOS);
    }

    @PostMapping("/withdraw/notify")
    public String jqWithdrawCallback(HttpServletRequest servletRequest, @RequestParam Map<String, String> params){
        log.info("==> 代付回调参数{} , 请求ip:{}",params, HttpServletUtil.getIpAddr(servletRequest));
        TreeMap paramTreeMap = new TreeMap();
        paramTreeMap.putAll(params);
        String thirdOrderId = params.get("orderId");
        boolean validate = validateSign(params.get("sign"),paramTreeMap);
        if (!validate){
            log.error("订单号:{},签名验证失败",thirdOrderId);
            return String.valueOf(JQPayErrorEnum.SIGN_ERROR.getCode());
        }
        WithdrawOrderBean withdrawOrderBean = withdrawOrderService.getByOrderId(params.get("outOrderNo"));
        if (withdrawOrderBean == null){
            // 我方订单未创建
            log.error("我方订单未创建,订单id:{}", thirdOrderId);
            return String.valueOf(JQPayErrorEnum.OUR_ORDER_CREATE_ERROR.getCode());
        }
        // 验证通过 处理重复请求
        String reqId = thirdOrderId;
        String checkResult = this.idempotentValidate(reqId);
        if (StringUtils.isNotEmpty(checkResult)) {
            log.warn("重复的订单请求,请求流水id:{},返回体:{}", reqId, checkResult);
            return checkResult;
        }

        // 用户验证
        UserBean userBean = userService.getUserById(Long.parseLong(params.get("outUserId")));
        if (userBean == null) {
            // 用户不存在
            log.error("购买用户不存在,用户id:{}", params.get("outUserId"));
            return String.valueOf(JQPayErrorEnum.USER_NO_EXIT.getCode());
        }
        if ("0".equals(params.get("code"))){
            withdrawOrderBean.setAccount(new BigDecimal(params.get("realAmount")));
            withdrawOrderBean.setServiceCharges(new BigDecimal(params.get("amount")).subtract(new BigDecimal(params.get("realAmount"))));
            withdrawOrderBean.setThirdOrderId(params.get("orderId"));
            withdrawOrderBean.setAccountDate(DateUtil.parse(params.get("successTime")));
            withdrawOrderBean.setReplenishAccount(BigDecimal.ZERO);
            withdrawOrderBean.setOrderStatus(WithdrawOrderStatusEnum.FINISH.getCode());
            withdrawOrderService.update(withdrawOrderBean);
        }else {
            withdrawOrderBean.setOrderStatus(WithdrawOrderStatusEnum.PAYING.getCode());
            withdrawOrderBean.setReplenishAccount(withdrawOrderBean.getWithdrawAmount()
                    .subtract(withdrawOrderBean.getWithdrawAmount())
                    .setScale(2,BigDecimal.ROUND_HALF_UP));
            withdrawOrderService.update(withdrawOrderBean);
        }

        // 存返回体
        stringRedisTemplate.opsForValue().set(JQ_DAIFU_REQUEST_ID + ":" + params.get("orderId"), SUCCESS, TimeUnit.HOURS.toMinutes(24));

        return SUCCESS;
    }

    private boolean validateSign(String sign, TreeMap<String, String> paramMap){
        StringBuilder sb = new StringBuilder();
        for (String key : paramMap.keySet()){
            if (StringUtils.isEmpty(paramMap.get(key)) || !signKey.contains(key)){
                continue;
            }
            String value = paramMap.get(key);
            sb.append(key + "=" + value + "&");
        }
        sb.append("key=");
        sb.append(SECRETKEY);
        log.info("签名字符串==>{}",sb.toString());
        String oursign = stringToMD5(sb.toString()).toLowerCase();
        log.info("签名==>{}",oursign);
        if (oursign.equals(sign)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * @param reqId
     * @return void
     * @description: 幂等验证
     * @author mice
     * @date 2019/12/23
     */
    private String idempotentValidate(String reqId) {
        String respStr = stringRedisTemplate.opsForValue().get(JQ_DAIFU_REQUEST_ID + ":" + reqId);
        if (StringUtils.isNotEmpty(respStr)) {
            // 重复请求
            return respStr;
        }else {
            return null;
        }
    }

    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

}
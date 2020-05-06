package com.dmg.lobbyserver.controller.pay.jqpay;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.common.util.HttpServletUtil;
import com.dmg.lobbyserver.dao.bean.PlatformRechargeLogBean;
import com.dmg.lobbyserver.dao.bean.SysMallConfigBean;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.service.PlatformRechargeLogService;
import com.dmg.lobbyserver.service.SysMallConfigService;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.server.common.enums.PlatformRechargeStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_FORMAT;
import static com.dmg.lobbyserver.common.constants.CommonConstants.SECRETKEY;
import static com.dmg.lobbyserver.config.RedisKey.JQ_PAY_REQUEST_ID;


/**
 * @Description
 * @Author mice
 * @Date 2019/12/23 10:03
 * @Version V1.0
 **/
@Slf4j
@RestController
@RequestMapping("jqpay")
public class JQPayController {

    private static final String APPID = "jq100000036";


    private static final String SUCCESS = "success";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private PlatformRechargeLogService platformRechargeLogService;
    @Autowired
    private SysMallConfigService sysMallConfigService;
    @Autowired
    private JQPayService jqPayService;
    @Autowired
    private ThirdPayService thirdPayService;
    // 参与签名的key
    private final Set<String> signKey = new HashSet<>();

    {
        signKey.add("amount");
        signKey.add("currency");
        signKey.add("code");
        signKey.add("orderId");
        signKey.add("merchantId");
        signKey.add("appId");
        signKey.add("outUserId");
        signKey.add("passageTradeNo");
        signKey.add("successTime");
    }

    @PostMapping("/third/notify")
    public String jqPayCallback(HttpServletRequest servletRequest, @RequestParam Map<String, String> params){
        log.info("==> JQPAY 支付回调参数{} , 请求ip:{}",params, HttpServletUtil.getIpAddr(servletRequest));
        if (params.get("commodityId").equals("qpay")){
         return thirdPay(servletRequest,params);
        }
        TreeMap paramTreeMap = new TreeMap();
        paramTreeMap.putAll(params);
        String thirdOrderId = params.get("orderId");
        boolean validate = validateSign(params.get("sign"),paramTreeMap);
        if (!validate){
            log.error("订单号:{},签名验证失败",thirdOrderId);
            return String.valueOf(JQPayErrorEnum.SIGN_ERROR.getCode());
        }

        // 验证通过 处理重复请求
        String reqId = thirdOrderId;
        String checkResult = this.idempotentValidate(reqId);
        if (StringUtils.isNotEmpty(checkResult)) {
            log.warn("重复的订单请求,请求流水id:{},返回体:{}", reqId, checkResult);
            return checkResult;
        }
        PlatformRechargeLogBean platformRechargeLogBean = platformRechargeLogService.getByOrderId(params.get("outOrderNo"));
        if (platformRechargeLogBean == null){
            // 我方订单未创建
            log.error("我方订单未创建,订单id:{}", thirdOrderId);
            return String.valueOf(JQPayErrorEnum.OUR_ORDER_CREATE_ERROR.getCode());
        }
        // 商品验证
        String commodityId = params.get("commodityId");
        SysMallConfigBean sysMallConfigBean = sysMallConfigService.getSysMallConfigByThirdId(commodityId);
        if (sysMallConfigBean == null) {
            // 商品不存在
            log.error("商品不存在,商品id:{}", commodityId);
            this.platformRechargeLogBeanWarpper(params, JQPayErrorEnum.ITEM_NO_EXIT.getCode(), platformRechargeLogBean);
            platformRechargeLogService.updateById(platformRechargeLogBean);
            return String.valueOf(JQPayErrorEnum.ITEM_NO_EXIT.getCode());
        }

        BigDecimal amount = new BigDecimal(params.get("amount"));
        if (amount.compareTo(sysMallConfigBean.getOriginalPrice())!=0) {
            // 价格不匹配
            log.error("商品价格不匹配,商品id:{},请求价格为:{},实际价格为:{}", commodityId,
                    amount, sysMallConfigBean.getOriginalPrice().doubleValue());
            this.platformRechargeLogBeanWarpper(params, JQPayErrorEnum.PRICE_NO_MATCH.getCode(),platformRechargeLogBean);
            platformRechargeLogService.updateById(platformRechargeLogBean);
            return String.valueOf(JQPayErrorEnum.PRICE_NO_MATCH.getCode());
        }

        // 用户验证
        UserBean userBean = userService.getUserById(Long.parseLong(params.get("outUserId")));
        if (userBean == null) {
            // 用户不存在
            log.error("购买用户不存在,用户id:{}", params.get("outUserId"));
            this.platformRechargeLogBeanWarpper(params, JQPayErrorEnum.USER_NO_EXIT.getCode(), platformRechargeLogBean);
            platformRechargeLogService.updateById(platformRechargeLogBean);
            return String.valueOf(JQPayErrorEnum.USER_NO_EXIT.getCode());
        }

        try {
            jqPayService.sendItem(platformRechargeLogBean,userBean,params);
        }catch (Exception e){
            log.error("==>发放商品失败",e);
            return String.valueOf(JQPayErrorEnum.SYS_SEND_GOLD_ERROR.getCode());
        }
        return SUCCESS;
    }

    private String thirdPay(HttpServletRequest servletRequest,Map<String, String> params){
        log.info("==> THIRD PAY 支付回调参数{} , 请求ip:{}",params, HttpServletUtil.getIpAddr(servletRequest));
        TreeMap paramTreeMap = new TreeMap();
        paramTreeMap.putAll(params);
        String thirdOrderId = params.get("orderId");
        boolean validate = validateSign(params.get("sign"),paramTreeMap);
        if (!validate){
            log.error("订单号:{},签名验证失败",thirdOrderId);
            return String.valueOf(JQPayErrorEnum.SIGN_ERROR.getCode());
        }
        PlatformRechargeLogBean platformRechargeLogBean = platformRechargeLogService.getByOrderId(params.get("outOrderNo"));
        if (platformRechargeLogBean == null){
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
            this.platformRechargeLogBeanWarpper(params, JQPayErrorEnum.USER_NO_EXIT.getCode(),platformRechargeLogBean);
            platformRechargeLogService.updateById(platformRechargeLogBean);
            return String.valueOf(JQPayErrorEnum.USER_NO_EXIT.getCode());
        }

        try {
            thirdPayService.sendItem(platformRechargeLogBean,userBean,params);
        }catch (Exception e){
            log.error("==>发放商品失败",e);
            return String.valueOf(JQPayErrorEnum.SYS_SEND_GOLD_ERROR.getCode());
        }
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
        String respStr = stringRedisTemplate.opsForValue().get(JQ_PAY_REQUEST_ID + ":" + reqId);
        if (StringUtils.isNotEmpty(respStr)) {
            // 重复请求
            return respStr;
        }
        respStr = platformRechargeLogService.idempotentValidate(reqId);
        if (StringUtils.isNotEmpty(respStr)) {
            return respStr;
        } else {
            return null;
        }
    }

    private void platformRechargeLogBeanWarpper(Map<String, String> params, int errorCode, PlatformRechargeLogBean platformRechargeLogBean){
        platformRechargeLogBean.setThirdOrderId(params.get("orderId"));
        platformRechargeLogBean.setPayDate(DateUtil.parse(params.get("successTime"),NORM_DATETIME_FORMAT));
        platformRechargeLogBean.setRequestBody(JSONObject.toJSONString(params));
        platformRechargeLogBean.setResponseBody(String.valueOf(errorCode));
        platformRechargeLogBean.setOrderStatus(PlatformRechargeStatusEnum.NO_ARRIVE.getCode());
        platformRechargeLogBean.setErrorCode(errorCode);
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
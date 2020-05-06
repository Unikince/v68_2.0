package com.dmg.lobbyserver.controller.pay.vnnnpay;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.dmg.data.client.NettySend;
import com.dmg.data.common.dto.GoldPaySendDto;
import com.dmg.lobbyserver.common.constants.RechargeType;
import com.dmg.lobbyserver.common.enums.VnnnPayErrorEnum;
import com.dmg.lobbyserver.dao.bean.PlatformRechargeLogBean;
import com.dmg.lobbyserver.dao.bean.SysMallConfigBean;
import com.dmg.lobbyserver.dao.bean.SysRechargeLogBean;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.dto.pay.VnnPayReqDTO;
import com.dmg.lobbyserver.model.dto.pay.VnnPayRespContentDTO;
import com.dmg.lobbyserver.service.*;
import com.dmg.server.common.enums.AccountChangeTypeEnum;
import com.dmg.server.common.enums.PlatformRechargeStatusEnum;
import com.dmg.server.common.enums.PayPlatformEnum;
import com.dmg.server.common.enums.TaskTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.dmg.lobbyserver.config.RedisKey.VNNN_PAY_REQUEST_ID;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/19 10:43
 * @Version V1.0
 **/
@RestController
@RequestMapping("vn")
@Slf4j
@ComponentScan(basePackages = {"com.dmg.data.client"})
public class VnnnPayController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private SysMallConfigService sysMallConfigService;

    @Autowired
    private PlatformRechargeLogService platformRechargeLogService;

    @Autowired
    private NettySend nettySend;

    @Autowired
    private UserTaskProgressService userTaskProgressService;

    @Autowired
    private SysRechargeLogService sysRechargeLogService;

    @Autowired
    private VnnPayService vnnPayService;

    private static final String APP_ID = "2500";

    @PostMapping("coin")
    public synchronized String pay(@RequestParam("signature") String signature
            , @RequestParam("content") String content
            , @RequestParam("timestamp") String timestamp
            , @RequestParam("rand") String rand) {

        log.info("vnnnpay请求参数为==>[signature:{},content:{},timestamp:{},rand:{}]", signature, content, timestamp, rand);

        boolean validateSignature = vnnPayService.signCheck(signature, timestamp, rand);
        String resp;
        if (!validateSignature) {
            return vnnPayService.getRespData(VnnPayRespContentDTO.builder()
                    .appid(APP_ID)
                    .err(VnnnPayErrorEnum.SIGN_ERROR.getMsg())
                    .status(VnnnPayErrorEnum.SIGN_ERROR.getCode()).build());
        }

        VnnPayReqDTO vnnPayReqDTO = vnnPayService.decryptData(content, VnnPayReqDTO.class);
        log.info("解析后的请求体==>reqDTO:{}", JSONObject.toJSONString(vnnPayReqDTO));

        if (!APP_ID.equals(vnnPayReqDTO.getAppId())) {
            // APP_ID 验证失败
            return vnnPayService.getRespData(VnnPayRespContentDTO.builder()
                    .appid(APP_ID)
                    .resp_id("0")
                    .req_id(vnnPayReqDTO.getId())
                    .err(VnnnPayErrorEnum.APPID_ERROR.getMsg())
                    .status(VnnnPayErrorEnum.APPID_ERROR.getCode()).build());
        }

        // 验证通过 处理重复请求
        String reqId = vnnPayReqDTO.getId();
        String checkResult = this.idempotentValidate(reqId);
        if (StringUtils.isNotEmpty(checkResult)) {
            log.warn("重复的订单请求,请求流水id:{},返回体:{}", reqId, checkResult);
            return checkResult;
        }

        // 商品验证
        SysMallConfigBean sysMallConfigBean = sysMallConfigService.getSysMallConfigById(Long.parseLong(vnnPayReqDTO.getGiftId()));
        if (sysMallConfigBean == null) {
            // 商品不存在
            log.error("商品不存在,商品id:{}", vnnPayReqDTO.getGiftId());
            resp = vnnPayService.getRespData(VnnPayRespContentDTO.builder()
                    .appid(APP_ID)
                    .resp_id("0")
                    .req_id(vnnPayReqDTO.getId())
                    .err(VnnnPayErrorEnum.ITEM_NO_EXIT.getMsg())
                    .status(VnnnPayErrorEnum.ITEM_NO_EXIT.getCode()).build());
            platformRechargeLogService.save(platformRechargeLogBeanWarpper(vnnPayReqDTO, VnnnPayErrorEnum.ITEM_NO_EXIT.getCode(), resp, 0, null));
            return resp;
        }

        if (vnnPayReqDTO.getNum() != sysMallConfigBean.getOriginalPrice().doubleValue()) {
            // 价格不匹配
            log.error("商品价格不匹配,商品id:{},请求价格为:reqDTO.getNum(),实际价格为:{}", vnnPayReqDTO.getGiftId(),
                    vnnPayReqDTO.getNum(), sysMallConfigBean.getOriginalPrice().doubleValue());
            resp = vnnPayService.getRespData(VnnPayRespContentDTO.builder()
                    .appid(APP_ID)
                    .resp_id("0")
                    .req_id(vnnPayReqDTO.getId())
                    .err(VnnnPayErrorEnum.PRICE_NO_MATCH.getMsg())
                    .status(VnnnPayErrorEnum.PRICE_NO_MATCH.getCode()).build());
            platformRechargeLogService.save(platformRechargeLogBeanWarpper(vnnPayReqDTO, VnnnPayErrorEnum.PRICE_NO_MATCH.getCode(), resp, sysMallConfigBean.getItemNum(), null));
            return resp;
        }
        // 用户验证
        UserBean userBean = userService.getUserById(Long.parseLong(vnnPayReqDTO.getTargetId()));
        if (userBean == null) {
            // 用户不存在
            log.error("购买用户不存在,用户id:{}", vnnPayReqDTO.getTargetId());
            resp = vnnPayService.getRespData(VnnPayRespContentDTO.builder()
                    .appid(APP_ID)
                    .resp_id("0")
                    .req_id(vnnPayReqDTO.getId())
                    .err(VnnnPayErrorEnum.USER_NO_EXIT.getMsg())
                    .status(VnnnPayErrorEnum.USER_NO_EXIT.getCode()).build());
            platformRechargeLogService.save(platformRechargeLogBeanWarpper(vnnPayReqDTO, VnnnPayErrorEnum.USER_NO_EXIT.getCode(), resp, sysMallConfigBean.getItemNum(), null));
            return resp;
        }


        // 发放金币
        GoldPaySendDto goldPaySendDto = new GoldPaySendDto(userBean.getId(), new BigDecimal(sysMallConfigBean.getItemNum()), AccountChangeTypeEnum.CODE_CHANNEL_RECHARGE.getCode());
        nettySend.goldPay(goldPaySendDto);

        String respId = String.valueOf(System.currentTimeMillis() + RandomUtil.randomInt(1, 10000));
        // 存订单
        resp = vnnPayService.getRespData(VnnPayRespContentDTO.builder()
                .appid(APP_ID)
                .err("ok")
                .status(0)
                .req_id(vnnPayReqDTO.getId())
                .resp_id(respId).build());
        platformRechargeLogService.save(platformRechargeLogBeanWarpper(vnnPayReqDTO, 0, resp, sysMallConfigBean.getItemNum(), userBean.getUserName()));


        // 存返回体
        stringRedisTemplate.opsForValue().set(VNNN_PAY_REQUEST_ID + ":" + reqId, resp, TimeUnit.MINUTES.toMinutes(20));

        // 存充值记录
        SysRechargeLogBean sysRechargeLogBean = new SysRechargeLogBean();
        sysRechargeLogBean.setOrderId(reqId);
        sysRechargeLogBean.setPlatformType(PayPlatformEnum.VNNN.getCode());
        sysRechargeLogBean.setRechargeNumber(sysMallConfigBean.getItemNum());
        sysRechargeLogBean.setRechargeType(RechargeType.GOLD);
        sysRechargeLogService.save(sysRechargeLogBean);

        log.info("用户:{},购买商品:{} 成功", userBean.getUserName(), sysMallConfigBean.getItemId());

        try {
            userTaskProgressService.userTaskChange(userBean.getId(), TaskTypeEnum.CODE_PURCHASE_GOODS.getCode(), 1, null);
        } catch (Exception e) {
            log.error("任务变更出现异常：{}", e);
        }
        return resp;
    }


    /**
     * @param reqId
     * @return void
     * @description: 幂等验证
     * @author mice
     * @date 2019/11/27
     */
    private String idempotentValidate(String reqId) {
        String respStr = stringRedisTemplate.opsForValue().get(VNNN_PAY_REQUEST_ID + ":" + reqId);
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

    private PlatformRechargeLogBean platformRechargeLogBeanWarpper(VnnPayReqDTO vo, int errorCode, String resp, long rechargeAmount, String nickname) {
        int orderStatus = PlatformRechargeStatusEnum.NO_ARRIVE.getCode();
        if (errorCode == 0) {
            orderStatus = PlatformRechargeStatusEnum.FINISH.getCode();
        }
        PlatformRechargeLogBean platformRechargeLogBean = PlatformRechargeLogBean.builder()
                .orderId(System.currentTimeMillis() + RandomUtil.randomInt(10000, 99999) + "")
                .thirdOrderId(vo.getId())
                .userId(Long.parseLong(vo.getTargetId()))
                .nickName(nickname)
                .sysMallConfigId(Long.parseLong(vo.getGiftId()))
                .payDate(new Date())
                .arriveDate(new Date())
                .platformId(PayPlatformEnum.VNNN.getCode())
                .payAmount(new BigDecimal(vo.getNum()))
                .rechargeAmount(new BigDecimal(rechargeAmount))
                .requestBody(JSONObject.toJSONString(vo))
                .responseBody(resp)
                .orderStatus(orderStatus)
                .errorCode(errorCode)
                .build();
        return platformRechargeLogBean;
    }

}
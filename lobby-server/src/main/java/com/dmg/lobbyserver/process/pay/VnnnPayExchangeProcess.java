package com.dmg.lobbyserver.process.pay;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.data.client.NettySend;
import com.dmg.data.common.dto.GoldPaySendDto;
import com.dmg.lobbyserver.controller.pay.vnnnpay.VnnPayService;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.dao.bean.WithdrawOrderBean;
import com.dmg.lobbyserver.exception.BusinessException;
import com.dmg.lobbyserver.model.dto.CommonRespDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.lobbyserver.service.WithdrawOrderService;
import com.dmg.server.common.enums.AccountChangeTypeEnum;
import com.dmg.server.common.enums.WithdrawOrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.dmg.lobbyserver.config.MessageConfig.USER_VNNN_PAY_EXCHANGE;
import static com.dmg.lobbyserver.config.RedisKey.WITHDEAW_ORDER_REQUEST;

/**
 * @Author liubo
 * @Description //TODO vnnnPay 回兑
 * @Date 18:06 2020/2/12
 */
@Service
@Slf4j
public class VnnnPayExchangeProcess implements AbstractMessageHandler {

    @Override
    public String getMessageId() {
        return USER_VNNN_PAY_EXCHANGE;
    }

    @Autowired
    private WithdrawOrderService withdrawOrderService;

    @Autowired
    private VnnPayService vnnPayService;

    @Autowired
    private UserService userService;

    @Autowired
    private NettySend nettySend;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${md5.salt}")
    private String salt;

    @Override
    public void messageHandler(String userId, JSONObject params, MessageResult result) {

        UserBean userBean = userService.getUserById(Long.parseLong(userId));
        if (userBean == null) {
            result.setRes(ResultEnum.PARAM_ERROR.getCode());
            return;
        }
        BigDecimal gold = params.getBigDecimal("payAmount");
        String kalUserId = params.getString("payAccount");
        if (userBean.getAccountBalance().compareTo(gold) <= 0) {
            result.setRes(ResultEnum.ACCOUNT_INSUFFICIENT.getCode());
            return;
        }
        String exit = stringRedisTemplate.opsForValue().get(WITHDEAW_ORDER_REQUEST + ":" + userId);
        if (StringUtils.isNotEmpty(exit)) {
            result.setRes(ResultEnum.WITHDRAW_ORDER_REQUEST_ERROR.getCode());
            return;
        }
        String orderCode = String.valueOf(System.currentTimeMillis()).concat(String.valueOf(RandomUtil.randomInt(100000, 999999)));

        CommonRespDTO commonRespDTO = vnnPayService.exchange(kalUserId, gold, true, orderCode);
        if (commonRespDTO.getStatus()) {
            WithdrawOrderBean withdrawOrderBean = WithdrawOrderBean.builder()
                    .orderId(orderCode)
                    .userId(Long.parseLong(userId))
                    .nickName(userBean.getUserName())
                    .withdrawAmount(gold)
                    .replenishAccount(BigDecimal.ZERO)
                    .orderStatus(WithdrawOrderStatusEnum.FINISH.getCode())
                    .applyDate(new Date())
                    .accountDate(new Date())
                    .account(gold)
                    .bankCardNum(kalUserId)
                    .serviceCharges(BigDecimal.ZERO)
                    .build();
            withdrawOrderService.save(withdrawOrderBean);
            // 扣款
            nettySend.goldPayAsync(new GoldPaySendDto(Long.parseLong(userId), gold.negate(), AccountChangeTypeEnum.CODE_DRAWING.getCode()));
            stringRedisTemplate.opsForValue().set(WITHDEAW_ORDER_REQUEST + ":" + userId, kalUserId);
            stringRedisTemplate.expire(WITHDEAW_ORDER_REQUEST + ":" + userId, 10, TimeUnit.SECONDS);
        }
        result.setRes(commonRespDTO.getCode());
        result.setMsg(JSON.toJSON(commonRespDTO));
    }

}

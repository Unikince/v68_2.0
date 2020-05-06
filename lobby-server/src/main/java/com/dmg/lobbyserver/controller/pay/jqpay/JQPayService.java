package com.dmg.lobbyserver.controller.pay.jqpay;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.dmg.data.client.NettySend;
import com.dmg.data.common.dto.GoldPaySendDto;
import com.dmg.lobbyserver.dao.bean.PlatformRechargeLogBean;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.service.PlatformRechargeLogService;
import com.dmg.lobbyserver.service.UserTaskProgressService;
import com.dmg.server.common.enums.AccountChangeTypeEnum;
import com.dmg.server.common.enums.PlatformRechargeStatusEnum;
import com.dmg.server.common.enums.TaskTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.dmg.lobbyserver.config.RedisKey.JQ_PAY_REQUEST_ID;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/23 14:47
 * @Version V1.0
 **/
@Service
@Slf4j
public class JQPayService {
    private static final String SUCCESS = "success";
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private NettySend nettySend;
    @Autowired
    private PlatformRechargeLogService platformRechargeLogService;
    @Autowired
    private UserTaskProgressService userTaskProgressService;

    public void sendItem(PlatformRechargeLogBean platformRechargeLogBean, UserBean userBean, Map<String, String> params) {
        // 存订单
        this.platformRechargeLogBeanWarpper(platformRechargeLogBean, params);
        platformRechargeLogService.updateById(platformRechargeLogBean);

        // 存返回体
        stringRedisTemplate.opsForValue().set(JQ_PAY_REQUEST_ID + ":" + params.get("orderId"), SUCCESS, TimeUnit.MINUTES.toMinutes(20));

        // 发放金币
        GoldPaySendDto goldPaySendDto = new GoldPaySendDto(userBean.getId(), platformRechargeLogBean.getRechargeAmount(), AccountChangeTypeEnum.CODE_CHANNEL_RECHARGE.getCode());
        nettySend.goldPay(goldPaySendDto);
        log.info("用户:{},购买数量:{} 成功", userBean.getUserName(), platformRechargeLogBean.getRechargeAmount());

        try {
            userTaskProgressService.userTaskChange(userBean.getId(), TaskTypeEnum.CODE_PURCHASE_GOODS.getCode(), 1, null);
        } catch (Exception e) {
            log.error("任务变更出现异常：{}", e);
        }
    }

    private void platformRechargeLogBeanWarpper(PlatformRechargeLogBean platformRechargeLogBean, Map<String, String> params) {
        platformRechargeLogBean.setThirdOrderId(params.get("orderId"));
        platformRechargeLogBean.setRequestCount(1);
        platformRechargeLogBean.setPayDate(DateUtil.parseDate(params.get("successTime")));
        platformRechargeLogBean.setArriveDate(new Date());
        platformRechargeLogBean.setRequestBody(JSONObject.toJSONString(params));
        platformRechargeLogBean.setResponseBody(SUCCESS);
        platformRechargeLogBean.setOrderStatus(PlatformRechargeStatusEnum.FINISH.getCode());
    }
}
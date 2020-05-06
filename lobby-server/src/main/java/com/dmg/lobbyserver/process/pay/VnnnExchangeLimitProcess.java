package com.dmg.lobbyserver.process.pay;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.controller.pay.vnnnpay.VnnPayService;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.dto.CommonRespDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.dmg.lobbyserver.config.MessageConfig.USER_VNNN_EXCHANG_LIMIT;

/**
 * @description: 获取兑换最大金额
 * @return
 * @author mice
 * @date 2020/3/26
*/
@Service
@Slf4j
public class VnnnExchangeLimitProcess implements AbstractMessageHandler {

    @Override
    public String getMessageId() {
        return USER_VNNN_EXCHANG_LIMIT;
    }

    @Autowired
    private VnnPayService vnnPayService;

    @Autowired
    private UserService userService;

    @Value("${md5.salt}")
    private String salt;

    @Override
    public void messageHandler(String userId, JSONObject params, MessageResult result) {

        UserBean userBean = userService.getUserById(Long.parseLong(userId));
        if (userBean == null) {
            result.setRes(ResultEnum.PARAM_ERROR.getCode());
            return;
        }
        BigDecimal gold = BigDecimal.TEN;
        String kalUserId = "1003684299";
        if (userBean.getAccountBalance().compareTo(gold) <= 0) {
            result.setRes(ResultEnum.ACCOUNT_INSUFFICIENT.getCode());
            return;
        }
        String orderCode = String.valueOf(System.currentTimeMillis()).concat(String.valueOf(RandomUtil.randomInt(100000, 999999)));

        CommonRespDTO commonRespDTO = vnnPayService.exchange(kalUserId, gold, false, orderCode);
        result.setRes(commonRespDTO.getCode());
        result.setMsg(commonRespDTO.getData());
    }

}

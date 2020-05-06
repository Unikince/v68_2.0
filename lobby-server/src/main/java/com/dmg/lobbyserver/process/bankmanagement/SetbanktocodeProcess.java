package com.dmg.lobbyserver.process.bankmanagement;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.SETBANK_TOCODE;

/**
 * @Description 绑定银行卡验证码验证开户信息
 * @Author jock
 * @Date 2019/6/20 0020
 * @Version V1.0
 **/
@Service
@Slf4j
public class SetbanktocodeProcess  implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return SETBANK_TOCODE;
    }

    @Override


    public void messageHandler(String userid, JSONObject params, MessageResult result) {

    }
}

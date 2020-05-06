package com.dmg.lobbyserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.result.MessageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.CUSTOMER_SERVICE_INFO;

/**
 * @Description 客服信息
 * @Author mice
 * @Date 2019/6/21 10:01
 * @Version V1.0
 **/
@Service
public class CustomerServiceInfoProcess implements AbstractMessageHandler {
    @Value("${customerServiceInfo.phone}")
    private String phone;

    @Override
    public String getMessageId() {
        return CUSTOMER_SERVICE_INFO;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        JSONObject customerServiceInfo = new JSONObject();
        customerServiceInfo.put("phone",phone);
        result.setMsg(customerServiceInfo);
    }
}
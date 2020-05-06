package com.dmg.lobbyserver.process.bankmanagement;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.BankCardDao;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.DELETE_BANK;

/**
 * @Description 删除银行
 * @Author jock
 * @Date 2019/6/26 0026
 * @Version V1.0
 **/
@Slf4j
@Service
public class DeleteBankProcess  implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return DELETE_BANK;
    }
@Autowired
    BankCardDao bankCardDao;
    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        String bankId = params.getString("bankId");
        if(StringUtils.isEmpty(bankId)){
            result.setRes(ResultEnum.PARAM_ERROR.getCode());
        }
        bankCardDao.deleteById(bankId);
    }
}

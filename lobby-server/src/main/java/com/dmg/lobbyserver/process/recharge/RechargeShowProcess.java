package com.dmg.lobbyserver.process.recharge;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.SysRechargeLogDao;
import com.dmg.lobbyserver.dao.bean.SysRechargeLogBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dmg.lobbyserver.config.MessageConfig.RECHARGE_MONNEY_SHOW;

/**
 * @Description 充值显示(金额)
 * @Author jock
 * @Date 2019/7/3 0003
 * @Version V1.0
 **/
@Service
@Slf4j
public class RechargeShowProcess  implements AbstractMessageHandler {
    @Autowired
    SysRechargeLogDao sysRechargeLogDao;
    @Override
    public String getMessageId() {
        return RECHARGE_MONNEY_SHOW;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        List<SysRechargeLogBean> moneyList = sysRechargeLogDao.getMoneyList();
        result.setMsg(JSON.toJSON(moneyList));
    }
}

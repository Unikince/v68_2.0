package com.dmg.lobbyserver.process.task;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.IntegralExchangeLogDao;
import com.dmg.lobbyserver.dao.bean.IntegralExchangeLogBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.dmg.lobbyserver.config.MessageConfig.CONVERSION_RECORD;
/**
 * @Description 兑换记录
 * @Author jock
 * @Date 2019/6/25 0025
 * @Version V1.0
 **/
@Slf4j
@Service
public class ConversionRecordProcess  implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return CONVERSION_RECORD;
    }
@Autowired
    IntegralExchangeLogDao integralExchangeLogDao;
    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        List<IntegralExchangeLogBean> integrals = integralExchangeLogDao.getIntegrals(Long.parseLong(userid));
        result.setMsg(integrals);
    }
}

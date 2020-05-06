package com.dmg.lobbyserver.process.bankmanagement;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.BankCardDao;
import com.dmg.lobbyserver.dao.bean.BankCardBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.dmg.lobbyserver.config.MessageConfig.GET_BANKDETAILS;
/**
 * @Description 银行卡信息
 * @Author jock
 * @Date 2019/6/20 0020
 * @Version V1.0
 **/
@Slf4j
@Service
public class GetbankProcess  implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return GET_BANKDETAILS;
    }
    @Autowired
    BankCardDao bankCardDao;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        List<BankCardBean> bankCardBeans = bankCardDao.selectList(new LambdaQueryWrapper<BankCardBean>().eq(BankCardBean::getUserId, userid));
            result.setMsg(JSON.toJSON(bankCardBeans));

    }
}

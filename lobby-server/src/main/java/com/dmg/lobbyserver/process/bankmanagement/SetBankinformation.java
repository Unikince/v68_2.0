package com.dmg.lobbyserver.process.bankmanagement;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.BankCardDao;
import com.dmg.lobbyserver.dao.bean.BankCardBean;
import com.dmg.lobbyserver.model.vo.BankMessageVo;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static com.dmg.lobbyserver.config.MessageConfig.SETBANK_MESSAGE;
/**
 * @Description 绑定银行信息
 * @Author jock
 * @Date 2019/6/20 0020
 * @Version V1.0
 **/
@Service
@Slf4j
public class SetBankinformation  implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return SETBANK_MESSAGE;
    }
    @Autowired
    BankCardDao bankCardDao;
    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result)  {
        BankMessageVo bankMessageVo = params.toJavaObject(BankMessageVo.class);
        BankCardBean  CardBean=new BankCardBean();
        CardBean.setUserId(Long.parseLong(userid));
        CardBean.setAccountName(bankMessageVo.getName());
        CardBean.setBankType(bankMessageVo.getType());
        CardBean.setBankCardNum(bankMessageVo.getCardNo());
        CardBean.setOpenAccountProvince(bankMessageVo.getProvince());
        CardBean.setOpenAccountCity(bankMessageVo.getCity());
        CardBean.setCreateDate(new Date());
        CardBean.setPhone(bankMessageVo.getPhone());
        bankCardDao.insert(CardBean);
    }
}

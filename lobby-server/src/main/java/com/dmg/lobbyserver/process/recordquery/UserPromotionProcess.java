package com.dmg.lobbyserver.process.recordquery;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.UserPromotionLogDao;
import com.dmg.lobbyserver.dao.bean.UserPromotionLogBean;
import com.dmg.lobbyserver.model.dto.GamerecordDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.dmg.lobbyserver.config.MessageConfig.DISCOUNTS_RECORD;
/**
 * @Description  优惠记录
 * @Author jock
 * @Date 2019/6/19 0019
 * @Version V1.0
 **/
@Slf4j
@Service
public class UserPromotionProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return DISCOUNTS_RECORD;
    }
@Autowired
    UserPromotionLogDao userPromotionLogDao;
    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        GamerecordDTO gamerecordDTO = params.toJavaObject(GamerecordDTO.class);
        List<UserPromotionLogBean> gamerecord = userPromotionLogDao.getUserPromotion(gamerecordDTO.getType(),
                gamerecordDTO.getStartTime(), gamerecordDTO.getEndTime(),userid);
            result.setMsg(JSON.toJSON(gamerecord));

    }
}

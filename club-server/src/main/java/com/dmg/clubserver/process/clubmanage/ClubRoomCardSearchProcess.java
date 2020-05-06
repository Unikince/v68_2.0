package com.dmg.clubserver.process.clubmanage;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.ClubGameRecordDao;
import com.dmg.clubserver.dao.bean.ClubGameRecordBean;
import com.dmg.clubserver.model.vo.ClubRoomCardSearchVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.dmg.clubserver.config.MessageConfig.CLUB_ROOM_CARD_SEARCH;

/**
 * @Description 俱乐部房卡消耗查询
 * @Author mice
 * @Date 2019/6/3 17:09
 * @Version V1.0
 **/
@Service
public class ClubRoomCardSearchProcess implements AbstractMessageHandler {
    @Autowired
    private ClubGameRecordDao clubGameRecordDao;
    @Override
    public String getMessageId() {
        return CLUB_ROOM_CARD_SEARCH;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        ClubRoomCardSearchVO vo = params.toJavaObject(ClubRoomCardSearchVO.class);
        List<Integer> gameIds = clubGameRecordDao.selectGameId(vo.getClubId(),vo.getStartDate(),vo.getEndDate());
        if (CollectionUtil.isEmpty(gameIds)){
            result.setMsg(0);
            return;
        }
        List<Integer> ids = new ArrayList<>();
        gameIds.forEach(gameId -> {
            Integer id = clubGameRecordDao.selectId(StringUtils.join(gameIds,","));
            ids.add(id);
        });

        Integer costROomCard = clubGameRecordDao.selectCostRoomCard(StringUtils.join(ids,","));
        result.setMsg(costROomCard);
    }
}
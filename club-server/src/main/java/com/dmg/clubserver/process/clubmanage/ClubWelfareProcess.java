package com.dmg.clubserver.process.clubmanage;

import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import org.springframework.stereotype.Service;

import static com.dmg.clubserver.config.MessageConfig.CLUB_WELFARE;

/**
 * @Description 俱乐部福利
 * @Author mice
 * @Date 2019/6/3 17:09
 * @Version V1.0
 **/
@Service
public class ClubWelfareProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return CLUB_WELFARE;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        result.setMsg("亲爱的玩家：\n" +
                "       您好！为了回馈广大游戏玩家，我们官方经过协调，特此对一些消耗房卡数量多的玩家进行补助措施，即每个星期消耗房卡数量在10张以上（包含10张）的玩家皆可以联系客服，获得返利房卡。\n" +
                "奖励详情：\n" +
                "\t消耗10张房卡，返利：3张房卡；\n" +
                "\t消耗20张房卡，返利：5张房卡；\n" +
                "\t消耗30张房卡，返利：10张房卡；\n" +
                "\t消耗50张房卡，返利：15张房卡；\n" +
                "\t消耗100张房卡，返利：20张房卡。");
    }
}
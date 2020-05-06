package com.dmg.redblackwarserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.redblackwarserver.model.vo.BetVO;
import com.dmg.redblackwarserver.service.logic.BetService;
import com.dmg.redblackwarserver.tcp.server.AbstractMessageHandler;
import com.dmg.redblackwarserver.tcp.server.MessageIdConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 下注
 * @Author mice
 * @Date 2019/7/31 18:03
 * @Version V1.0
 **/
@Service
public class BetProcess implements AbstractMessageHandler {
    @Autowired
    private BetService betService;
    @Override
    public String getMessageId() {
        return MessageIdConfig.BET;
    }

    @Override
    public void messageHandler(int userId, JSONObject params) {
        BetVO vo = params.toJavaObject(BetVO.class);
        betService.playerBet(userId,vo.isCopyBet(),vo.getBetTableIndex(),vo.getBetChip());
    }
}
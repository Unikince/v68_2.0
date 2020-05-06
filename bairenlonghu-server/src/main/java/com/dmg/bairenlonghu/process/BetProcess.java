package com.dmg.bairenlonghu.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenlonghu.model.vo.BetVO;
import com.dmg.bairenlonghu.service.logic.BetService;
import com.dmg.bairenlonghu.tcp.server.AbstractMessageHandler;
import com.dmg.bairenlonghu.tcp.server.MessageIdConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 下注
 * @Author mice
 * @Date 2019/7/31 18:03
 * @Version V1.0
 **/
@Service
@Slf4j
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
        long start = System.currentTimeMillis();
        if (vo.isCopyBet()){
            betService.copyBet(userId);
        }else {
            betService.playerBet(userId,vo.getBetTableIndex(),vo.getBetChip());
        }
        log.info("============>下注耗时:{}",System.currentTimeMillis()-start);
    }
}
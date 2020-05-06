package com.dmg.doudizhuserver.business.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.doudizhuserver.core.msg.MessageHandler;

/**
 * 请求逻辑
 */
@Service
public class ReqLogic {
    @Autowired
    private RealizeLogic realize;
    @Autowired
    private ResLogic res;

    /** 登录游戏大厅 */
    @Component("Login")
    public class Login implements MessageHandler {
        @Override
        public void action(long playerId, JSONObject params) {
            ReqLogic.this.res.Login(playerId);
        }
    }

    /** 进入房间 */
    @Component("EnterRoom")
    public class EnterRoom implements MessageHandler {
        @Override
        public void action(long playerId, JSONObject params) {
            Integer roomId = params.getInteger("roomId");
            ReqLogic.this.realize.enterRoom(playerId, roomId);
        }
    }

    /** 玩家叫地主 */
    @Component("CallLandlord")
    public class CallLandlord implements MessageHandler {
        @Override
        public void action(long playerId, JSONObject params) {
            int call = params.getInteger("call");
            ReqLogic.this.realize.callLandlord(playerId, call);
        }
    }

    /** 玩家出牌 */
    @Component("PlayCards")
    public class PlayCards implements MessageHandler {
        @Override
        public void action(long playerId, JSONObject params) {
            List<Integer> cards = params.getJSONArray("cards").toJavaList(Integer.class);
            ReqLogic.this.realize.playCards(playerId, cards);
        }
    }

    /** 托管 */
    @Component("AutoPlay")
    public class AutoPlay implements MessageHandler {
        @Override
        public void action(long playerId, JSONObject params) {
            ReqLogic.this.realize.autoPlay(playerId);
        }
    }

    /** 请求退出房间 */
    @Component("ExitRoom")
    public class ExitRoom implements MessageHandler {
        @Override
        public void action(long playerId, JSONObject params) {
            ReqLogic.this.realize.exitRoom(playerId);
        }
    }

    /** 掉线 */
    @Component("LostOnline")
    public class LostOnline implements MessageHandler {
        @Override
        public void action(long playerId, JSONObject params) {
            ReqLogic.this.realize.lostOnline(playerId);
        }
    }

    /** 掉线 */
    @Component("9999")
    public class HeartbeatLost implements MessageHandler {
        @Override
        public void action(long playerId, JSONObject params) {
            ReqLogic.this.realize.heartbeatLost(playerId);
        }
    }
}

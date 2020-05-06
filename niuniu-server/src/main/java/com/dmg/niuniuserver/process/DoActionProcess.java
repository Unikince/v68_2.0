package com.dmg.niuniuserver.process;

import static com.dmg.niuniuserver.config.MessageConfig.DO_ACTION;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Player;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.constants.ActionType;
import com.dmg.niuniuserver.model.vo.DoActionVO;
import com.dmg.niuniuserver.platform.service.PlayerService;
import com.dmg.niuniuserver.service.PlayActionService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 玩家下注, 抢庄, 开牌等通用操作
 * @Author mice
 * @Date 2019/7/2 10:44
 * @Version V1.0
 **/
@Slf4j
@Service
public class DoActionProcess implements AbstractMessageHandler {

    @Autowired
    private PlayActionService playActionService;

    @Autowired
    private PlayerService playerService;

    @Override
    public String getMessageId() {
        return DO_ACTION;
    }

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        DoActionVO vo = params.toJavaObject(DoActionVO.class);
        Player player = this.playerService.getPlayer(userId);
        Seat seat = null;
        GameRoom gameRoom = RoomManager.instance().getRoom(player.getRoomId());
        if (gameRoom != null) {
            for (Seat s : gameRoom.getSeatMap().values()) {
                if (s.getPlayer().getUserId().intValue() == player.getUserId().intValue()) {
                    seat = s;
                    break;
                }
            }
        }
        switch (vo.getActionType()) {
            // 抢庄
            case ActionType.TYPE_QIANGZHUANG:
                this.playActionService.qiangZhuang(seat, vo.getMultiple());
                break;
            // 下注
            case ActionType.TYPE_XIAZHU:
                this.playActionService.xiaZhu(seat, vo.getMultiple(), gameRoom);
                break;
            // 开牌
            case ActionType.TYPE_KAIPAI:
                this.playActionService.kaiPai(gameRoom, seat, DO_ACTION);
                break;
            default:
        }
    }
}
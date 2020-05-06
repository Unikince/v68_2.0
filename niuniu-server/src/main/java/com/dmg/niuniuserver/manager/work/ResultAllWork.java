package com.dmg.niuniuserver.manager.work;

import static com.dmg.niuniuserver.config.MessageConfig.RESULT_ALL_INFO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dmg.common.core.util.SpringUtil;
import com.dmg.niuniuserver.SpringContextUtil;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.platform.service.PlayerService;
import com.dmg.niuniuserver.result.MessageResult;
import com.dmg.niuniuserver.service.PushService;
import com.zyhy.common_server.work.DelayTimeWork;

/**
 * 总结算延时任务
 */
public class ResultAllWork extends DelayTimeWork {
    private int roomId;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
    }

    @Override
    public void go() {
        PlayerService playerService = SpringUtil.getBean(PlayerService.class);

        PushService pushService = SpringContextUtil.getBean(PushService.class);
        GameRoom room = RoomManager.instance().getRoom(this.roomId);
        if (room != null) {
            // 总结算数据生成
            List<Map<String, Object>> resultAll = new ArrayList<>();

            for (Seat seat : room.getSeatMap().values()) {
                if (seat.getGameCount() <= 0) {
                    continue;
                }
                Map<String, Object> rs = new HashMap<>();
                // 头像
                rs.put("headImage", seat.getPlayer().getHeadImg());
                rs.put("nickname", seat.getPlayer().getNickname());
                rs.put("userId", seat.getPlayer().getUserId());
                if (room.isPrivateRoom() && room.getCustomRule().getGamePlay() == 5 && seat.getPlayer().getUserId() == room.getCreator()) {
                    rs.put("win", seat.getChipsRemain() - room.getCustomRule().getCarryFraction());
                } else {
                    rs.put("win", seat.getChipsRemain());
                }
                resultAll.add(rs);
            }
            MessageResult messageResult = new MessageResult(RESULT_ALL_INFO, resultAll);
            pushService.broadcast(messageResult, room);

            // 更新房间关系,不推送离开信息
            for (Seat seat1 : room.getSeatMap().values()) {
                playerService.syncRoom(0, seat1.getPlayer().getUserId());
            }
            // 本地清空房间
            RoomManager.instance().removeRoom(room.getRoomId());
        }
    }
}

package com.dmg.niuniuserver.manager.work;

import cn.hutool.core.util.RandomUtil;
import com.dmg.niuniuserver.SpringContextUtil;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.manager.TimerManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Robot;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.constants.ActionType;
import com.dmg.niuniuserver.model.constants.GameConfig;
import com.dmg.niuniuserver.model.constants.RoomStatus;
import com.dmg.niuniuserver.model.constants.SeatState;
import com.dmg.niuniuserver.service.PushService;
import com.zyhy.common_server.work.DelayTimeWork;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;

import java.util.HashMap;
import java.util.Map;

import static com.dmg.niuniuserver.config.MessageConfig.TURN_ACTION;
import static com.dmg.niuniuserver.result.ResultEnum.SUCCESS;

/**
 * 定庄任务结束调用
 *
 * @author: CharlesLee
 * @Date 2018/4/18 0018 20:54
 */
@Slf4j
public class NotifyPlayerNoteCountdownTask extends DelayTimeWork {

    private int roomId;

    @Override
    public void init(Object... args) {
        roomId = (int) args[0];
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void go() {
        PushService pushService = SpringContextUtil.getBean(PushService.class);
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room == null) {
            return;
        }
        if ((room.isPrivateRoom() && room.getCustomRule().getGamePlay() == 1) || !room.isPrivateRoom()) {
            if (room.getRoomStatus() != RoomStatus.STATE_DING_ZHUANG) {
                return;
            }
        }
        long xiazhuTime = System.currentTimeMillis() + GameConfig.XIA_ZHU_LENGTH_TIME;

        // 通知客户端下注
        log.info("通知客户端下注,房间{}", room.getRoomId());
        for (Seat seat : room.getSeatMap().values()) {
            if (!seat.isReady() || seat.getGameCount() <= 0) {
                continue;
            }
            seat.setStatus(SeatState.XIAZHU);
            HashMap<String, Object> maps = new HashMap();
            maps.put("actionType", ActionType.TYPE_XIAZHU);
            //倒计时
            maps.put("countdownTime", System.currentTimeMillis() + GameConfig.XIA_ZHU_LENGTH_TIME);
            // 下注倍数
            maps.put("betMultiple", room.getDiscardMul(seat));
            pushService.push(seat.getPlayer().getUserId(), TURN_ACTION, SUCCESS.getCode(), maps);
            /*if (seat.getSeatId() != room.getBankerSeat().getSeatId()) {
                HashMap<String, Object> maps = new HashMap();
                maps.put("actionType", ActionType.TYPE_XIAZHU);
                //倒计时
                maps.put("countdownTime", GameConfig.XIA_ZHU_LENGTH_TIME);
                // 下注倍数
                maps.put("betMultiple", room.getDiscardMul(seat));
                pushService.push(seat.getPlayer().getUserId(), TURN_ACTION, SUCCESS.getCode(), maps);
            } else {
                Map<String, Object> m = new HashMap();
                m.put("actionType", ActionType.TYPE_DING_ZHUANG);
                m.put("countdownTime", xiazhuTime);
                pushService.push(seat.getPlayer().getUserId(), TURN_ACTION, SUCCESS.getCode(), m);
            }*/
        }
        // 下注任务启动
        try {
            room.setRoomStatus(RoomStatus.STATE_XIAZHU);
            room.setPhaseCountdown(xiazhuTime);
            log.info("房间:{},下注任务启动", room.getRoomId());
            for (Seat seat : room.getSeatMap().values()) {
                if (seat.getPlayer() instanceof Robot && seat.getSeatId() != room.getBankerSeat().getSeatId()) {
                    int time = RandomUtil.randomInt(GameConfig.XIA_ZHU_LENGTH_TIME);
                    TimerManager.instance().submitDelayWork(BetCountDownWork.class, time, room.getRoomId(), seat.getSeatId());
                }
            }
            TimerManager.instance().submitDelayWork(BetCountDownWork.class, GameConfig.XIA_ZHU_LENGTH_TIME, room.getRoomId(), 0);
        } catch (SchedulerException e) {
            log.error("submit BetCountDownWork error:{}", e);
        }
    }
}

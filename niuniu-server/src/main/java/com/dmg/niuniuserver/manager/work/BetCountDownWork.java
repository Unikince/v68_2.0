package com.dmg.niuniuserver.manager.work;


import cn.hutool.core.util.RandomUtil;
import com.dmg.niuniuserver.SpringContextUtil;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.constants.RoomStatus;
import com.dmg.niuniuserver.model.constants.SeatState;
import com.dmg.niuniuserver.model.dto.NiuniuRobotActionDTO;
import com.dmg.niuniuserver.service.PlayActionService;
import com.dmg.niuniuserver.service.cache.RobotActionCacheService;
import com.dmg.niuniuserver.service.cache.impl.RobotActionCacheServiceImpl;
import com.zyhy.common_server.work.DelayTimeWork;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 下注倒计时结束以后处理
 *
 * @author: CharlesLee
 * @Date 2018/4/13 0013 18:08
 */
@Slf4j
public class BetCountDownWork extends DelayTimeWork {

    private int roomId;
    private int seatId;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
        this.seatId = (int) args[1];
    }

    @Override
    public void go() {
        PlayActionService playActionService = SpringContextUtil.getBean(PlayActionService.class);
        GameRoom room = RoomManager.instance().getRoom(roomId);

        if (room.getRoomStatus() != RoomStatus.STATE_XIAZHU) {
            return;
        }
        // 自动下注
        if (seatId != 0) {
            Seat seat = room.getSeatMap().get(seatId);
            // 判断玩家是否参与本局游戏
            if (seat.getStatus() == SeatState.STATE_JOIN_ROOM || seat.getStatus() == SeatState.STATE_WAIT_READY) {
                return;
            }
            if (seat.getBetChips() == 0) {
                RobotActionCacheService robotActionCacheService = SpringContextUtil.getBean(RobotActionCacheServiceImpl.class);
                int random = RandomUtil.randomInt(10000) + 1;
                //调用配置服务查询信息
                List<NiuniuRobotActionDTO> niuniuRobotActionDTOList = robotActionCacheService.getPressureInfoByRob(seat.getHandCardsType().getValue(),
                        room.getBankerFraction().get(seatId) == null ? 0 : room.getBankerFraction().get(seatId));
                niuniuRobotActionDTOList.sort((x, y) -> Integer.compare(x.getProbabilityPressure(), y.getProbabilityPressure()));
                Integer minProbability = 1;
                Integer maxProbability;
                int index = 0;
                for (NiuniuRobotActionDTO niuniuRobotActionDTO : niuniuRobotActionDTOList) {
                    maxProbability = minProbability + niuniuRobotActionDTO.getProbabilityPressure();
                    if (random < maxProbability && random >= minProbability) {
                        index = niuniuRobotActionDTO.getRobType();
                        break;
                    }
                    minProbability += niuniuRobotActionDTO.getProbabilityPressure();
                }
                playActionService.xiaZhu(seat, room.discard[index], room);
            }
        } else {
            for (Seat seat : room.getSeatMap().values()) {
                if (seat.getSeatId() == room.getBankerSeat().getSeatId() || !seat.isReady()) {
                    continue;
                }
                if (seat.getGameCount() <= 0) {
                    continue;
                }
                if (seat.getBetChips() == 0) {
                    // 下注限制规则
                    if (room.isPrivateRoom() && room.getCustomRule().isXiaZhuLimit()) {
                        for (Seat s : room.getMaxQiangZhuangList()) {
                            if (s.getPlayer().getUserId().intValue() == seat.getPlayer().getUserId().intValue()) {
                                playActionService.xiaZhu(seat, room.discard[1], room);
                            }
                        }
                    } else {
                        playActionService.xiaZhu(seat, room.discard[0], room);
                    }
                }
            }
        }
    }
}

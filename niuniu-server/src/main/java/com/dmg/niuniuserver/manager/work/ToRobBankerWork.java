package com.dmg.niuniuserver.manager.work;


import cn.hutool.core.util.RandomUtil;
import com.dmg.niuniuserver.SpringContextUtil;
import com.dmg.niuniuserver.config.MessageConfig;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.constants.ActionType;
import com.dmg.niuniuserver.model.constants.RoomStatus;
import com.dmg.niuniuserver.model.constants.SeatState;
import com.dmg.niuniuserver.model.dto.NiuniuRobotActionDTO;
import com.dmg.niuniuserver.model.dto.QiangZhuangDTO;
import com.dmg.niuniuserver.result.MessageResult;
import com.dmg.niuniuserver.service.PlayActionService;
import com.dmg.niuniuserver.service.PushService;
import com.dmg.niuniuserver.service.RoomService;
import com.dmg.niuniuserver.service.cache.RobotActionCacheService;
import com.dmg.niuniuserver.service.cache.impl.RobotActionCacheServiceImpl;
import com.zyhy.common_server.work.DelayTimeWork;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author mice
 * @description: 抢庄倒计时结束以后处理
 * @return
 * @date 2019/7/2
 */
@Slf4j
public class ToRobBankerWork extends DelayTimeWork {

    private int roomId;
    private int seatId;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
        this.seatId = (int) args[1];
    }

    @Override
    public void go() {
        RoomService roomService = SpringContextUtil.getBean(RoomService.class);
        PlayActionService playActionService = SpringContextUtil.getBean(PlayActionService.class);
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room == null || room.getRoomStatus() != RoomStatus.STATE_QIANG_ZHUANG) {
            return;
        }
        if (seatId != 0) {
            Seat seat = room.getSeatMap().get(seatId);
            if (seat != null) {
                RobotActionCacheService robotActionCacheService = SpringContextUtil.getBean(RobotActionCacheServiceImpl.class);
                int random = RandomUtil.randomInt(10000) + 1;
                //调用配置服务查询信息
                List<NiuniuRobotActionDTO> niuniuRobotActionDTOList = robotActionCacheService.getRobInfoByCard(seat.getHandCardsType().getValue());
                niuniuRobotActionDTOList.sort((x, y) -> Integer.compare(x.getProbabilityRob(), y.getProbabilityRob()));
                Integer minProbability = 1;
                Integer maxProbability;
				int index = 0;
                for (NiuniuRobotActionDTO niuniuRobotActionDTO : niuniuRobotActionDTOList) {
                    maxProbability = minProbability + niuniuRobotActionDTO.getProbabilityRob();
                    if (random < maxProbability && random >= minProbability) {
						index = niuniuRobotActionDTO.getRobType();
						break;
                    }
                    minProbability += niuniuRobotActionDTO.getProbabilityRob();
                }
                playActionService.qiangZhuang(seat, room.betMultiple[index]);
            }
        } else {
            if (room.getRoomStatus() != RoomStatus.STATE_QIANG_ZHUANG) {
                return;
            }
            PushService pushService = SpringContextUtil.getBean(PushService.class);
            for (Seat seat : room.getSeatMap().values()) {
                if (seat.isReady() && seat.getGameCount() > 0 && seat.getStatus() != SeatState.QIANGZHUANG_OVER) {
                    QiangZhuangDTO qiangZhuangDTO = new QiangZhuangDTO();
                    qiangZhuangDTO.setSeatId(seat.getSeatId());
                    qiangZhuangDTO.setActionType(ActionType.TYPE_QIANGZHUANG);
                    qiangZhuangDTO.setMultiple(0);
                    qiangZhuangDTO.setUserId(seat.getPlayer().getUserId());
                    MessageResult messageResult = new MessageResult(MessageConfig.DO_ACTION_RESULT_NTC, qiangZhuangDTO);
                    pushService.broadcast(messageResult, room);
                }
            }

            log.debug("定庄2,房间号为{}", room.getRoomId());
            roomService.definitionBanker(room, null);
        }
    }
}

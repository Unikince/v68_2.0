package com.dmg.data.client;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.dmg.data.client.net.NettyClientMap;
import com.dmg.data.common.constant.MsgConst;
import com.dmg.data.common.constant.NettyErrorEnum;
import com.dmg.data.common.dto.BetRecvDto;
import com.dmg.data.common.dto.BetSendDto;
import com.dmg.data.common.dto.GoldPayRecvDto;
import com.dmg.data.common.dto.GoldPaySendDto;
import com.dmg.data.common.dto.SettleRecvDto;
import com.dmg.data.common.dto.SettleSendDto;
import com.dmg.data.common.dto.SyncRoomRecvDto;
import com.dmg.data.common.dto.SyncRoomSendDto;
import com.dmg.data.common.dto.UserRecvDto;
import com.dmg.data.common.dto.UserSendDto;
import com.dmg.data.common.message.RecvMsg;

/**
 * netty客户端调用发送消息
 */
@Component
public class NettySend {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    /** 获取玩家信息 */
    public UserRecvDto getUser(UserSendDto sendDto) {
        if (sendDto == null) {
            this.log.error("参数sendDto为空");
            return UserRecvDto.builder().code(-1).build();
        }
        RecvMsg recvMsg = NettyClientMap.sendMsg(MsgConst.getUser, sendDto);
        if (recvMsg == null) {
            this.log.error("玩家[{}]未找到", sendDto.getUserId());
            return UserRecvDto.builder().code(1).build();
        }
        if (recvMsg.getCode() == 0) {
            return JSON.parseObject(recvMsg.getData(), UserRecvDto.class);
        } else if (recvMsg.getCode() == NettyErrorEnum.PLAYER_NOT_FIND.getCode()) {
            this.log.error("玩家[{}]未找到", sendDto.getUserId());
            return UserRecvDto.builder().code(1).build();
        } else {
            this.log.error("获取玩家信息错误[{}][{}]", recvMsg.getClass(), recvMsg.getData());
            return UserRecvDto.builder().code(-1).build();
        }
    }

    /** 下注 */
    public BetRecvDto bet(BetSendDto sendDto) {
        if (sendDto == null) {
            this.log.error("参数sendDto为空");
            return BetRecvDto.builder().code(-1).build();
        }
        if (sendDto.getDecGold().compareTo(BigDecimal.ZERO) > 0) {
            this.log.error("参数错误{}", JSON.toJSONString(sendDto));
            return BetRecvDto.builder().code(-1).build();
        }
        RecvMsg recvMsg = NettyClientMap.sendMsg(MsgConst.bet, sendDto);
        if (recvMsg.getCode() == 0) {
            return JSON.parseObject(recvMsg.getData(), BetRecvDto.class);
        } else if (recvMsg.getCode() == NettyErrorEnum.GOLD_NOT_ENOUGH.getCode()) {
            this.log.error("玩家[{}]金币不足", sendDto.getUserId());
            return BetRecvDto.builder().code(1).build();
        } else {
            this.log.error("下注错误[{}][{}]", recvMsg.getClass(), recvMsg.getData());
            return BetRecvDto.builder().code(-1).build();
        }
    }

    /** 下注 */
    public void betAsync(BetSendDto sendDto) {
        if (sendDto == null) {
            this.log.error("参数sendDto为空");
            return;
        }
        if (sendDto.getDecGold().compareTo(BigDecimal.ZERO) > 0) {
            this.log.error("参数错误{}", JSON.toJSONString(sendDto));
            return;
        }
        NettyClientMap.sendMsgNotResult(MsgConst.bet, sendDto);
    }

    /** 结算 */
    public SettleRecvDto settle(SettleSendDto sendDto) {
        if (sendDto == null) {
            this.log.error("参数sendDto为空");
            return SettleRecvDto.builder().code(-1).build();
        }
        RecvMsg recvMsg = NettyClientMap.sendMsg(MsgConst.settle, sendDto);
        if (recvMsg.getCode() == 0) {
            return JSON.parseObject(recvMsg.getData(), SettleRecvDto.class);
        } else {
            this.log.error("结算错误[{}][{}]", recvMsg.getClass(), recvMsg.getData());
            return SettleRecvDto.builder().code(-1).build();
        }
    }

    /** 结算 */
    public void settleAsync(SettleSendDto sendDto) {
        if (sendDto == null) {
            this.log.error("参数sendDto为空");
            return;
        }
        NettyClientMap.sendMsgNotResult(MsgConst.settle, sendDto);
    }

    /** 金币充值 */
    public GoldPayRecvDto goldPay(GoldPaySendDto sendDto) {
        if (sendDto == null) {
            this.log.error("参数sendDto为空");
            return GoldPayRecvDto.builder().code(-1).build();
        }
        RecvMsg recvMsg = NettyClientMap.sendMsg(MsgConst.goldPay, sendDto);
        if (recvMsg.getCode() == 0) {
            return JSON.parseObject(recvMsg.getData(), GoldPayRecvDto.class);
        } else {
            this.log.error("金币充值错误[{}][{}]", recvMsg.getClass(), recvMsg.getData());
            return GoldPayRecvDto.builder().code(-1).build();
        }
    }

    /** 金币充值 */
    public void goldPayAsync(GoldPaySendDto sendDto) {
        if (sendDto == null) {
            this.log.error("参数sendDto为空");
            return;
        }
        NettyClientMap.sendMsgNotResult(MsgConst.goldPay, sendDto);
    }

    /**
     * 同步房间
     */
    public SyncRoomRecvDto syncRoom(SyncRoomSendDto sendDto) {
        if (sendDto == null) {
            this.log.error("参数sendDto为空");
            return null;
        }
        if (sendDto.getGameId() == 0) {
            this.log.error("参数错误{}", JSON.toJSONString(sendDto));
            return null;
        }
        if (sendDto.getRoomLevel() == 0 || sendDto.getRoomId() == 0) {
            sendDto.setRoomLevel(0);
            sendDto.setRoomId(0);
        }
        RecvMsg recvMsg = NettyClientMap.sendMsg(MsgConst.syncRoom, sendDto);
        if (recvMsg.getCode() == 0) {
            return JSON.parseObject(recvMsg.getData(), SyncRoomRecvDto.class);
        } else {
            this.log.error("同步房间错误[{}][{}]", recvMsg.getClass(), recvMsg.getData());
            return null;
        }
    }

    /**
     * 同步房间
     */
    public void syncRoomAsync(SyncRoomSendDto sendDto) {
        if (sendDto == null) {
            this.log.error("参数sendDto为空");
            return;
        }
        if (sendDto.getGameId() == 0) {
            this.log.error("参数错误{}", JSON.toJSONString(sendDto));
            return;
        }
        if (sendDto.getRoomLevel() == 0 || sendDto.getRoomId() == 0) {
            sendDto.setRoomLevel(0);
            sendDto.setRoomId(0);
        }
        NettyClientMap.sendMsgNotResult(MsgConst.syncRoom, sendDto);
    }
}

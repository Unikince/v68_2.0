package com.dmg.dataserver.business.controller;

import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.dmg.data.common.constant.MsgConst;
import com.dmg.data.common.constant.NettyErrorEnum;
import com.dmg.data.common.dto.BetRecvDto;
import com.dmg.data.common.dto.BetSendDto;
import com.dmg.data.common.dto.GoldPayPushDto;
import com.dmg.data.common.dto.GoldPayRecvDto;
import com.dmg.data.common.dto.GoldPaySendDto;
import com.dmg.data.common.dto.SettleRecvDto;
import com.dmg.data.common.dto.SettleSendDto;
import com.dmg.data.common.dto.SyncRoomSendDto;
import com.dmg.data.common.dto.UserRecvDto;
import com.dmg.data.common.dto.UserSendDto;
import com.dmg.data.common.message.NettyMsgException;
import com.dmg.data.common.message.RecvMsg;
import com.dmg.data.common.message.SendMsg;
import com.dmg.dataserver.business.model.BaseUser;
import com.dmg.dataserver.business.model.User;
import com.dmg.dataserver.business.service.PlatfromService;
import com.dmg.dataserver.business.service.UserCacheService;
import com.dmg.dataserver.common.net.annotation.ActionMapping;
import com.dmg.dataserver.common.net.annotation.NettyController;
import com.dmg.dataserver.common.net.socket.NettyServerMap;

import cn.hutool.core.bean.BeanUtil;

@NettyController
public class PlatfromHandler {
    @Autowired
    private PlatfromService platfromService;
    @Autowired
    private UserCacheService userCacheService;

    /** 获取玩家信息 */
    @ActionMapping(key = MsgConst.getUser)
    public RecvMsg getUser(SendMsg sendMsg) {
        UserSendDto sendDto = null;
        try {
            sendDto = JSON.parseObject(sendMsg.getData(), UserSendDto.class);
        } catch (Exception e) {
            throw new NettyMsgException(NettyErrorEnum.PARAM_ERROR, sendMsg.getData(), e);
        }

        BaseUser baseUser = null;
        try {
            baseUser = this.platfromService.getUser(sendDto);
        } catch (Exception e) {
            if (e instanceof NettyMsgException) {
                throw e;
            } else {
                throw new NettyMsgException(NettyErrorEnum.UNKNOWN, e);
            }
        }
        if (baseUser == null) {
            throw new NettyMsgException(NettyErrorEnum.PLAYER_NOT_FIND, "" + sendDto.getUserId());
        }

        long userId = sendDto.getUserId();
        User user = this.userCacheService.getCacheUser(userId);
        if (user == null) {
            user = new User();
            user.setId(userId);
        }

        user.setNickname(baseUser.getNickname());
        user.setHeadImage(baseUser.getHeadImage());
        user.setSex(baseUser.getSex());
        user.setGold(baseUser.getGold());
        this.userCacheService.update(user);

        UserRecvDto recvDto = new UserRecvDto();
        BeanUtils.copyProperties(user, recvDto);
        return new RecvMsg(recvDto);
    }

    /** 下注 */
    @ActionMapping(key = MsgConst.bet)
    public RecvMsg bet(SendMsg sendMsg) {
        BetSendDto sendDto = null;
        try {
            sendDto = JSON.parseObject(sendMsg.getData(), BetSendDto.class);
        } catch (Exception e) {
            throw new NettyMsgException(NettyErrorEnum.PARAM_ERROR, sendMsg.getData(), e);
        }

        if (sendDto.getDecGold().compareTo(BigDecimal.ZERO) > 0) {
            throw new NettyMsgException(NettyErrorEnum.GOLD_ERROR, "" + sendDto.getDecGold());
        }

        BetRecvDto recvDto = null;
        try {
            recvDto = this.platfromService.bet(sendDto);
        } catch (Exception e) {
            if (e instanceof NettyMsgException) {
                throw e;
            } else {
                throw new NettyMsgException(NettyErrorEnum.UNKNOWN, e);
            }
        }
        return new RecvMsg(recvDto);
    }

    /** 结算 */
    @ActionMapping(key = MsgConst.settle)
    public RecvMsg settle(SendMsg sendMsg) {
        SettleSendDto sendDto = null;
        try {
            sendDto = JSON.parseObject(sendMsg.getData(), SettleSendDto.class);
        } catch (Exception e) {
            throw new NettyMsgException(NettyErrorEnum.PARAM_ERROR, sendMsg.getData(), e);
        }
        SettleRecvDto recvDto = null;

        try {
            recvDto = this.platfromService.settle(sendDto);
        } catch (Exception e) {
            if (e instanceof NettyMsgException) {
                throw e;
            } else {
                throw new NettyMsgException(NettyErrorEnum.UNKNOWN, e);
            }
        }
        return new RecvMsg(recvDto);
    }

    /** 金币充值提款 */
    @ActionMapping(key = MsgConst.goldPay)
    public void goldPay(SendMsg sendMsg) {
        GoldPaySendDto sendDto = null;
        try {
            sendDto = JSON.parseObject(sendMsg.getData(), GoldPaySendDto.class);
        } catch (Exception e) {
            throw new NettyMsgException(NettyErrorEnum.PARAM_ERROR, sendMsg.getData(), e);
        }
        GoldPayRecvDto recvDto = null;
        try {
            recvDto = this.platfromService.goldPay(sendDto);
        } catch (Exception e) {
            if (e instanceof NettyMsgException) {
                throw e;
            } else {
                throw new NettyMsgException(NettyErrorEnum.UNKNOWN, e);
            }
        }
        RecvMsg recvMsg = new RecvMsg();
        recvMsg.setMsgId(MsgConst.goldPay);
        recvMsg.setUnique(sendMsg.getUnique());
        recvMsg.setData(recvDto);
        NettyServerMap.send(sendMsg.getClientId(), recvMsg);

        GoldPayPushDto pushDto = new GoldPayPushDto();
        BeanUtil.copyProperties(sendDto, pushDto);

        User user = this.userCacheService.getCacheUser(sendDto.getUserId());
        if (user == null || user.getGameId() == 0) {
            NettyServerMap.pushAll(MsgConst.goldPay, pushDto);
        } else {
            NettyServerMap.push(user.getServer(), MsgConst.goldPay, pushDto);
        }
    }

    /** 同步房间 */
    @ActionMapping(key = MsgConst.syncRoom)
    public RecvMsg syncRoom(SendMsg sendMsg) {
        SyncRoomSendDto sendDto = null;
        try {
            sendDto = JSON.parseObject(sendMsg.getData(), SyncRoomSendDto.class);
        } catch (Exception e) {
            throw new NettyMsgException(NettyErrorEnum.PARAM_ERROR, sendMsg.getData(), e);
        }

        if (sendDto.getGameId() == 0) {
            throw new NettyMsgException(NettyErrorEnum.PARAM_ERROR, sendMsg.getData());
        }
        try {
            User user = this.userCacheService.getCacheUser(sendDto.getUserId());
            if (user == null) {
                throw new NettyMsgException(NettyErrorEnum.PLAYER_NOT_FIND, "" + sendDto.getUserId());
            }
            if (sendDto.getRoomLevel() == 0 || sendDto.getRoomId() == 0) {
                user.setGameId(0);
                user.setRoomLevel(0);
                user.setRoomId(0);
            } else {
                user.setGameId(sendDto.getGameId());
                user.setRoomLevel(sendDto.getRoomLevel());
                user.setRoomId(sendDto.getRoomId());
            }
            user.setServer(sendMsg.getClientId());
            this.userCacheService.update(user);
        } catch (Exception e) {
            if (e instanceof NettyMsgException) {
                throw e;
            } else {
                throw new NettyMsgException(NettyErrorEnum.UNKNOWN, e);
            }
        }
        return new RecvMsg(new SyncRoomSendDto());
    }
}
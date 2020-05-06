package com.dmg.dataserver.business.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.data.common.constant.NettyErrorEnum;
import com.dmg.data.common.dto.BetRecvDto;
import com.dmg.data.common.dto.BetSendDto;
import com.dmg.data.common.dto.GoldPayRecvDto;
import com.dmg.data.common.dto.GoldPaySendDto;
import com.dmg.data.common.dto.SettleRecvDto;
import com.dmg.data.common.dto.SettleSendDto;
import com.dmg.data.common.dto.UserSendDto;
import com.dmg.data.common.message.NettyMsgException;
import com.dmg.dataserver.business.model.BaseUser;
import com.dmg.dataserver.business.platfrom.send.http.HttpSend;
import com.dmg.dataserver.business.platfrom.send.socket.SocketSend;

@Service
public class PlatfromService {
    @Autowired
    private HttpSend httpSend;

    @Autowired
    private SocketSend socketSend;

    /** 获取玩家信息 */
    public BaseUser getUser(UserSendDto sendDto) {
        long userId = sendDto.getUserId();
        BaseUser user = this.httpSend.getUser(userId);
        if (user == null) {
            throw new NettyMsgException(NettyErrorEnum.PLAYER_NOT_FIND, "" + userId);
        }
        return user;
    }

    /** 下注 */
    public BetRecvDto bet(BetSendDto sendDto) {
        int gameId = sendDto.getGameId();
        long userId = sendDto.getUserId();
        BigDecimal decGold = sendDto.getDecGold();

        if (decGold.compareTo(BigDecimal.ZERO) < 0) {
            this.socketSend.changeGold(gameId, userId, decGold);
        } else {
            throw new NettyMsgException(NettyErrorEnum.GOLD_ERROR, "" + sendDto.getDecGold());
        }
        UserSendDto userSendDto = UserSendDto.builder().userId(userId).build();
        BaseUser user = this.getUser(userSendDto);
        BigDecimal gold = user.getGold();
        return BetRecvDto.builder().curGold(gold).build();
    }

    /** 结算 */
    public SettleRecvDto settle(SettleSendDto sendDto) {
        int gameId = sendDto.getGameId();
        long userId = sendDto.getUserId();
        BigDecimal changeGold = sendDto.getChangeGold();
        this.socketSend.changeGold(gameId, userId, changeGold);
        UserSendDto userSendDto = UserSendDto.builder().userId(userId).build();
        BaseUser user = this.getUser(userSendDto);
        BigDecimal gold = user.getGold();
        return SettleRecvDto.builder().curGold(gold).build();
    }

    /** 金币充值 */
    public GoldPayRecvDto goldPay(GoldPaySendDto sendDto) {
        long userId = sendDto.getUserId();
        BigDecimal payGold = sendDto.getPayGold();
        this.socketSend.changeGold(sendDto.getType(), userId, payGold);
        return GoldPayRecvDto.builder().build();
    }
}
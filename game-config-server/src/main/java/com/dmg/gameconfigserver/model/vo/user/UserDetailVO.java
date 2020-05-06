package com.dmg.gameconfigserver.model.vo.user;

import com.dmg.agentserviceapi.business.agentrelation.model.dto.AgentRelationGifaudRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.AgentSettleRecordGifaudRes;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerChargeAndWithdrawInfo;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:00 2019/11/21
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetailVO {
    /** 玩家基础信息 */
    private UserVO user;
    /** 银行卡绑定 */
    private UserBindingCardVO bindingCard;
    /** 玩家充值提款信息 */
    private PlayerChargeAndWithdrawInfo chargeAndWithdrawInfo;
    /** 代理关系 */
    private AgentRelationGifaudRes agentRelation;
    /** 代理业绩 */
    private AgentSettleRecordGifaudRes agentPerformance;
}

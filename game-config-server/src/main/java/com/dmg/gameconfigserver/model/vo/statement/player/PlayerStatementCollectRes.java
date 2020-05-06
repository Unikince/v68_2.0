package com.dmg.gameconfigserver.model.vo.statement.player;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.vo.statement.player.common.PlayerStatementCommonInfoRes;

import lombok.Data;

/**
 * 玩家报表_汇总_返回
 */
@Data
public class PlayerStatementCollectRes {
    /** 总注册人数 */
    private long allRegister;
    /** 报表数据 */
    private IPage<PlayerStatementCommonInfoRes> datas;
}
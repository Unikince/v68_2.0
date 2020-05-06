package com.dmg.gameconfigserver.model.dto.record;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:20 2019/11/20
 */
@Data
public class GameRecordDTO extends PageReqDTO {
    //游戏id
    @NotNull(message = "gameId不能为空")
    private Integer gameId;
    //玩家id
    private Long userId;
    //查询起期
    private Date startDate;
    //查询止期
    private Date endDate;
}

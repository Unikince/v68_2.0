package com.dmg.gameconfigserver.model.vo.user;

import com.dmg.server.common.model.dto.PageConditionDTO;
import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2020/3/25 11:35
 * @Version V1.0
 **/
@Data
public class GoodNumberPageVO extends PageConditionDTO {
    // 用户id
    private Long userId;
    // 靓号id
    private Long goodNumber;
}
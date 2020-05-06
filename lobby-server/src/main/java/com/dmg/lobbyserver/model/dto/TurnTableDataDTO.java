package com.dmg.lobbyserver.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 转盘数据DTO
 * Author:刘将军
 * Time:2019/6/20 9:52
 * Created by IntelliJ IDEA Community
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TurnTableDataDTO  extends GiftDataDTO{
    private Long id;
    /**
     * 转盘编号
     */
    private Integer itemOrder;

}

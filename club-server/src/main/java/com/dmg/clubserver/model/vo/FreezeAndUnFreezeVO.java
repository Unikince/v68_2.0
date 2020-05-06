package com.dmg.clubserver.model.vo;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/28 16:58
 * @Version V1.0
 **/
@Data
public class FreezeAndUnFreezeVO {
    private Integer roleId;
    private Integer clubId;
    private Integer operatorId;
    // 0:解冻 1:冻结
    private Integer freezeStatus;

}
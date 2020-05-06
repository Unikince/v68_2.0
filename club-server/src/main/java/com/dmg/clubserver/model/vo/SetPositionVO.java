package com.dmg.clubserver.model.vo;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/28 17:48
 * @Version V1.0
 **/
@Data
public class SetPositionVO {
    private Integer roleId;
    private Integer clubId;
    private Integer operatorId;
    private Integer position;
}
package com.dmg.clubserver.model.vo;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/1 15:43
 * @Version V1.0
 **/
@Data
public class JoinTableSuccessVO {
    private Integer roleId;
    private Integer clubId;
    private Integer roomId;
    private Integer seatNum;
}
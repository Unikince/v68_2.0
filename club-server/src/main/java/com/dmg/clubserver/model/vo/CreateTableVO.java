package com.dmg.clubserver.model.vo;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/1 15:29
 * @Version V1.0
 **/
@Data
public class CreateTableVO {
    private Integer roleId;
    private Integer clubId;
    private Integer costRoomCard;
    private Integer tableNum;
    private String tableMsg;
}
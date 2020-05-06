package com.dmg.clubserver.model.table;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/30 16:45
 * @Version V1.0
 **/
@Data
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 位置编号
     */
    private Integer seatNum;
    /**
     * 玩家id
     */
    private Integer roleId;
    /**
     * 头像
     */
    private String headImage;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 在线状态
     */
    private Integer onlineStatus;
}
package com.dmg.clubserver.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/27 19:28
 * @Version V1.0
 **/
@Data
public class ClubPlayerListDTO implements Serializable {
    private static final long serialVersionUID = 5716628787719790372L;
    private String headImage;
    private String nickName;
    private Integer level;
    private Integer roleId;
    // 是否在线 0:否 1:是
    private Integer online;
    private Date onlineDate;
    // 玩家状态 (0:解冻 1:冻结)
    private Integer playerStatus;
    // 是否是俱乐部成员 0:不是 1:是
    private Integer clubPlayer;
    // 职位
    private Integer position;

}
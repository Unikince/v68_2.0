package com.dmg.bjlserver.business.platform.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 玩家信息
 */
@Data
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 玩家id */
    private long id;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String headImg;
    /** 性别 1:男 2:女 */
    private int sex;
    /** 金币 */
    private long gold;
    /** 是否是机器人 */
    private boolean robot;
    /** 是否加入房间 */
    private boolean play;
}

package com.dmg.bjlserver.business.platform.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 机器人实体类
 */
@Data
public class RobotBean implements Serializable {
    private static final long serialVersionUID = 3715391800435026080L;
    /** 逻辑键 */
    private Integer id;
    /** 玩家唯一id */
    private Long userId;
    /** 头像地址 */
    private String headImg;
    /** 昵称 */
    private String nickname;
    /** 性别 1:男 2:女 */
    private Integer sex;
    /** 金币 */
    private long gold;
    /** 省份 */
    private String province;
    /** 城市 */
    private String city;
}

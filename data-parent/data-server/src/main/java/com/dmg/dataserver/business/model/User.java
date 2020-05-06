package com.dmg.dataserver.business.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户数据信息（包括本地服务器信息）
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseUser {
    /** 游戏id */
    private int gameId;
    /** 玩家所在服务器 */
    private String server;
    /** 房间等级 */
    private int roomLevel;
    /** 房间id */
    private int roomId;
}

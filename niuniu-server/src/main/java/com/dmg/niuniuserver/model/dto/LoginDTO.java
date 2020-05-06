package com.dmg.niuniuserver.model.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/3 10:10
 * @Version V1.0
 **/
@Data
public class LoginDTO {
    // 用户信息
    private PlayerDTO playerDTO;
    // 房间配置信息
    private List<Map<String,Object>> roomConfig;

    @Data
    public static class PlayerDTO {
        protected Long userId;
        protected String nickname;
        protected int sex;
        protected String headImg;
        protected int roomId; // 当前加入的房间
        protected int platform; // 渠道
        protected double gold; // 游戏币
        protected int playRounds;//当前房间玩牌局数

    }
}
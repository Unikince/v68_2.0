package com.dmg.gameconfigserver.model.dto.sys;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/25 17:37
 * @Version V1.0
 **/
@Data
public class ServerMaintainVO {
    // 需要维护的游戏id
    private List<Integer> gameIds;

}
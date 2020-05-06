package com.dmg.zhajinhuaserver.model.vo;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/4 17:26
 * @Version V1.0
 **/
@Data
public class SyncPlayerGoldVO {
    private Long userId;
    private Long gold;
}
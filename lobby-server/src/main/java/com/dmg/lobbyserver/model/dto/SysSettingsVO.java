package com.dmg.lobbyserver.model.dto;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/21 17:24
 * @Version V1.0
 **/
@Data
public class SysSettingsVO {
    private Long userId;
    // 1:指纹key 2: 手势key 3:面部key
    private Integer keyType;
    private String key;

}
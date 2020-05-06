package com.dmg.lobbyserver.model.dto;

import lombok.Data;

import java.util.Map;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/18 11:06
 * @Version V1.0
 **/
@Data
public class HttpLoginDTO {
    private Long userId;
    private String userName;
    private String ip;
    private String port;
    private String sign;
    private Map<String,Object> versionInfo;
}
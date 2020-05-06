package com.dmg.lobbyserver.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/23 16:22
 * @Version V1.0
 **/
@Data
public class JQLoginVO {
    private String headImg;
    private String phone;
    @NotBlank
    private String nickName;
    private int sex;
    private String accessToken;
    @NotBlank
    private Long userId;
    private long expiredTime;
    private int age;
    private String username;
    @NotBlank
    private String deviceCode;
    /**
     * 渠道code
     */
    @NotBlank
    private String channelCode;

}
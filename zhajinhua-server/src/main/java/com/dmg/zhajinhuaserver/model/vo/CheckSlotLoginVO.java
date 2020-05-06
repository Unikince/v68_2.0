package com.dmg.zhajinhuaserver.model.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author jock
 * @Date 2019/7/19 10:49
 * @Version V1.0
 **/
@Data
public class CheckSlotLoginVO {
    @NotNull
    private Integer channelId;
    @NotNull
    private Integer subChannelId;
    @NotNull
    private Integer userId;
    @NotBlank
    private String token;
}
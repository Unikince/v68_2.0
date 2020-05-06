package com.dmg.lobbyserver.model.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @Description
 * @Author mice
 * @Date 2020/2/6 10:47
 * @Version V1.0
 **/
@Data
public class YYChangeMobileVO {
    @NotBlank
    private String mobile;
    @NotBlank
    private String newMobile;
    @NotBlank
    private String vertifyCode;
    @NotBlank
    private String token;
    @NotBlank
    private String channelCode;
}
package com.dmg.lobbyserver.model.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/18 14:25
 * @Version V1.0
 **/
@Data
public class KeyLoginVO {
    @NotBlank
    private String sign;
    @NotNull
    private Long userId;
    private String deviceCode;
}
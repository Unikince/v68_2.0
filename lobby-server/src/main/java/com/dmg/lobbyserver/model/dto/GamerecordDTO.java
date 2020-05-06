package com.dmg.lobbyserver.model.dto;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Description
 * @Author jock
 * @Date 2019/6/20 0020
 * @Version V1.0
 **/
@Data
public class GamerecordDTO {
    @NotBlank
    private int type;//类型
    @NotBlank
    private String startTime;//开始时间
    @NotBlank
    private String endTime;//结束时间
}

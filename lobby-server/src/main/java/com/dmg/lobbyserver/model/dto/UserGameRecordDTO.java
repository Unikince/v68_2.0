package com.dmg.lobbyserver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.NotBlank;
import java.util.Date;

/**
 * @Description
 * @Author jock
 * @Date 2019/6/20 0020
 * @Version V1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGameRecordDTO {

    private Integer gameId;//游戏

    private String startTime;//开始时间

    private String endTime;//结束时间

    private Long userId;
}

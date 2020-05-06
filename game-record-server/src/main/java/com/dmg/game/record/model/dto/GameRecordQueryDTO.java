package com.dmg.game.record.model.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:49 2019/11/29
 */
@Data
public class GameRecordQueryDTO {

    @NotNull
    private Integer gameId;//游戏id

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;//开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//结束时间

    @NotNull
    private Long userId;
}

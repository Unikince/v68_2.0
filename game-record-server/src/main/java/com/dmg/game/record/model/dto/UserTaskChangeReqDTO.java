package com.dmg.game.record.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:44 2019/11/26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskChangeReqDTO {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 任务类型
     */
    private Integer taskType;
    /**
     * 变更值
     */
    private Integer number;
    /**
     * 最大变更值
     */
    private Integer maxNumber;
}

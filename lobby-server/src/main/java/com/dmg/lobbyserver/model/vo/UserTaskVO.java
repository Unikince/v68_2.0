package com.dmg.lobbyserver.model.vo;

import com.dmg.lobbyserver.model.dto.UserTaskDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:42 2019/11/27
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserTaskVO {
    /**
     * 任务列表
     */
    private List<UserTaskDTO> userTask;
    /**
     * 活跃度任务列表
     */
    private List<UserTaskDTO> activityLevelTask;
    /**
     * 活跃度
     */
    private Double activityLevel;
    /**
     * 用户code
     */
    private Long userId;
}

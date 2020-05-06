package com.dmg.lobbyserver.controller.task;

import com.dmg.common.core.web.Result;
import com.dmg.lobbyserver.model.dto.UserTaskChangeReqDTO;
import com.dmg.lobbyserver.service.UserTaskProgressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:41 2019/11/26
 */
@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private UserTaskProgressService userTaskProgressService;

    /**
     * @Author liubo
     * @Description //TODO 任务进度变更
     * @Date 17:09 2019/11/26
     **/
    @PostMapping("userTaskChange")
    public Result userTaskChange(@RequestBody List<UserTaskChangeReqDTO> req) {
        if (req != null && req.size() > 0) {
            log.info("任务进度变更req data:{}", req.toString());
            req.forEach(userTaskChangeReqDTO -> {
                userTaskProgressService.userTaskChange(userTaskChangeReqDTO.getUserId(),
                        userTaskChangeReqDTO.getTaskType(), userTaskChangeReqDTO.getNumber(), userTaskChangeReqDTO.getMaxNumber());
            });
        }
        return Result.success();
    }

}

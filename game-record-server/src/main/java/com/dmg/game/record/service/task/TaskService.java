package com.dmg.game.record.service.task;

import com.dmg.common.core.web.Result;
import com.dmg.game.record.model.dto.UserTaskChangeReqDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:16 2019/11/26
 */
@FeignClient(value = "lobby-server", path = "/task/")
public interface TaskService {
    /**
     * @Author liubo
     * @Description //TODO 活动进度变更
     * @Date 12:43 2019/11/26
     **/
    @PostMapping("userTaskChange")
    Result userTaskChange(@RequestBody List<UserTaskChangeReqDTO> req);


}

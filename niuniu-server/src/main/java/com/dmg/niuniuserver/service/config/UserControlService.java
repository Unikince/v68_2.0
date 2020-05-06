package com.dmg.niuniuserver.service.config;

import com.dmg.common.core.web.Result;
import com.dmg.server.common.model.dto.UserControlInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:07 2020/2/25
 **/
@FeignClient(value = "game-config-server")
public interface UserControlService {

    /**
     * @Author liubo
     * @Description //TODO 查询用户控制模型
     * @Date 11:11 2020/2/25
     **/
    @PostMapping("/game-config-api/game/config/userControl/getModel")
    Map<Long, UserControlInfoDTO> getUserControlModel(@RequestBody List<Long> userIds);

}

package com.dmg.lobbyserver.service.feign;

import com.dmg.lobbyserver.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/29 16:25
 * @Version V1.0
 **/
@FeignClient(value = "DATA-SERVER")
public interface UserPositionService {

    /**
     * @description:
     * @param userId
     * @author mice
     * @date 2019/12/25
    */
    @GetMapping(value = "/getPosition")
    Result getPosition(@RequestParam("id") Integer userId);

}
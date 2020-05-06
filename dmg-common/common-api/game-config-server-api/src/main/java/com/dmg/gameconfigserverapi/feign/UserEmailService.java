package com.dmg.gameconfigserverapi.feign;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserverapi.dto.UserEmailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:09 2020/4/2
 */
@FeignClient(value = "GAME-CONFIG-SERVER", path = "/game-config-api")
public interface UserEmailService {

    /**
     * @Author liubo
     * @Description 发送转账邮件//TODO
     * @Date 10:10 2020/4/2
     **/
    @PostMapping("/config/email/saveUserEmail")
    Result saveUserEmail(@RequestBody UserEmailDTO userEmailDTO);

    /**
     * @Author liubo
     * @Description 撤销邮件//TODO
     * @Date 10:35 2020/4/2
     **/
    @GetMapping("/config/email/deleteUserEmail/{transferAccountId}")
    Result deleteUserEmail(@RequestParam("transferAccountId") Long id);
}

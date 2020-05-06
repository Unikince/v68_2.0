package com.dmg.lobbyserver.service.feign;

import com.dmg.lobbyserver.controller.pay.jqpay.FinancePropertiesVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description
 * @Author mice
 * @Date 2020/1/20 14:46
 * @Version V1.0
 **/
@FeignClient(value = "GAME-CONFIG-SERVER",path = "/game-config-api/FinancePropertiesApi")
public interface FinancePropertiesService {

    /** 获取 */
    @GetMapping("getFinanceProperties")
    FinancePropertiesVo getFinanceProperties();

}
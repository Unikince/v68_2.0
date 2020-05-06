package com.dmg.gameconfigserver.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.vo.finance.FinancePropertiesVo;
import com.dmg.gameconfigserver.service.financing.FinancePropertiesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author mice
 * @Date 2020/1/20 14:43
 * @Version V1.0
 **/
@RequestMapping(Constant.CONTEXT_PATH+"FinancePropertiesApi")
@RestController
@Slf4j
public class FinancePropertiesApiController {

    @Autowired
    private FinancePropertiesService service;

    /** 获取 */
    @GetMapping("/getFinanceProperties")
    @NoNeedLoginMapping
    public FinancePropertiesVo get() {
        FinancePropertiesVo vo = this.service.get();
        log.info("==>getFinanceProperties{}", JSONObject.toJSONString(vo));
        return vo;
    }
}
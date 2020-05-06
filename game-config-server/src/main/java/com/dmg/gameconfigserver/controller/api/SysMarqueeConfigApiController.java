package com.dmg.gameconfigserver.controller.api;

import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.service.sys.SysMarqueeConfigService;
import com.dmg.gameconfigserverapi.dto.SysMarqueeConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/25 10:41
 * @Version V1.0
 **/
@RequestMapping(Constant.CONTEXT_PATH + "sysMarqueeConfigApi")
@RestController
public class SysMarqueeConfigApiController {
    @Autowired
    private SysMarqueeConfigService sysMarqueeConfigService;

    @GetMapping("list/{marqueeType}")
    @NoNeedLoginMapping
    public List<SysMarqueeConfigDTO> getSysMarqueeConfigList(@PathVariable("marqueeType") Integer marqueeType){
        return sysMarqueeConfigService.getEnableExecuteSysMarqueeList(marqueeType);

    }
}
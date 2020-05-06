package com.dmg.gameconfigserver.controller.sys;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoAuthMapping;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.service.sys.SysResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author liubo
 * @Description //TODO 资源管理控制器
 * @Date 13:58 2019/11/6
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "sys/resource")
public class SysResourceController extends BaseController {

    @Autowired
    private SysResourceService sysResourceService;

    @GetMapping("/getAll")
    @NoAuthMapping
    public Result getAllList() {
        return Result.success(sysResourceService.getAllList());
    }


}

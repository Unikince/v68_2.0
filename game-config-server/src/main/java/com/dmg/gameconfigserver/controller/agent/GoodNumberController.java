package com.dmg.gameconfigserver.controller.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.vo.user.DistributeGoodNumVO;
import com.dmg.gameconfigserver.model.vo.user.GoodNumberPageVO;
import com.dmg.gameconfigserver.model.vo.user.UntieGoodNumVO;
import com.dmg.gameconfigserver.service.user.GoodNumberService;

/**
 * @author mice
 * @email .com
 * @date 2020-03-25 11:28:56
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "agent/goodnumber")
public class GoodNumberController {
    @Autowired
    private GoodNumberService goodNumberService;

    @PostMapping("getList")
    @SuppressWarnings("rawtypes")
    public Result getPage(@RequestBody GoodNumberPageVO vo) {
        return Result.success(this.goodNumberService.getPage(vo));
    }

    @PostMapping("distribute")
    @SuppressWarnings("rawtypes")
    public Result distribute(@RequestBody DistributeGoodNumVO vo) {
        this.goodNumberService.distribute(vo.getGoodNumber(), vo.getUserId(), vo.getOperatorId());
        return Result.success();
    }

    @PostMapping("untie")
    @SuppressWarnings("rawtypes")
    public Result untie(@RequestBody UntieGoodNumVO vo) {
        this.goodNumberService.untie(vo.getUserId(), vo.getOperatorId());
        return Result.success();
    }

}

package com.dmg.gameconfigserver.controller.financing;

import com.dmg.gameconfigserver.common.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.vo.finance.FinancePropertiesVo;
import com.dmg.gameconfigserver.service.financing.FinancePropertiesService;

/**
 * 财务属性
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "finance/config/properties")
public class FinancePropertiesController extends BaseController {
    @Autowired
    private FinancePropertiesService service;

    /** 修改 */
    @PostMapping("/updateOne")
    @SuppressWarnings("rawtypes")
    public Result updateOne(@RequestBody FinancePropertiesVo vo) {
        this.service.updateOne(vo);
        return Result.success();
    }

    /** 获取 */
    @GetMapping("/get")
    @SuppressWarnings("rawtypes")
    public Result get() {
        FinancePropertiesVo vo = this.service.get();
        return Result.success(vo);
    }
}

package com.dmg.gameconfigserver.controller.config;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import com.dmg.gameconfigserver.model.vo.config.zjh.ZjhRobotProbabilityConfigVO;
import com.dmg.gameconfigserver.model.bean.config.zjh.ZjhRobotProbabilityConfigBean;
import com.dmg.gameconfigserver.service.config.ZjhRobotProbabilityConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO 扎金花机器人基础概率配置控制器
 * @Date 17:21 2019/9/27
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "zjhRobotProbability")
public class ZjhRobotProbabilityController {

    @Autowired
    private ZjhRobotProbabilityConfigService zjhRobotProbabilityConfigService;

    @NoNeedLoginMapping
    @GetMapping("/getList")
    public Result getAllList() {
        List<ZjhRobotProbabilityConfigBean> zjhRobotProbabilityConfigBeanList = zjhRobotProbabilityConfigService.getAllList();
        return Result.success(zjhRobotProbabilityConfigBeanList);
    }

    @GetMapping("/getInfo/{id}")
    public Result getInfoById(@PathVariable("id") Integer id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        ZjhRobotProbabilityConfigBean zjhRobotProbabilityConfigBean =  zjhRobotProbabilityConfigService.getZjhRobotProbabilityById(id);
        return Result.success(zjhRobotProbabilityConfigBean);
    }

    @PostMapping("/saveOne")
    public Result saveOne(@RequestBody @Validated({SaveValid.class}) ZjhRobotProbabilityConfigVO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        ZjhRobotProbabilityConfigBean zjhRobotProbabilityConfigBean = new ZjhRobotProbabilityConfigBean();
        BeanUtils.copyProperties(vo, zjhRobotProbabilityConfigBean);
        zjhRobotProbabilityConfigService.insert(zjhRobotProbabilityConfigBean);
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result updateOne(@RequestBody @Validated({UpdateValid.class}) ZjhRobotProbabilityConfigVO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        zjhRobotProbabilityConfigService.update(vo);
        return Result.success();
    }

    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        zjhRobotProbabilityConfigService.deleteById(id);
        return Result.success();
    }
}

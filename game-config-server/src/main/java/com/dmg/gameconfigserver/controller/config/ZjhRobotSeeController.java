package com.dmg.gameconfigserver.controller.config;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import com.dmg.gameconfigserver.model.vo.config.zjh.ZjhRobotSeeConfigVO;
import com.dmg.gameconfigserver.model.bean.config.zjh.ZjhRobotSeeConfigBean;
import com.dmg.gameconfigserver.service.config.ZjhRobotSeeConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO 扎金花机器人是否看牌概率配置控制器
 * @Date 17:21 2019/9/27
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "zjhRobotSee")
public class ZjhRobotSeeController {

    @Autowired
    private ZjhRobotSeeConfigService zjhRobotSeeConfigService;

    @NoNeedLoginMapping
    @GetMapping("/getList")
    public Result getAllList() {
        List<ZjhRobotSeeConfigBean> zjhRobotSeeConfigBeanList = zjhRobotSeeConfigService.getAllList();
        return Result.success(zjhRobotSeeConfigBeanList);
    }

    @GetMapping("/getInfo/{id}")
    public Result getInfoById(@PathVariable("id") Integer id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        ZjhRobotSeeConfigBean zjhRobotSeeConfigBean = zjhRobotSeeConfigService.getZjhRobotSeeById(id);
        return Result.success(zjhRobotSeeConfigBean);
    }

    @NoNeedLoginMapping
    @GetMapping("/getRobotSeeByRound")
    public Result getRobotSeeByRound(@RequestParam("round") Integer round) {
        if (round == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "round cannot be empty");
        }
        ZjhRobotSeeConfigBean zjhRobotSeeConfigBean = zjhRobotSeeConfigService.getRobotSeeByRound(round);
        return Result.success(zjhRobotSeeConfigBean);
    }

    @PostMapping("/saveOne")
    public Result saveOne(@RequestBody @Validated({SaveValid.class}) ZjhRobotSeeConfigVO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        ZjhRobotSeeConfigBean zjhRobotSeeConfigBean = new ZjhRobotSeeConfigBean();
        BeanUtils.copyProperties(vo, zjhRobotSeeConfigBean);
        zjhRobotSeeConfigService.insert(zjhRobotSeeConfigBean);
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result updateOne(@RequestBody @Validated({UpdateValid.class}) ZjhRobotSeeConfigVO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        zjhRobotSeeConfigService.update(vo);
        return Result.success();
    }

    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        zjhRobotSeeConfigService.deleteById(id);
        return Result.success();
    }
}

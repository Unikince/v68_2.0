package com.dmg.gameconfigserver.controller.config;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.bean.config.niuniu.NiuniuRobotActionConfigBean;
import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import com.dmg.gameconfigserver.model.vo.config.niuniu.NiuniuRobotActionConfigVO;
import com.dmg.gameconfigserver.service.config.NiuniuRobotActionConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO 抢庄牛牛机器人动作配置控制器
 * @Date 17:21 2019/9/27
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "niuniuRobotAction")
public class NiuniuRobotActionController {

    @Autowired
    private NiuniuRobotActionConfigService niuniuRobotActionConfigService;

    @NoNeedLoginMapping
    @GetMapping("/getList")
    public Result getAllList() {
        List<NiuniuRobotActionConfigBean> niuniuRobotActionConfigBeanList = niuniuRobotActionConfigService.getAllList();
        return Result.success(niuniuRobotActionConfigBeanList);
    }

    @GetMapping("/getInfo/{id}")
    public Result getInfoById(@PathVariable("id") Integer id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        NiuniuRobotActionConfigBean niuniuRobotActionConfigBean = niuniuRobotActionConfigService.getNiuniuRobotActionById(id);
        return Result.success(niuniuRobotActionConfigBean);
    }

    @NoNeedLoginMapping
    @GetMapping("/getRobInfoByCard")
    public Result getRobInfoByCard(@RequestParam("card") Integer card) {
        if (card == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "card cannot be empty");
        }
        List<NiuniuRobotActionConfigBean> niuniuRobotActionConfigBeanList = niuniuRobotActionConfigService.getRobInfoByCard(card);
        return Result.success(niuniuRobotActionConfigBeanList);
    }

    @NoNeedLoginMapping
    @GetMapping("/getPressureInfoByRob")
    public Result getPressureInfoByRob(@RequestParam("card") Integer card, @RequestParam("robType") Integer robType) {
        if (card == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "card cannot be empty");
        }
        if (robType == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "robType cannot be empty");
        }
        List<NiuniuRobotActionConfigBean> niuniuRobotActionConfigBeanList = niuniuRobotActionConfigService.getPressureInfoByRob(card, robType);
        return Result.success(niuniuRobotActionConfigBeanList);
    }

    @PostMapping("/saveOne")
    public Result saveOne(@RequestBody @Validated({SaveValid.class}) NiuniuRobotActionConfigVO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        NiuniuRobotActionConfigBean niuniuRobotActionConfigBean = new NiuniuRobotActionConfigBean();
        BeanUtils.copyProperties(vo, niuniuRobotActionConfigBean);
        niuniuRobotActionConfigService.insert(niuniuRobotActionConfigBean);
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result updateOne(@RequestBody @Validated({UpdateValid.class}) NiuniuRobotActionConfigVO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        niuniuRobotActionConfigService.update(vo);
        return Result.success();
    }

    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        niuniuRobotActionConfigService.deleteById(id);
        return Result.success();
    }
}

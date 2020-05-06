package com.dmg.gameconfigserver.controller.config;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import com.dmg.gameconfigserver.model.vo.config.zjh.ZjhRobotActionConfigVO;
import com.dmg.gameconfigserver.model.bean.config.zjh.ZjhRobotActionConfigBean;
import com.dmg.gameconfigserver.service.config.ZjhRobotActionConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO 扎金花机器人动作概率配置控制器
 * @Date 17:21 2019/9/27
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "zjhRobotAction")
public class ZjhRobotActionController {

    @Autowired
    private ZjhRobotActionConfigService zjhRobotActionConfigService;

    @NoNeedLoginMapping
    @GetMapping("/getList")
    public Result getAllList() {
        List<ZjhRobotActionConfigBean> zjhRobotActionConfigBeanList = zjhRobotActionConfigService.getAllList();
        return Result.success(zjhRobotActionConfigBeanList);
    }

    @GetMapping("/getInfo/{id}")
    public Result getInfoById(@PathVariable("id") Integer id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        ZjhRobotActionConfigBean zjhRobotActionConfigBean = zjhRobotActionConfigService.getZjhRobotActionById(id);
        return Result.success(zjhRobotActionConfigBean);
    }

    @NoNeedLoginMapping
    @GetMapping("/getRobotActionByIsSee")
    public Result getRobotActionByIsSee(@RequestParam("isSee") Boolean isSee) {
        if (isSee == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "isSee cannot be empty");
        }
        ZjhRobotActionConfigBean zjhRobotActionConfigBean = zjhRobotActionConfigService.getRobotActionByIsSee(isSee);
        return Result.success(zjhRobotActionConfigBean);
    }

    @NoNeedLoginMapping
    @GetMapping("/getRobotActionByCard")
    public Result getRobotActionByCard(@RequestParam("cardType") Integer cardType, @RequestParam("card") Integer card) {
        if (cardType == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "cardType cannot be empty");
        }
        if (card == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "card cannot be empty");
        }
        ZjhRobotActionConfigBean zjhRobotActionConfigBean = zjhRobotActionConfigService.getRobotActionByCard(cardType, card);
        return Result.success(zjhRobotActionConfigBean);
    }

    @PostMapping("/saveOne")
    public Result saveOne(@RequestBody @Validated({SaveValid.class}) ZjhRobotActionConfigVO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        ZjhRobotActionConfigBean zjhRobotActionConfigBean = new ZjhRobotActionConfigBean();
        BeanUtils.copyProperties(vo, zjhRobotActionConfigBean);
        zjhRobotActionConfigService.insert(zjhRobotActionConfigBean);
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result updateOne(@RequestBody @Validated({UpdateValid.class}) ZjhRobotActionConfigVO vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        zjhRobotActionConfigService.update(vo);
        return Result.success();
    }

    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        zjhRobotActionConfigService.deleteById(id);
        return Result.success();
    }
}

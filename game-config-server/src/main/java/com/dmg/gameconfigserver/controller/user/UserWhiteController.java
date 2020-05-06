package com.dmg.gameconfigserver.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.dto.user.UserWhiteDTO;
import com.dmg.gameconfigserver.model.vo.user.UserWhiteVO;
import com.dmg.gameconfigserver.service.user.UserWhiteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:17 2019/11/20
 */
@Slf4j
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "member/userWhite")
public class UserWhiteController extends BaseController {

    @Autowired
    private UserWhiteService userWhiteService;

    @PostMapping("/getList")
    public Result getAllList(@RequestBody @Validated UserWhiteDTO userWhiteDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        IPage<UserWhiteVO> userWhiteList = userWhiteService.getUserWhitePage(userWhiteDTO);
        return Result.success(userWhiteList);
    }

    @PostMapping("/saveOne")
    public Result saveOne(@RequestBody Map<String, String> data) {
        if (data == null || StringUtils.isEmpty(data.get("userIds"))) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "userIds is not null");
        }
        return Result.success(userWhiteService.insert(data.get("userIds").split(","), getUserId()));
    }

    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        userWhiteService.deleteById(id);
        return Result.success();
    }

}

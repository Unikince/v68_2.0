package com.dmg.gameconfigserver.controller.config.email;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.bean.config.email.EmailBean;
import com.dmg.gameconfigserver.model.bean.user.UserEmailBean;
import com.dmg.gameconfigserver.model.dto.config.email.EmailDTO;
import com.dmg.gameconfigserver.model.dto.config.email.EmailPageDTO;
import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import com.dmg.gameconfigserver.service.config.EmailService;
import com.dmg.gameconfigserverapi.dto.UserEmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 19:15 2020/1/7
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "config/email")
public class EmailController extends BaseController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/getList")
    public Result getList(@RequestBody EmailPageDTO pageReqDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        return Result.success(emailService.getEmailList(pageReqDTO));
    }

    @PostMapping("/saveOne")
    public Result save(@RequestBody @Validated({SaveValid.class}) EmailDTO emailDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        emailService.insert(emailDTO, getUserId());
        return Result.success();
    }

    @NoNeedLoginMapping
    @PostMapping("/saveUserEmail")
    public Result saveUserEmail(@RequestBody UserEmailDTO userEmailDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        emailService.insertUserEmail(userEmailDTO);
        return Result.success();
    }

    @NoNeedLoginMapping
    @GetMapping("/deleteUserEmail/{transferAccountId}")
    public Result deleteUserEmail(@PathVariable("transferAccountId") Long id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        UserEmailBean userEmailBean = emailService.getUserEmailByTransferAccountId(id);
        if (userEmailBean == null) {
            return Result.error(ResultEnum.EMAIL_NOT_EXIT.getCode().toString(), ResultEnum.EMAIL_NOT_EXIT.getMsg());
        }
        if (userEmailBean.getReceive()) {
            return Result.error(ResultEnum.EMAIL_RECEIVE.getCode().toString(), ResultEnum.EMAIL_RECEIVE.getMsg());
        }
        emailService.deleteUserEmailById(id);
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result update(@RequestBody @Validated({UpdateValid.class}) EmailDTO emailDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        EmailBean emailBean = emailService.getEmailById(emailDTO.getId());
        if (emailBean == null) {
            return Result.error(ResultEnum.EMAIL_NOT_EXIT.getCode().toString(), ResultEnum.EMAIL_NOT_EXIT.getMsg());
        }
        if (emailBean.getSendDate().getTime() < new Date().getTime()) {
            return Result.error(ResultEnum.EMAIL_SEND.getCode().toString(), ResultEnum.EMAIL_SEND.getMsg());
        }
        Boolean result = emailService.update(emailDTO, getUserId());
        if (!result) {
            return Result.error(ResultEnum.EMAIL_NOT_EXIT.getCode().toString(), ResultEnum.EMAIL_NOT_EXIT.getMsg());
        }
        return Result.success();
    }

    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        EmailBean emailBean = emailService.getEmailById(id);
        if (emailBean == null) {
            return Result.error(ResultEnum.EMAIL_SEND.getCode().toString(), ResultEnum.EMAIL_SEND.getMsg());
        }
        if (emailBean.getSendDate().getTime() < new Date().getTime()
                && emailBean.getExpireDate().getTime() > new Date().getTime()) {
            return Result.error(ResultEnum.EMAIL_SEND.getCode().toString(), ResultEnum.EMAIL_SEND.getMsg());
        }
        emailService.deleteById(id);
        return Result.success();
    }
}

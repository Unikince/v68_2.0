package com.dmg.lobbyserver.controller.platformlogin;

import com.dmg.lobbyserver.common.util.HttpServletUtil;
import com.dmg.lobbyserver.exception.BusinessException;
import com.dmg.lobbyserver.model.dto.HttpLoginDTO;
import com.dmg.lobbyserver.model.vo.JQLoginVO;
import com.dmg.lobbyserver.result.Result;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.PlatformLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/23 16:02
 * @Version V1.0
 **/
@RestController
@RequestMapping("platform")
public class PlatformLoginController {
    @Autowired
    private PlatformLoginService platformLoginService;

    /**
     * @description: 登录
     * @param vo
     * @return com.dmg.lobbyserver.result.Result
     * @author mice
     * @date 2019/12/23
     */
    @PostMapping("/jqLogin")
    public Result jqLogin(HttpServletRequest servletRequest, @RequestBody JQLoginVO vo) {
        HttpLoginDTO httpLoginDTO = platformLoginService.jqLogin(vo.getDeviceCode(),vo, HttpServletUtil.getIpAddr(servletRequest));
        return Result.success(httpLoginDTO);
    }
}
package com.dmg.lobbyserver.controller;

import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.dto.SyncUserInfoDTO;
import com.dmg.lobbyserver.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/1 18:53
 * @Version V1.0
 **/
@RestController
public class SyncUserInfoController {
    @Autowired
    private UserService userService;

    @RequestMapping("syncUserInfo")
    public SyncUserInfoDTO syncUserInfo(Long userId){
        UserBean userBean = userService.getUserById(userId);
        if (userBean!=null){
            SyncUserInfoDTO userDTO = new SyncUserInfoDTO();
            BeanUtils.copyProperties(userBean,userDTO);
            return userDTO;
        }
        return null;
    }
}
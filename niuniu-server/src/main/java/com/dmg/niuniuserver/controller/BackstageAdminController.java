package com.dmg.niuniuserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 后台管理Controller
 * @Author: Lemon
 * @Date: 2019/9/5 10:24
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/backstage", method = RequestMethod.POST)
public class BackstageAdminController {

    @RequestMapping("/setMaintenanceTime")
    public void setMaintenanceTime() {

    }

}

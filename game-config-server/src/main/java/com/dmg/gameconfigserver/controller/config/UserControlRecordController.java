package com.dmg.gameconfigserver.controller.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.bean.config.UserControlRecordBean;
import com.dmg.gameconfigserver.model.vo.config.UserControlRecordVO;
import com.dmg.gameconfigserver.service.config.UserControlRecordService;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 10:14
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/userControlRecord")
public class UserControlRecordController {

    @Autowired
    private UserControlRecordService userControlRecordService;
    
    @PostMapping("/getUserControlRecordList")
    public Result getUserControlRecordList(@RequestBody UserControlRecordVO vo) {
    	IPage<UserControlRecordBean> userControlRecordList = userControlRecordService.getUserControlRecordList(vo);
        return Result.success(userControlRecordList);
    }
}
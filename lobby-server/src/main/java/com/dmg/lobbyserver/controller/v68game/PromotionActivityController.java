package com.dmg.lobbyserver.controller.v68game;

import com.dmg.lobbyserver.dao.bean.SysPromotionCodeBean;
import com.dmg.lobbyserver.dao.bean.TaskConfigBean;
import com.dmg.lobbyserver.result.Result;
import com.dmg.lobbyserver.service.PromotionActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description  充值用户选择优惠
 * @Author jock
 * @Date 13:50
 * @Version V1.0
 **/
@RestController
public class PromotionActivityController {
    @Autowired
    PromotionActivityService promotionActivityService ;

    /**
     * 优惠活动
     * @param userId
     * @return
     */
    @PostMapping("/getUserActivity")
    public Result getUserActivity(@RequestParam("userId") long userId){
        List<TaskConfigBean> userActivity = promotionActivityService.getUserActivity(userId);
       return Result.success(userActivity);
    }
    @PostMapping("/getUserActivityCode")
    public Result getUserActivityCode(@RequestParam("userId") long userId){
        List<SysPromotionCodeBean> userActivityCode = promotionActivityService.getUserActivityCode(userId);
        return  Result.success(userActivityCode);

    }
}

package com.dmg.lobbyserver.service;

import com.dmg.lobbyserver.dao.bean.SysPromotionCodeBean;
import com.dmg.lobbyserver.dao.bean.TaskConfigBean;

import java.util.List;

/**
 * @Description    充值选择活动
 * @Author jock
 * @Date 13:51
 * @Version V1.0
 **/
public interface PromotionActivityService {
    /**
     * 用户优惠活动(充值入口)
     * @param userId
     * @return
     */
    List<TaskConfigBean> getUserActivity(Long userId);
    /**
     * 用户优惠代码显示
     */
    List<SysPromotionCodeBean> getUserActivityCode(Long userId);
}

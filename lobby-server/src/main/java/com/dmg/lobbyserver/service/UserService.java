package com.dmg.lobbyserver.service;

import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.dto.LeaderboardDTO;
import com.dmg.lobbyserver.model.dto.UserDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/24 15:44
 * @Version V1.0
 **/
public interface UserService {

    /**
     * @param userId
     * @return com.dmg.lobbyserver.dao.bean.UserBean
     * @description: 通过userId 获取userBean
     * @author mice
     * @date 2019/6/24
     */
    UserBean getUserById(Long userId);

    /**
     * @description: 保存userBean
     * @description: 通过userCode 获取userBean
     * @param userCode
     * @return com.dmg.lobbyserver.dao.bean.UserBean
     * @author mice
     * @date 2019/6/24
     */
    UserBean getUserByUserCode(Long userCode);

    /**
     * @description: 保存userBean
     * @param userBean
     * @return com.dmg.lobbyserver.dao.bean.UserBean
     * @description: 保存userBean
     * @author mice
     * @date 2019/6/24
     */
    UserBean insert(UserBean userBean);

    /**
     * @param userBean
     * @return com.dmg.lobbyserver.dao.bean.UserBean
     * @description: 通过userId更新userBean
     * @author mice
     * @date 2019/6/24
     */
    UserBean updateUserById(UserBean userBean);

    /**
     * @Author liubo
     * @Description //TODO 变更余额
     * @Date 11:20 2019/11/27
     **/
    UserBean changeAccountBalance(Long userId, BigDecimal changeAccount);

    /**
     * @Author liubo
     * @Description //TODO 变更积分
     * @Date 11:48 2019/11/27
     **/
    UserBean changeIntegral(Long userId, Long integral);

    /**
     * @Author liubo
     * @Description //TODO 变更活跃度
     * @Date 11:48 2019/11/27
     **/
    void changeActivityLevel(Long userId, Double activityLevel);

    UserBean getUserByDeviceCode(String deviceCode);

    /**
     * @description: 金币排行榜
     * @param limit
     * @return java.util.List<com.dmg.lobbyserver.model.dto.LeaderboardDTO>
     * @author mice
     * @date 2019/12/24
    */
    List<LeaderboardDTO> getLeaderboard(int limit);
}
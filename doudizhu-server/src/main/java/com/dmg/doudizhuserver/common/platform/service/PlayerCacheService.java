package com.dmg.doudizhuserver.common.platform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.dmg.doudizhuserver.common.platform.model.Player;
import com.dmg.doudizhuserver.common.platform.model.Robot;
import com.dmg.doudizhuserver.common.platform.model.RobotBean;

/**
 * 玩家信息
 */
@Service
public class PlayerCacheService {
    private static final Logger LOG = LoggerFactory.getLogger(PlayerCacheService.class);
    @Autowired
    private NamedParameterJdbcTemplate gameDataJdbcTpl;
    /** 机器人实体类 */
    private static final BeanPropertyRowMapper<RobotBean> ROBOT_BEAN_MAPPER = new BeanPropertyRowMapper<>(RobotBean.class);

    @Cacheable(cacheNames = "ddz_player", key = "#playerId", unless = "#result == null")
    public Player getPlayer(long playerId) {
        return null;
    }

    /**
     * 更新玩家信息
     *
     * @param Player 玩家信息
     * @return 玩家信息
     */
    @CachePut(cacheNames = "ddz_player", key = "#player.id")
    public Player updatePlayer(Player player) {
        return player;
    }

    /**
     * 获取机器人信息
     *
     * @param playerId 玩家id
     * @return 机器人信息
     */
    @Cacheable(cacheNames = "ddz_robot", key = "#playerId", unless = "#result == null")
    public Robot getRobot(long playerId) {
        try {
            playerId = playerId - 10000000;
            String sql = "select * from robot where `user_id`=:userId";
            RobotBean robotBean = this.gameDataJdbcTpl.queryForObject(sql, new MapSqlParameterSource("userId", playerId), ROBOT_BEAN_MAPPER);
            Robot robot = new Robot();
            robot.setId(robotBean.getUserId() + 10000000);
            robot.setNickname(robotBean.getNickname());
            robot.setHeadImg(robotBean.getHeadImg());
            robot.setSex(robotBean.getSex());
            robot.setRobot(true);
            return robot;
        } catch (EmptyResultDataAccessException e) {
            LOG.error("selectRobotByUserId userId={} error={}", playerId, e.toString());
            return null;
        }
    }

    /**
     * 更新玩家信息
     *
     * @param Robot 玩家信息
     * @return 玩家信息
     */
    @CachePut(cacheNames = "ddz_robot", key = "#robot.id")
    public Robot updateRobot(Robot robot) {
        return robot;
    }
}
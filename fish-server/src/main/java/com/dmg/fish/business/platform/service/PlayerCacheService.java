package com.dmg.fish.business.platform.service;

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

import com.dmg.fish.business.platform.model.Player;
import com.dmg.fish.business.platform.model.Robot;
import com.dmg.fish.business.platform.model.RobotBean;

/**
 * 玩家缓存信息
 */
@Service
public class PlayerCacheService {
    public static long ROBOT_ID_PREFIX = 900000000;
    private static final Logger LOG = LoggerFactory.getLogger(PlayerCacheService.class);
    @Autowired
    private NamedParameterJdbcTemplate gameDataJdbcTpl;
    /** 机器人实体类 */
    private static final BeanPropertyRowMapper<RobotBean> ROBOT_BEAN_MAPPER = new BeanPropertyRowMapper<>(RobotBean.class);

    /**
     * 获取玩家信息
     *
     * @param playerId 玩家id
     * @return 玩家信息
     */
    @Cacheable(cacheNames = "fish:player", key = "#playerId", unless = "#result == null")
    public Player getPlayer(long playerId) {
        return null;
    }

    /**
     * 更新玩家信息
     *
     * @param Player 玩家信息
     * @return 玩家信息
     */
    @CachePut(cacheNames = "fish:player", key = "#player.id")
    public Player update(Player player) {
        return player;
    }

    /**
     * 获取机器人信息
     *
     * @param playerId 玩家id
     * @return 机器人信息
     */
    public Robot getRobot(long playerId) {
        try {
            long id = playerId - ROBOT_ID_PREFIX;
            String sql = "select * from robot where `id`=:id";
            RobotBean robotBean = this.gameDataJdbcTpl.queryForObject(sql, new MapSqlParameterSource("id", id), ROBOT_BEAN_MAPPER);
            Robot robot = new Robot();
            robot.setId(robotBean.getId() + ROBOT_ID_PREFIX);
            robot.setNickname(robotBean.getNickname());
            robot.setHeadImg(robotBean.getHeadImg());
            robot.setSex(robotBean.getSex());
            robot.setGold(robot.getGold() + (int) (Math.random() * 450000));
            robot.setRobot(true);
            return robot;
        } catch (EmptyResultDataAccessException e) {
            LOG.error("selectRobotByUserId userId={} error={}", playerId, e.toString());
            return null;
        }
    }
}
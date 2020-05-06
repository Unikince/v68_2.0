package com.dmg.bjlserver.business.robot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.dmg.bjlserver.business.platform.model.Player;
import com.dmg.bjlserver.business.platform.model.RobotBean;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/8/27 10:28
 * @Version V1.0
 **/
@Component
@Slf4j
public class RobotInit {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    /** 机器人实体类 */
    private RowMapper<RobotBean> ROBOT_BEAN_MAPPER = new BeanPropertyRowMapper<>(RobotBean.class);
    @PostConstruct
    private void init() {
        log.info("正在加载机器人数据...");
        String sql = "select * from robot";
        List<RobotBean> robotBeans = this.jdbcTemplate.query(sql, this.ROBOT_BEAN_MAPPER);
        for (RobotBean robotBean : robotBeans) {
            Player player = new Player();
            player.setId(robotBean.getId());
            player.setSex(robotBean.getSex());
            player.setHeadImg(robotBean.getHeadImg());
            player.setRobot(true);
            player.setGold(0);
            player.setNickName(robotBean.getNickname());
            RobotMgr.getInstance().getAllRobots().put(player.getId(),player);
        }
    }
}
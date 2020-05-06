package com.dmg.fish.business.robot;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.config.dic.FishRoomDic;
import com.dmg.fish.business.config.wrapper.FishRoomWrapper;
import com.dmg.fish.business.platform.model.Robot;
import com.dmg.fish.business.platform.model.RobotBean;
import com.dmg.fish.business.platform.service.PlayerService;

import cn.hutool.core.util.RandomUtil;

/**
 * 机器人调度类
 */
@Component
@ConditionalOnClass(FishRoomDic.class)
public class FishRobotScheduler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    /** 所有机器人 */
    private static Map<Long, FishRobot> ALL_ROBOTS = new ConcurrentHashMap<>();
    /** 机器人实体类 */
    private RowMapper<RobotBean> ROBOT_BEAN_MAPPER = new BeanPropertyRowMapper<>(RobotBean.class);
    // key:房间id
    private ConcurrentHashMap<Integer, ConcurrentHashMap<Long, FishRobot>> robots = new ConcurrentHashMap<>();
    private ScheduledExecutorService scheduledExecutor;
    @Autowired
    private FishRoomDic fishRoomDic;
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @PostConstruct
    private void init() {
        long now = System.currentTimeMillis();
        long total = 0;
        this.log.info("正在加载机器人数据...");
        String sql = "select * from robot";
        List<RobotBean> robotBeans = this.jdbcTemplate.query(sql, this.ROBOT_BEAN_MAPPER);
        for (RobotBean robotBean : robotBeans) {
            Robot robot = new Robot();
            robot.setId(robotBean.getId() + PlayerService.ROBOT_ID_PREFIX);
            robot.setNickname(robotBean.getNickname());
            robot.setHeadImg(robotBean.getHeadImg());
            robot.setSex(robotBean.getSex());
            robot.setRobot(true);
            FishRobotScheduler.ALL_ROBOTS.put(robot.getId(), new FishRobot(robot));
            total++;
        }
        this.log.info("加载机器人[{}],耗时[{}]毫秒", total, System.currentTimeMillis() - now);
        this.startSchedule();
    }

    /** 开始调度 */
    public void startSchedule() {
        List<FishRoomWrapper> roomBeans = this.fishRoomDic.values();
        // 调度线程数等于房间数量 + 1
        this.scheduledExecutor = Executors.newScheduledThreadPool(roomBeans.size() + 1, (r) -> new Thread(r, "RobotScheduledExecutor"));
        for (FishRoomWrapper fishRoomBean : roomBeans) {
            // 每隔多少秒驱动一次房间机器人
            this.scheduledExecutor.scheduleWithFixedDelay(() -> {
                try {
                    this.scheduleRoomRobots(fishRoomBean);
                } catch (Exception e) {
                    this.log.error("机器人加载错误", e);
                }
            }, 30, fishRoomBean.getRobotScheduleRate(), TimeUnit.SECONDS);
        }
        // 每隔多少分钟清理一次房间中被标记为LOCK的机器人
        this.scheduledExecutor.scheduleWithFixedDelay(() -> {
            try {
                this.releaseLockRobots();
            } catch (Exception e) {
                this.log.error("机器人释放错误", e);
            }
        }, 1, 3, TimeUnit.MINUTES);
    }

    /** 调度房间机器人 */
    private void scheduleRoomRobots(FishRoomWrapper fishRoomBean) {
        int roomId = fishRoomBean.getId();
        Long pointLower = fishRoomBean.getRobotEnterPointLower();
        Long pointUpper = fishRoomBean.getRobotEnterPointUpper();
        ConcurrentHashMap<Long, FishRobot> roomRobots = this.getRobots(roomId);
        // 期望获取的机器人数量
        int expectRobots = fishRoomBean.getRobotNum() - this.getRobotsValid(roomId);
        if (expectRobots > fishRoomBean.getRobotScheduleNum()) {
            expectRobots = fishRoomBean.getRobotScheduleNum();
        }
        if (expectRobots > 0) {
            int num = 0;
            for (FishRobot fishRobot : ALL_ROBOTS.values()) {
                if (fishRobot.use != null) {
                    continue;
                }
                roomRobots.put(fishRobot.data.getId(), fishRobot);
                fishRobot.data.setGold(RandomUtil.randomLong(pointLower, pointUpper + 1));
                fishRobot.roomId = roomId;
                fishRobot.schedule();
                if (++num >= expectRobots) {
                    break;
                }
            }
        }
    }

    /**
     * 获取房间机器人
     */
    private ConcurrentHashMap<Long, FishRobot> getRobots(int roomId) {
        ConcurrentHashMap<Long, FishRobot> robots = this.robots.get(roomId);
        if (robots == null) {
            robots = new ConcurrentHashMap<>();
            ConcurrentHashMap<Long, FishRobot> robotsTmp = this.robots.putIfAbsent(roomId, robots);
            if (robotsTmp != null) {
                robots = robotsTmp;
            }
        }

        return robots;
    }

    /**
     * 释放机器人
     */
    public void releaseLockRobots() {
        List<FishRoomWrapper> roomBeans = this.fishRoomDic.values();
        for (FishRoomWrapper fishRoomBean : roomBeans) {
            ConcurrentHashMap<Long, FishRobot> robots = this.getRobots(fishRoomBean.getId());
            for (Iterator<Map.Entry<Long, FishRobot>> iterator = robots.entrySet().iterator(); iterator.hasNext();) {
                FishRobot robot = iterator.next().getValue();
                if (robot.use != null && robot.use == false) {
                    robot.clear();
                    iterator.remove();
                }
            }
        }

    }

    /**
     * 获取房间有效机器人数
     *
     * @param roomId
     * @return
     */
    private int getRobotsValid(int roomId) {
        ConcurrentHashMap<Long, FishRobot> robots = this.robots.get(roomId);
        int num = 0;
        if (robots == null) {
            return num;
        }
        for (FishRobot robot : robots.values()) {
            if (robot.use != null && robot.use == true) {
                num++;
            }
        }
        return num;
    }

    /**
     * 获取正在使用的指定id机器人
     */
    public FishRobot getUseRobot(long robotId) {
        FishRobot robot = ALL_ROBOTS.get(robotId);
        if (robot == null) {
            return null;
        }
        if (robot.use == null || robot.use == false) {
            return null;
        }
        return robot;
    }
}

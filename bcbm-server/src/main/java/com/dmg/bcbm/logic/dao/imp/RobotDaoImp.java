package com.dmg.bcbm.logic.dao.imp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bcbm.core.annotation.Dao;
import com.dmg.bcbm.core.pool.DBPool;
import com.dmg.bcbm.logic.dao.RobotBean;
import com.dmg.bcbm.logic.dao.RobotDao;
@Dao
public class RobotDaoImp implements RobotDao {
	 private static Logger logger = LoggerFactory.getLogger(RobotDaoImp.class);
	@Override
	public RobotBean queryRobot(int userId) {
        Connection conn = null;
        RobotBean robot = null;
        try {
            conn = DBPool.instance().getConnection();
            String sql = "select * from robot where user_id = ?";
            PreparedStatement state = conn.prepareStatement(sql);
            state.setInt(1, userId);
            ResultSet set = state.executeQuery();
            while (set.next()) {
            	robot = new RobotBean();
            	robot.setUserId(userId);
            	robot.setNickname(set.getString("nickname"));
            }
        } catch (Exception e) {
            logger.error("查找机器人失败失败", e);
        } finally {
            DBPool.instance().closeConnection(conn);
        }
        return robot;
	}

}

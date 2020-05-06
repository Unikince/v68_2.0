package com.dmg.bcbm.core.pool;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariDataSource;

/**
 * @Date: 2015年9月30日 下午3:48:30
 * @Author: zhuqd
 * @Description:
 */
@Component("lobbyDBPool")
public class DBPool {
	private static Logger logger = LoggerFactory.getLogger(DBPool.class);
	private static DBPool pool;
	private HikariDataSource ds = null;

	@Autowired
	private DBConfig config;

	private DBPool() {
		pool = this;
	}

	/**
	 * get instance
	 * 
	 * @return
	 */
	public static DBPool instance() {
		return pool;
	}

	/**
	 * @throws PropertyVetoException
	 * 
	 */
	public void init(JdbcTemplate jdbcTemplate) throws PropertyVetoException {
		ds = (HikariDataSource)jdbcTemplate.getDataSource();
		logger.info("[DBPOOL]INITIALIZED");
	}

	/**
	 * 获取连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			logger.error("dbpool error.{}", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 关闭
	 */
	public void close() {
		ds.close();
	}

	/**
	 * 关闭连接
	 * 
	 * @param conn
	 */
	public void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error("close connenction error ", e);
			}
		}
	}

	@EventListener(EnvironmentChangeEvent.class)
	public void onApplicationEvent(EnvironmentChangeEvent event) {
		for (String key : event.getKeys()) {
			if (key.startsWith("db.")) {
				break;
			}
		}
	}

}

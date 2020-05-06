package com.zyhy.lhj_server.dao.imp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_server.model.GameLhjLog;
import com.zyhy.lhj_server.dao.ShowRecordDao;
import com.zyhy.lhj_server.game.ShowRecordResult;
@Service
public class ShowRecordDaoImp implements ShowRecordDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<GameLhjLog> showRecord(String tableTime, String roleid, String uuid, String game, Long startTime, Long endTime) {
		StringBuilder sql = new StringBuilder("SELECT fujia FROM `lhj_game_log_");
		sql.append(tableTime).append("`")
		.append(" WHERE roleid='").append(roleid).append("'")
		.append(" AND uuid='").append(uuid).append("'")
		.append(" AND gamename='").append(game).append("'")
		.append(" AND time>='").append(startTime).append("'")
		.append(" AND time<='").append(endTime).append("'");
		String slqstr = sql.toString();
		System.out.println("SQL :" + slqstr);
		List<GameLhjLog> list = jdbcTemplate.query(slqstr, new RowMapper<GameLhjLog>() {
			@Override
			public GameLhjLog mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				GameLhjLog log = new GameLhjLog();
				//log.setId(rs.getInt("id"));
				//log.setLogtype(rs.getString("logtype"));
				//log.setGamename(rs.getString("gamename"));
				//log.setRoleid(rs.getString("roleid"));
				//log.setTime(rs.getLong("time"));
				log.setFujia(rs.getString("fujia"));
				return log;
			}
		});
		/*if(list.size() > 0){
			for (GameLhjLog gameLhjLog : list) {
				//String fujia = gameLhjLog.getFujia();
				//ShowRecordResult result = JSONObject.parseObject(gameLhjLog.getFujia(), ShowRecordResult.class);
				System.out.println(gameLhjLog);
			} 
			return list;
		}*/
		return list;
	}

}

package com.zyhy.lhj_server.dao;

import java.util.List;

import com.zyhy.common_server.model.GameLhjLog;

public interface ShowRecordDao {
	// 查询玩家战绩
	List<GameLhjLog> showRecord(String tableTime, String roleid, String uuid, String game, Long startTime, Long endTime);
}

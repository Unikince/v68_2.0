package com.zyhy.lhj_server.game;

import java.util.List;

import com.zyhy.common_server.model.GameLhjLog;

public class ShowRecord {
	// 战绩记录
	private List<List<GameLhjLog>> queryResults;
	

	public ShowRecord() {
	}

	public ShowRecord(List<List<GameLhjLog>> queryResults) {
		this.queryResults = queryResults;
	}

	public List<List<GameLhjLog>> getQueryResults() {
		return queryResults;
	}

	public void setQueryResults(List<List<GameLhjLog>> queryResults) {
		this.queryResults = queryResults;
	}

	@Override
	public String toString() {
		return "ShowRecord [queryResults=" + queryResults + "]";
	}
	
}

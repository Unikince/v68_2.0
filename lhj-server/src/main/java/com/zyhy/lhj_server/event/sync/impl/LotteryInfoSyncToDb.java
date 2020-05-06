package com.zyhy.lhj_server.event.sync.impl;

import org.springframework.stereotype.Service;

import com.zyhy.lhj_server.event.queue.Event;
import com.zyhy.lhj_server.event.sync.SyncUpdateData;

/**
 * 
 * @author linanjun
 * 异步更新到数据库
 */
@Service
public class LotteryInfoSyncToDb implements SyncUpdateData{

	@Override
	public void updateData(Event qm) throws Exception {
		
	}

}

package com.dmg.clubserver.event.sync.impl;

import com.dmg.clubserver.event.queue.Event;
import com.dmg.clubserver.event.sync.SyncUpdateData;
import org.springframework.stereotype.Service;

/**
 * 
 * @author linanjun
 * 异步更新到数据库
 */
@Service
public class LotteryInfoSyncToDb implements SyncUpdateData {

	@Override
	public void updateData(Event qm) throws Exception {
		
	}

}

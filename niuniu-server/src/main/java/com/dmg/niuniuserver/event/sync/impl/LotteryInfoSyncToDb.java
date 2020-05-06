package com.dmg.niuniuserver.event.sync.impl;

import com.dmg.niuniuserver.event.sync.SyncUpdateData;
import com.dmg.niuniuserver.event.queue.Event;
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

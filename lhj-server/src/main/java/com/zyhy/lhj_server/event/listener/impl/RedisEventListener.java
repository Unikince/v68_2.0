/**   
* @Title: RedisEventListener.java
* @Package com.longma.ssss.event.listener
* @Description:
* @author nanjun.li   
* @date 2017-1-9 下午1:27:14
* @version V1.0   
*/

package com.zyhy.lhj_server.event.listener.impl;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyhy.lhj_server.event.listener.EventListener;
import com.zyhy.lhj_server.event.queue.Event;
import com.zyhy.lhj_server.event.queue.impl.QueueModel;
import com.zyhy.lhj_server.event.sync.SyncUpdateData;
import com.zyhy.lhj_server.event.sync.impl.LotteryInfoSyncToDb;

/**
 * @ClassName: RedisEventListener
 * @Description: redis事件监听器
 * @author nanjun.li
 * @date 2017-1-9 下午1:27:14
 */
@Service
public class RedisEventListener implements EventListener {

	@Autowired
	private LotteryInfoSyncToDb lotteryInfoSyncToDb;

	@Override
	public void take(Event event) {
		if (event instanceof QueueModel) {
			QueueModel qm = (QueueModel) event;
			process(qm);
		}
	}

	/**
	 * 
	 * @Title: process 
	 * @Description: 执行存入 
	 * @param type 
	 * @param qm 
	 * @return void 返回类型
	 * @throws
	 */
	protected void process(QueueModel qm) {
		if (qm == null || qm.getTable() == null) {
			return;
		}
		SyncUpdateData std = null;
		switch (qm.getTable()) {
		case t_user_info:
			std = lotteryInfoSyncToDb;
			break;
		default:
			break;
		}
		try {
			std.updateData(qm);
		} catch (SQLException sql) {
			sql.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int orderLevel() {
		return 1;
	}

}

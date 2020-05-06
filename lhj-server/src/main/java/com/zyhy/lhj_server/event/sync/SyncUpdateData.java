/**
 * 
 */
package com.zyhy.lhj_server.event.sync;

import com.zyhy.lhj_server.event.queue.Event;

/**
 * 异步入库接口
 * @author nanjun.li
 */
public interface SyncUpdateData {

	void updateData(Event qm) throws Exception;
}

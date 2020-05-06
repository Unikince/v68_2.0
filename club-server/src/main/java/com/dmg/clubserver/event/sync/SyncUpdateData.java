/**
 * 
 */
package com.dmg.clubserver.event.sync;


import com.dmg.clubserver.event.queue.Event;

/**
 * 异步入库接口
 * @author nanjun.li
 */
public interface SyncUpdateData {

	void updateData(Event qm) throws Exception;
}

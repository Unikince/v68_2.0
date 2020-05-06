/**
 * 
 */
package com.dmg.niuniuserver.event.sync;


import com.dmg.niuniuserver.event.queue.Event;

/**
 * 异步入库接口
 * @author nanjun.li
 */
public interface SyncUpdateData {

	void updateData(Event qm) throws Exception;
}

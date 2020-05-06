package com.dmg.zhajinhuaserver.dao;

import com.dmg.zhajinhuaserver.model.bean.Record;
import com.dmg.zhajinhuaserver.model.bean.RoomRecord;

import java.util.List;

/**
 * @author zhuqd
 * @Date 2017年9月5日
 * @Desc
 */
public interface RecordDao {

	/**
	 * 添加游戏记录
	 * 
	 * @param list
	 */
	void insert(List<Record> list);

	/**
	 * 记录房间参与信息
	 * 
	 * @param roomRecordList
	 */
	void insertRoomRecord(List<RoomRecord> roomRecordList);

}

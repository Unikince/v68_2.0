/**   
* @Title: QueueModel.java
* @Package com.longma.ssss.dbsync
* @Description:
* @author nanjun.li   
* @date 2017-1-22 上午11:06:55
* @version V1.0   
*/


package com.dmg.clubserver.event.queue.impl;

import com.dmg.clubserver.event.queue.Event;
import com.zyhy.common_server.constants.ConstantsEnum;
import com.zyhy.common_server.constants.TablesEnum;

/**
 * @ClassName: QueueModel
 * @Description: 入库事件
 * @author nanjun.li
 * @date 2017-1-22 上午11:06:55
 */

public class QueueModel implements Event {

	private ConstantsEnum type = ConstantsEnum.DATA_CREATE;
	private TablesEnum table = TablesEnum.t_user_info;
	private Object data;
	
	/**
	 * 
	 * @param type 类型 0=更新,1=创建
	 */
	public QueueModel(ConstantsEnum type, TablesEnum table, Object data){
		this.type = type;
		this.table = table;
		this.data = data;
	}

	public ConstantsEnum getType() {
		return type;
	}

	public void setType(ConstantsEnum type) {
		this.type = type;
	}

	public TablesEnum getTable() {
		return table;
	}

	public void setTable(TablesEnum table) {
		this.table = table;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueueModel other = (QueueModel) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (table != other.table)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QueueModel [type=" + type + ", table=" + table + ", data="
				+ data + "]";
	}

}

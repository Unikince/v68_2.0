/**
 * 
 */
package com.zyhy.common_server.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_server.result.MessageClient;

/**
 * @author lnj
 * 解析游戏参数
 */
public class JsonMesssageUtil {
	
	/**
	 * 从客户端接收json格式的消息，转化成ClientMesage对象
	 * @param c
	 * @return
	 */
	public static MessageClient genClientMessage(String c) {
		MessageClient m = new MessageClient();
		JSONObject jsonArray = JSONObject.parseObject(c);
		Object messageid = jsonArray.get("messageid");
		Object uuid = jsonArray.get("uuid");
		Object sid = jsonArray.get("sid");
		Map<String, String> map = toMap(jsonArray);

		m.setMessageid(messageid == null ? 0 : Integer.parseInt(messageid.toString()));
		m.setUuid(uuid == null ? "" : uuid.toString());
		m.setSid(sid == null ? "" : sid.toString());
		m.setParams(map);
		return m;
	}

	/**
	 * 将json对象转换成Map
	 * @param jsonObject
	 * @return
	 */
	private static Map<String, String> toMap(JSONObject jsonObject) {
		Map<String, String> result = new HashMap<String, String>();
		if (jsonObject == null) {
			return result;
		}
		Iterator<String> iterator = jsonObject.keySet().iterator();
		String key = null;
		String value = null;
		while (iterator.hasNext()) {
			key = iterator.next();
			value = jsonObject.getString(key);
			if ("map".equals(key)) {
				JSONObject jsonArray = JSONObject.parseObject(value);
				Iterator<String> ite = jsonArray.keySet().iterator();
				while (ite.hasNext()) {
					String zkey = ite.next();
					String zvalue = jsonArray.getString(zkey);
					result.put(zkey, zvalue);
				}
			}else {
				result.put(key, value);
			}
		}
		return result;
	}
}

/**
 *  注意：本内容仅限于大拇哥互娱科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
package com.dmg.clubserver.tcp.manager.impl;

import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @className:--WebSocketManagerImpl
 * @author:-----Vito
 * @date:-------2019年3月19日 下午5:56:06
 * @version:----1.0
 * @Description:实现连接管路
 */
@Service
public class WebSocketManagerImpl implements LocationManager {

	@Override
	public List<MyWebSocket> getWebSocketAll() {
		List<MyWebSocket> list = new ArrayList<MyWebSocket>(userMap.values().size());
		list.addAll(userMap.values());
		return list;
	}

	public MyWebSocket getWebSocket(Integer appid){
		return userMap.get(appid+"");
	}

	@Override
	public boolean addUserMap(String appId, MyWebSocket mws) {
		try {
			MyWebSocket zqmws = userMap.get(appId);
			if(zqmws != null) {
				return false;
			} else {
				userMap.put(appId, mws);
				return true;
			}
		} catch (Exception e) {
			mws.onClose();
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public void close(String appId) {
		userMap.remove(appId);
	}
	
	@Override
	public int getPushSize() {
		return userMap.size();
	}
}

/**
 *  注意：本内容仅限于大拇哥互娱科技有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
package com.dmg.redblackwarserver.tcp.manager;


import com.dmg.redblackwarserver.tcp.server.MyWebSocket;

import javax.websocket.Session;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @className:--LocationManager
 * @author:-----Vito
 * @date:-------2019年3月19日 下午5:33:57
 * @version:----1.0
 * @Description:定位
 */
public interface LocationManager {
	
	// 1所有用户的连接保存
	public static ConcurrentHashMap<String, MyWebSocket> userMap = new ConcurrentHashMap<>();

	/**
	 * 获取当前服务器所有连接
	 * 
	 * @return 列表
	 */
	public List<MyWebSocket> getWebSocketAll();

	/**
	 * 获取链接
	 *
	 * @return
	 */
	public MyWebSocket getWebSocket(int appid);

	/**
	 * 添加连接
	 * 
	 * @param appId APPID
	 * @param mws   连接
	 * @return 是否添加成功
	 */
	public boolean addUserMap(String appId, MyWebSocket mws);

	/**
	 * 获取当前服务器中的连接数量
	 * 
	 * @return 数量
	 */
	public int getPushSize();

	/**
	 * 关闭链接
	 * @param appId
	 */
	void close(String appId, Session session);

}

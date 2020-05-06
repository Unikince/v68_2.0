package com.dmg.clubserver.tcp.manager;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;

/**
 * 操作类
 * 
 * @author Administrator
 *
 */
@Component
public class TcpManager {

	private static TcpManager tcpManager;
	@Autowired
	private RedisManager redisManager;

	// <连接者uid,channle> 所有连接通道缓存
	private ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		tcpManager = this;
		tcpManager.redisManager = this.redisManager;
	}

	public static TcpManager getInst() {
		return tcpManager;
	}

	public RedisManager getManager() {
		return redisManager;
	}

	public void setManager(RedisManager redisManager) {
		this.redisManager = redisManager;
	}

	public ConcurrentHashMap<String, Channel> getChannelMap() {
		return channelMap;
	}

	public void addChannelMap(int machineGroup, String uid, Channel channel) {
		channelMap.put(uid, channel);
		redisManager.addUserChannel(machineGroup, uid);
	}

	/**
	 * 请求连接
	 * 
	 * @param channel
	 * @param uid
	 */
	public void connect(Channel channel, JSONObject msg) {
		addChannelMap(msg.getIntValue("machineGroup"), msg.getString("uid"), channel);
		channel.writeAndFlush(msg.getString("uid") + " : connect success");
	}

	/**
	 * 根据主组，子组发送消息
	 * 
	 * @param machineGroup
	 * @param childGroup
	 * @param msg
	 */
	public void pushMsg(int machineGroup, int childGroup, Object msg) {
		Set<String> set = redisManager.getByGroupId(machineGroup, childGroup);
		// 全体发送
		if (set == null) {
			channelMap.forEach((key, value) -> {
				value.writeAndFlush(msg);
			});
		} else {// 部分发送
			set.forEach(value -> {
				Channel channel = channelMap.get(value);
				if (channel != null) {
					channel.writeAndFlush(msg);
				} else {
					redisManager.deleteUserChannel(machineGroup, value);
				}
			});
		}
	}

	/**
	 * 单独推送某一玩家
	 * 
	 * @param machineGroup
	 * @param uid
	 * @param object
	 */
	public void pushSingle(String uid, Object msg) {
		Channel channel = channelMap.get(uid);
		if (channel != null) {
			channel.writeAndFlush(msg);
		}
	}

}

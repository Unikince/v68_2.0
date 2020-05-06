package com.dmg.bcbm.core.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bcbm.core.abs.message.MessageNet;
import com.dmg.bcbm.core.annotation.Net;
import com.dmg.bcbm.logic.def.DefType;
import com.dmg.bcbm.logic.def.ServerDef;

import util.ClassUtils;

/**
 * @Date: 2015年10月7日 下午2:20:40
 * @Author: zhuqd
 * @Description:
 */
public class NetManager {
	private static NetManager instance = new NetManager();
	private final Map<String, Class<MessageNet>> netMap = new HashMap<>();
	private volatile boolean close = false;

	private static final Logger LOG = LoggerFactory.getLogger(NetManager.class);

	private NetManager() {

	}

	public static NetManager instance() {
		return instance;
	}

	/**
	 * 初始化
	 * 
	 * @param path
	 * 
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("unchecked")
	public void init() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		ServerDef def = DefFactory.instance().getDef(DefType.SERVER);
		Collection<Class<?>> classes = ClassUtils.searchByAnnotation(def.getClassPath(), Net.class);
		for (Class<?> clazz : classes) {
			Net net = clazz.getAnnotation(Net.class);
			String cmd = net.value();
			if (cmd != null) {
				netMap.put(cmd, (Class<MessageNet>) clazz);
			}
			LOG.info("[NET]INIT[{}]", clazz.getName());
		}
	}

	/**
	 * 
	 * @param cmd
	 * @return
	 */
	public Class<MessageNet> get(String cmd) {
		if (close) {
			return null;
		}
		return netMap.get(cmd);
	}

	/**
	 * 设置关闭状态，拒绝玩家请求
	 */
	public void close() {
		close = true;
//		System.out.println("net manager closed ....");
		LOG.info("[NET]CLOSED");
	}
	//

	public Map<String, Class<MessageNet>> getNetMap() {
		return netMap;
	}

}

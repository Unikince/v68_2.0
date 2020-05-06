package com.dmg.bcbm.core.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bcbm.core.annotation.Http;
import com.dmg.bcbm.logic.def.DefType;
import com.dmg.bcbm.logic.def.ServerDef;

import util.ClassUtils;
import util.StringHelper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @param <T>
 * @Date: 2015年11月19日 下午5:16:51
 * @Author: zhuqd
 * @Description:
 */
public class HttpManager {

	private static HttpManager instance = new HttpManager();
	private final Map<String, Object> netMap = new HashMap<>();

	private static final Logger LOG = LoggerFactory.getLogger(HttpManager.class);

	private HttpManager() {

	}

	public static HttpManager instance() {
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
	public void init() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		ServerDef def = DefFactory.instance().getDef(DefType.SERVER);
		Collection<Class<?>> classes = ClassUtils.searchByAnnotation(def.getClassPath(), Http.class);
		for (Class<?> clazz : classes) {
			Http net = clazz.getAnnotation(Http.class);
			String path = net.value();
			if (!StringHelper.isEmpty(path)) {
				netMap.put(path, clazz.newInstance());
			}
			LOG.info("[HTTP]INIT[{}]", clazz.getName());
		}
	}

	/**
	 * 获取http处理器
	 * 
	 * @param path
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getHttpNet(String path) {
		return (T) netMap.get(path);
	}
}

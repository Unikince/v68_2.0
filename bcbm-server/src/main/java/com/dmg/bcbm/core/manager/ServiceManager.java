package com.dmg.bcbm.core.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bcbm.core.annotation.Service;
import com.dmg.bcbm.logic.def.DefType;
import com.dmg.bcbm.logic.def.ServerDef;

import util.ClassUtils;

/**
 * @Date: 2015年9月30日 下午3:32:35
 * @Author: zhuqd
 * @Description:
 */
public class ServiceManager {
	private static ServiceManager instance = new ServiceManager();
	private final Map<Class<?>, Object> serviceMap = new HashMap<>();

	private static final Logger LOG = LoggerFactory.getLogger(ServiceManager.class);

	private ServiceManager() {
	}

	/**
	 * get instance
	 * 
	 * @return
	 */
	public static ServiceManager instance() {
		return instance;
	}

	/**
	 * 初始化
	 * 
	 *
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void init() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		ServerDef def = DefFactory.instance().getDef(DefType.SERVER);
		Collection<Class<?>> classes = ClassUtils.searchByAnnotation(def.getClassPath(), Service.class);
		for (Class<?> clazz : classes) {
			Class<?>[] superClass = clazz.getInterfaces();
			if (superClass != null && superClass.length > 0) {
				serviceMap.put(superClass[0], clazz.newInstance());
			} else {
				serviceMap.put(clazz, clazz.newInstance());
			}
			LOG.info("[SERVICE]INIT[{}]", clazz.getName());
		}
	}

	/**
	 * 获取连接
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Class<?> clazz) {
		return (T) serviceMap.get(clazz);
	}

	public Map<Class<?>, Object> getServiceMap() {
		return serviceMap;
	}

}

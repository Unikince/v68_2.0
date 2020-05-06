package com.dmg.bcbm.core.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bcbm.core.annotation.Dao;
import com.dmg.bcbm.logic.def.DefType;
import com.dmg.bcbm.logic.def.ServerDef;

import util.ClassUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Date: 2015年9月30日 下午3:41:56
 * @Author: zhuqd
 * @Description:
 */
public class DaoManager {
	private static DaoManager instance = new DaoManager();
	private final Map<Class<?>, Object> daoMap = new HashMap<>();

	private static final Logger LOG = LoggerFactory.getLogger(DaoManager.class);

	private DaoManager() {

	}

	/**
	 * get instance
	 * 
	 * @return
	 */
	public static DaoManager instance() {
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
		Collection<Class<?>> classes = ClassUtils.searchByAnnotation(def.getClassPath(), Dao.class);
		for (Class<?> clazz : classes) {
			Class<?> superClass = clazz.getInterfaces()[0];
			if (superClass != null) {
				daoMap.put(superClass, clazz.newInstance());
			}
			LOG.info("[DAO]LOAD[{}]", clazz.getName());
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
		return (T) daoMap.get(clazz);
	}

	// setter and getter
	public Map<Class<?>, Object> getDaoMap() {
		return daoMap;
	}

}

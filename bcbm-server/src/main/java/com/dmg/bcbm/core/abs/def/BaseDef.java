package com.dmg.bcbm.core.abs.def;

/**
 * @param <V>基本类型，Long,Integer,String..
 * @param <IDefType>IDefType实现类
 * @Date: 2015年10月8日 上午10:54:19
 * @Author: zhuqd
 * @Description:
 */
public abstract class BaseDef<V, IDefType> {

	/**
	 * 获取配置id
	 * 
	 * @param <V>
	 * @return
	 */
	public abstract V id();

	/**
	 * 获取配置类型
	 * 
	 * @param <T>
	 * 
	 * @return
	 */
	public abstract IDefType type();
}

package com.dmg.bcbm.core.abs.def;

/**
 * @param <IDefType>
 *            配置类型
 * @Date: 2015年10月8日 下午2:46:19
 * @Author: zhuqd
 * @Description:服务器中只有一份的配置，id默认为1
 */
public abstract class UniqueDef<IDefType> extends BaseDef<Integer, IDefType> {

	@Override
	public Integer id() {
		return 1;
	}

}

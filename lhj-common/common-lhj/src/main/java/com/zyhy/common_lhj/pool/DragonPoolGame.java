package com.zyhy.common_lhj.pool;

import java.util.List;

import com.zyhy.common_lhj.WeightUtils;

public class DragonPoolGame {

	/**
	 * 得到
	 * @param os
	 * @return
	 */
	public static DragonItem random(List<DragonItem> os){
		DragonItem w = WeightUtils.random(os);
		return w;
	}
}

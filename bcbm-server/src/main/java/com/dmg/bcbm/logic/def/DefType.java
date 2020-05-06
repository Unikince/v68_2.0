package com.dmg.bcbm.logic.def;

import com.dmg.bcbm.core.abs.def.type.IDefType;

/**
 * @author zhuqd
 * @Date 2017年7月24日
 * @Desc 配置类型
 */
public enum DefType implements IDefType {
	SERVER, // 服务器配置
	WECHAT, // 微信授权相关配置
	BaoMahjiong, // 包麻将
	BaoCreateRoomCost, // 包麻将创建房间花费
	LoginAward, // 签到奖励
	PayList, // 充值列表
	Province, // 省份定义
}

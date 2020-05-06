package com.zyhy.common_server.type;


/**
 * 
* @ClassName: LogType
* @Description: 所有玩家行为日志的描述字段都在这里定义
* @author nanjun.li
* @date 2017-1-5 上午11:27:48
*
 */
public enum LogType {
	
	REGISTER("注册","user_log_"), // moid-uuid-名字-钻石-游戏币 user
	LOGIN("登录","user_log_"), // moid-uuid-名字-钻石-游戏币 user
	EXIT_LOGIN("退出","user_log_"),//退出
	
	OAUTH_LOGIN("登录授权","game_log_"), // moid-uuid-手机号&code youxi
	OAUTH_REFRESH("刷新授权","game_log_"), // moid-uuid-手机号&code youxi
	UPDATE_SET_INFO("更新设置信息","game_log_"), // moid-uuid-类型(1=音乐,2=音效)&值 youxi
	UPDATE_USER_PHONE_INFO("修改用户绑定手机号","game_log_"), //moid-uuid-手机号&code
	UPDATE_USER_NAME_INFO("修改用户昵称","game_log_"),//moid-uuid-昵称
	UPDATE_USER_AVATARURL_INFO("修改用户头像","game_log_)"),
	
	SAFEBOX_CREATE("创建保险箱","game_log_"), 
	SAFEBOX_UPDATE_PWD("修改保险箱密码","game_log_"), 
	SAFEBOX_UPDATE_INGOT("更新保险箱游戏币","game_log_"), 
	
	USERFEEDBACK_CREATE("创建用户反馈","game_log_"),
	USERFEEDBACK_UPDATE("修改用户邮件","game_log_"),
	USERFEEDBACK_DELETE("删除用户反馈","game_log_"),
	
	USERORDER_UPDATE("修改抽奖订单收货信息","game_log_"),
	
	COIN_CHOUJIANG("游戏币抽奖","game_log_"),
	
	LOGIN_FKDWC("登录FKDWC","user_log_"), // moid-uuid-名字-钻石-游戏币 user
	LOGIN_FKHZ("登录FKHZ","user_log_"), // moid-uuid-名字-钻石-游戏币 user
	LOGIN_FKSGJ("登录FKSGJ","user_log_"), // moid-uuid-名字-钻石-游戏币 user
	LOGIN_KXTTL("登录KXTTL","user_log_"), // moid-uuid-名字-钻石-游戏币 user
	LOGIN_FKLMJ("登录FKLMJ","user_log_"), // moid-uuid-名字-钻石-游戏币 user
	LOGIN_FKGGL("登录FKGGL","user_log_"), // moid-uuid-名字-钻石-游戏币 user
	LOGIN_FKDNN("登录FKDNN","user_log_"), // moid-uuid-名字-钻石-游戏币 user
	LOGIN_FKKD("登录FKKD", "user_log_"), // moid-uuid-名字-钻石-游戏币 user
	LOGIN_FKPK("登录FKPK", "user_log_"), // moid-uuid-名字-钻石-游戏币 user
	LOGIN_FKJL("登录FKJL","user_log_"),// moid-uuid-名字-钻石-游戏币 user
	LOGIN_FKDQ("登录FKDQ", "user_log_"), // moid-uuid-名字-钻石-游戏币 user
	LOGIN_GGHZ("登录GGHZ","user_log_"), // moid-uuid-名字-钻石-游戏币 user
	LOGIN_NNYY("登录NNYY","user_log_"),
	LOGIN_ZCTZ("登录ZCTZ","user_log_"),
	LOGIN_GSGL("登录GSGL","user_log_"),
	LOGIN_CZDBZ("登录CZDBZ","user_log_"),
	
	EXIT_FKDWC("退出FKDWC","user_log_"), // moid-uuid-名字-钻石-游戏币
	EXIT_FKHZ("退出FKHZ","user_log_"), // moid-uuid-名字-钻石-游戏币
	EXIT_FKSGJ("退出FKSGJ","user_log_"), // moid-uuid-名字-钻石-游戏币
	EXIT_KXTTL("退出KXTTL","user_log_"), // moid-uuid-名字-钻石-游戏币
	EXIT_FKLMJ("退出FKLMJ","user_log_"), // moid-uuid-名字-钻石-游戏币
	EXIT_FKGGL("退出FKGGL","user_log_"), // moid-uuid-名字-钻石-游戏币
	EXIT_FKDNN("退出FKDNN","user_log_"), // moid-uuid-名字-钻石-游戏币
	EXIT_FKKD("退出FKKD","user_log_"), // moid-uuid-名字-钻石-游戏币
	EXIT_FKPK("退出FKPK","user_log_"),// moid-uuid-名字-钻石-游戏币
	EXIT_FKJL("退出FKJL","user_log_"),// moid-uuid-名字-钻石-游戏币
	
	GAME_FKDWC("开始FKDWC","game_log_"), // moid-uuid-下注类型&下注游戏币&返回信息 youxi
	GAME_FKSGJ("开始FKSGJ","game_log_"), // moid-uuid-下注信息&返回信息 youxi
	GAME_FKHZ("开始FKHZ","game_log_"), // moid-uuid-下注档位&下注级别&返回信息 youxi
	GAME_KXTTL("开始KXTTL","game_log_"), // moid-uuid-下注号码&下注游戏币&返回信息 youxi
	GAME_FKLMJ("开始FKLMJ","game_log_"), // moid-uuid-下注信息&返回信息 youxi
	GAME_FKGGL("开始FKGGL","game_log_"),
	GAME_FKDNN("开始FKDNN","game_log_"), // moid-uuid-下注信息&返回信息 youxi
	GAME_FKKD("开始FKKD","game_log_"), // moid-uuid-下注信息&返回信息 youxi
	GAME_FKPK("开始FKPK","game_log_"), // moid-uuid-下注信息&返回信息 youxi
	GAME_FKJL("开始FKJL","game_log_"),// moid-uuid-下注信息&返回信息 youxi
	GAME_GGHZ("开始GGHZ","game_log_"), // moid-uuid-下注类型&下注游戏币&返回信息 youxi
	GAME_NNYY("开始NNYY","game_log_"),
	GAME_ZCTZ("开始ZCTZ","game_log_"),
	GAME_GSGL("开始GSGL","game_log_"),
	GAME_CZDBZ("开始CZDBZ","game_log_"),
	
	QUESS_QMSGJ("比大小QMSGJ","game_log_"), // moid-uuid-猜大小 0=小,1=大&用户游戏币&下注游戏币&返回信息 youxi
	QUESS_FKSGJ("比大小FKSGJ","game_log_"), // moid-uuid-猜大小 0=小,1=大&用户游戏币&下注游戏币&返回信息 youxi
	BONUS_FKDWC("小游戏FKDWC","game_log_"), // moid-uuid-剩余次数&返回信息 youxi
	FKKD_FIRST("FKKD第一次翻牌","game_log"), //moid-uuid-返回信息
	FKKD_SECEND("FKKD第二次翻牌","game_log"), //moid-uuid-返回信息
	FKKD_THIRDLY("FKKD第三次翻牌","game_log"), //moid-uuid-返回信息
	FKPK_DUIPAI("FKPK发对牌","game_log"), //moid-uuid-返回信息
	FKLMJ_XIAOYOUXI("FKLMJ小游戏","game_log"),   // moid-uuid-返回信息
	FKJL_FIRST("FKJL第一次翻牌","game_log_"), // moid-uuid-返回信息
	FKJL_SECEND("FKJL第二次翻牌","game_log_"),// moid-uuid-返回信息
	
	//签到
	QIANDAO_USER("签到","game_log_"),
	
	;
	private LogType(String value,String table){
		this.value = value;
		this.table = table;
	}
	private String value;
	private String table;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
}

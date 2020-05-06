package com.zyhy.common_server.constants;


public class Common {
	public static final String USER_CHANNEL_GROUP = "user_channel_group";
	public static final String BRIDGING = "-";
	
	/**
	 * 客户端机器组
	 * @author Administrator
	 *
	 */
	public enum MachineGroup{
		NONE,
		ANDROID,
		APPLE;
		public int getValue() {
			return this.ordinal();
		}
		public static MachineGroup forValue(int value) {
			return values()[value];
		}
	}
	/**
	 * 具体子组
	 * @author Administrator
	 *
	 */
	public enum ChildGroup{
		NONE,
		SPORT,
		DDZ,
		ZHJ,
		BJL,
		NN,
		MJ,
		LHJ;
		public int getValue() {
			return this.ordinal();
		}
		public static ChildGroup forValue(int value) {
			return values()[value];
		}
	}
	/**
	 * 登陆类型
	 * @author Administrator
	 *
	 */
	public enum LoginType{
		ACCOUNTID,
		PHONE,
		ACCOUNT,
		CODE,
		TOURIST;
		public int getValue() {
			return this.ordinal(); 
		}
		public static LoginType forValue(int value) {
			return values()[value];
		}
	}
}

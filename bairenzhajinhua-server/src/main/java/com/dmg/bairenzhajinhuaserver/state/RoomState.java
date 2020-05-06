package com.dmg.bairenzhajinhuaserver.state;
/**
 * 房间阶段状态
 * @author Administrator
 *
 */
public interface RoomState {
	public enum State {
		START,
		BET,
		DEAL,
		SETTLE;
		public int getValue() {
			return this.ordinal();
		}

		public static State forValue(int value) {
			return values()[value];
		}
	}
	
	public State getState();
	
	public void action();
	
}
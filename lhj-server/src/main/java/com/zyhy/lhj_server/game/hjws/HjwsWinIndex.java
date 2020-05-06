package com.zyhy.lhj_server.game.hjws;

import java.util.List;

import com.zyhy.common_lhj.WindowInfo;

/**
 * @author DPC
 * @version 创建时间：2019年2月25日 下午2:38:51
 * @ClassName 类名称
 */
public class HjwsWinIndex {
		//获奖位置
		protected List<List<WindowInfo>> windows;
		//奖励
		protected double reward;	
		//奖励类型 1奖金翻倍 2免费次数
		protected int rewardType;
		//吸血蝙蝠
		protected int bat;
		//奖励数量
		protected int num;
		//是否出现1+5特殊奖励
		protected boolean isSpecial;
		// 特殊奖励金额
		protected double specialRward;
		public List<List<WindowInfo>> getWindows() {
			return windows;
		}
		public void setWindows(List<List<WindowInfo>> windows) {
			this.windows = windows;
		}
		public double getReward() {
			return reward;
		}
		public void setReward(double reward) {
			this.reward = reward;
		}
		public int getRewardType() {
			return rewardType;
		}
		public void setRewardType(int rewardType) {
			this.rewardType = rewardType;
		}
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
		public int getBat() {
			return bat;
		}
		public void setBat(int bat) {
			this.bat = bat;
		}
		public boolean isSpecial() {
			return isSpecial;
		}
		public void setSpecial(boolean isSpecial) {
			this.isSpecial = isSpecial;
		}
		
		public double getSpecialRward() {
			return specialRward;
		}
		public void setSpecialRward(double specialRward) {
			this.specialRward = specialRward;
		}
		@Override
		public String toString() {
			return "HjwsWinIndex [windows=" + windows + ", reward=" + reward + ", rewardType=" + rewardType + ", bat="
					+ bat + ", num=" + num + ", isSpecial=" + isSpecial + ", specialRward=" + specialRward + "]";
		}
		
		
}

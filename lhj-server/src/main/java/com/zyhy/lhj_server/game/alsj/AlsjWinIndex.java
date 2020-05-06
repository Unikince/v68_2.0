package com.zyhy.lhj_server.game.alsj;

import java.util.ArrayList;
import java.util.List;

import com.zyhy.common_lhj.WindowInfo;

/**
 * @author DPC
 * @version 创建时间：2019年2月25日 下午2:38:51
 * @ClassName 类名称
 */
public class AlsjWinIndex {
		//获奖位置
		protected List<List<WindowInfo>> windows;
		//奖励
		protected double reward;	
		//奖励类型 1奖金翻倍 2免费次数
		protected int rewardType;
		//奖励数量
		protected int num;
		//下一轮是否出现全部Wild
		protected boolean isAllWild;
		//是否触发红利游戏
		protected boolean isRedGame;
		//wild出现的列
		protected List<Integer> wild = new ArrayList<Integer>();
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
		public boolean isAllWild() {
			return isAllWild;
		}
		public void setAllWild(boolean isAllWild) {
			this.isAllWild = isAllWild;
		}
		public List<Integer> getWild() {
			return wild;
		}
		public void setWild(List<Integer> wild) {
			this.wild = wild;
		}
		@Override
		public String toString() {
			return "AlsjWinIndex [windows=" + windows + ", reward=" + reward
					+ ", rewardType=" + rewardType + ", num=" + num
					+ ", isAllWild=" + isAllWild + ", wild=" + wild + "]";
		}
		public boolean isRedGame() {
			return isRedGame;
		}
		public void setRedGame(boolean isRedGame) {
			this.isRedGame = isRedGame;
		}
}

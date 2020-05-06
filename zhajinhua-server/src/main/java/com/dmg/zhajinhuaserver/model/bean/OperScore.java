
package com.dmg.zhajinhuaserver.model.bean;
import java.util.HashMap;
import java.util.Map;


/**
 * 单次操作获得的分数
 * 
 * @author Administrator
 *
 */
public class OperScore implements Cloneable{
	public int chairID; 		// 赢家位置
	public long score; 			// 获得的分数
	public int fan; 			// 番数

	@SuppressWarnings("rawtypes")
	public Map toMap() {
		Map<String, Object> ret = new HashMap<>();
		return ret;
	}

	public OperScore() {

	}
	@Override
	public OperScore clone() {
		OperScore os = null;
		try {
			os = (OperScore) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return os;
	}

	public OperScore(int chairID, long score, int fan, int scoreType) {
		super();
		this.chairID = chairID;		
		this.score = score;
		this.fan = fan;
		
	}

	@Override
	public String toString() {
		return " 得分玩家椅子ID:" + chairID + "  得分:" + score + "  番数" + fan;
				
	}
	
//	public String toString(ChairMgr chairMgr) {
//		Chair chair = chairMgr.getChairBy(chairID);
//		Chair beChair = null;
//		return " 得分玩家为:" + chair.playerVO.getpNickName() + " 出牌玩家为:" + (beChair == null ? "自己":beChair.playerVO.getpNickName()) + "  得分:" + score + "  番数" + fan + "  得分类型:"
//				+ scoreType.name();
//	}


}
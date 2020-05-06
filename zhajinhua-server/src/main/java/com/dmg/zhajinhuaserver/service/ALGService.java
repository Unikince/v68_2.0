package com.dmg.zhajinhuaserver.service;

import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Poker;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.model.bean.Segment;

import java.util.LinkedList;
import java.util.List;

/**
 * @author hexf
 * @Date 2018年3月13日
 * @Desc 双Q算法
 */
public interface ALGService {

	/**
	 * 生成整副牌
	 * 
	 * @return
	 */
	LinkedList<Poker> createDeck();

	/**
	 * 计算玩家提示牌
	 * 
	 * @param hand
	 * @param lastPlayCard
	 * @param hasJoker
	 * @return
	 */
	List<Segment> evalTipSegment(List<Poker> hand, Segment lastPlayCard, boolean hasJoker);

	/**
	 * 生成测试整副牌
	 * 
	 * @return
	 */
	LinkedList<Poker> createTestDeck();

	/**
	 * 比较两副牌的大小
	 * 
	 * @param segment1
	 * @param segment2
	 * @return
	 */
	boolean isBigThan(Segment segment1, Segment segment2);

	/**
	 * 计算牌型
	 * 
	 * @param list
	 * @return
	 */
	int evalPokerType(List<Poker> list);

	
	/**
	 * 判断当前玩家操作
	 * @param seat
	 * @param room
	 */
	void haveBaseOper(Seat seat, GameRoom room);
	
	/**
	 * 判断比牌牌型大小
	 * @param room
	 * @param cardList
	 * @param becardList
	 * @return
	 */
	 
	boolean operOneWin(GameRoom room, List<Poker> cardList, List<Poker> becardList);

}

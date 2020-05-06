package com.dmg.doudizhuserver.business.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dmg.doudizhuserver.business.ai.RobotAI;
import com.dmg.doudizhuserver.business.ai.RobotAI.Entity;
import com.dmg.doudizhuserver.business.model.Card;
import com.dmg.doudizhuserver.business.model.CardsType;

public class CardsTypeFinderType {
	
	public static void main(String[] args) {
		List<Card> cards = new ArrayList<Card>();
		cards.add(Card.HEI_TAO_J);
		cards.add(Card.MEI_HUA_J);
		cards.add(Card.HONG_TAO_J);
		cards.add(Card.FANG_KUAI_J);
//		cards.add(Card.HONG_TAO_Q);
//		cards.add(Card.FANG_KUAI_Q);
//		cards.add(Card.HEI_TAO_Q);
//		cards.add(Card.HONG_TAO_K);
//		cards.add(Card.FANG_KUAI_K);
//		cards.add(Card.HEI_TAO_K);
//		cards.add(Card.HEI_TAO_SAN);
//		cards.add(Card.HEI_TAO_SI);
		cards.add(Card.HEI_TAO_WU);
		cards.add(Card.HONG_TAO_SAN);
		List<List<Card>> out = CardsTypeFinderType.findTypeCards(cards, CardsType.SI_DAI_DAN);
		System.out.println(out);
		Entity entity = RobotAI.optimumSolution(cards);
		System.out.println(entity);
	}
	/** 查找指定牌型 */
	public static List<List<Card>> findTypeCards(Collection<Card> fromCards, CardsType cardType) {
		List<List<Card>> list = new ArrayList<>();
		List<GroupCard> groups = groupByCardNum(fromCards);
		switch (cardType) {
		case WANG_ZHA: {
			// 判断牌里面是否有王炸
			boolean dw = false;
			boolean xw = false;
			List<Card> tmp = new ArrayList<>();
			for (Card card : fromCards) {
				if (card.num == Card.DW_NUM) {
					dw = true;
					tmp.add(card);
				}
				if (card.num == Card.XW_NUM) {
					xw = true;
					tmp.add(card);
				}
			}
			if (dw && xw) {
				list.add(tmp);
			}
		}
			break;
		case ZHA_DAN: {
			// 判断牌里面所有的炸弹
			for (GroupCard group : groups) {
				if (group.getCount() == 4) {
					// 该牌型为4个一样的炸弹
					List<Card> tmp = new ArrayList<>();
					tmp.addAll(group.getList());
					if (!tmp.isEmpty()) {
						list.add(tmp);
					}
				}
			}
		}
			break;
		case SI_DAI_DAN: {
			// 判断牌里面所有的四带单
			// 1.首先判断单张是否够两个
			List<Card> tmp1 = new ArrayList<>();
			for (GroupCard group : groups) {
				if (group.getCount() == 1) {
					// 单张
					tmp1.addAll(group.getList());
				}
			}
			if (tmp1.size() < 2) {
				// 单张不够2张,不处理
			} else {
				// 2.再判断4张是否存在
				for (GroupCard group : groups) {
					if (group.getCount() == 4) {
						for (int i = 0; i < tmp1.size(); i++) {
							for (int j = i + 1; j < tmp1.size(); j++) {
								Card tt1 = tmp1.get(i);
								Card tt2 = tmp1.get(j);
								List<Card> tmp = new ArrayList<>();
								for (Card card : fromCards) {
									if (card.num == group.getCardNum()) {
										tmp.add(card);
									}
								}
								tmp.add(tt1);
								tmp.add(tt2);
								list.add(tmp);
							}
						}
					}
				}
			}
			List<List<Card>> removes = new ArrayList<List<Card>>();
			// 移除带双王的情况
			for (List<Card> cards : list) {
				boolean dw = false;
				boolean xw = false;
				for (Card card : cards) {
					if (card.num == Card.DW_NUM) {
						dw = true;
					}
					if (card.num == Card.XW_NUM) {
						xw = true;
					}
				}
				if (dw && xw) {
					removes.add(cards);
				}
			}
			list.removeAll(removes);
		}
			break;
		case SI_DAI_DUI: {
			// 判断牌里面所有的四带双
			// 1.首先判断对子是否够两个
			List<List<Card>> tmp1 = new ArrayList<>();
			for (GroupCard group : groups) {
				if (group.getCount() == 2) {
					// 对子
					List<Card> tt = new ArrayList<>();
					tt.addAll(group.getList());
					tmp1.add(tt);
				}
			}
			if (tmp1.size() < 2) {
				// 对子不够2对,不处理
			} else {
				// 2.再判断4张是否存在
				for (GroupCard group : groups) {
					if (group.getCount() == 4) {
						for (int i = 0; i < tmp1.size(); i++) {
							for (int j = i + 1; j < tmp1.size(); j++) {
								List<Card> tt1 = tmp1.get(i);
								List<Card> tt2 = tmp1.get(j);
								List<Card> tmp = new ArrayList<>();
								for (Card card : fromCards) {
									if (card.num == group.getCardNum()) {
										tmp.add(card);
									}
								}
								tmp.addAll(tt1);
								tmp.addAll(tt2);
								list.add(tmp);
							}
						}
					}
				}
			}
		}
			break;
		case SAN_DAI_DAN: {
			// 判断牌里面所有的三带单
			// 1.首先判断单张是否够1个
			List<Card> tmp1 = new ArrayList<>();
			for (GroupCard group : groups) {
				if (group.getCount() == 1) {
					// 单张
					tmp1.addAll(group.getList());
				}
			}
			if (tmp1.isEmpty()) {
				// 没有单张,不做处理
			} else {
				// 2.再判断3张是否存在
				for (GroupCard group : groups) {
					if (group.getCount() == 3) {
						for (int i = 0; i < tmp1.size(); i++) {
							Card tt1 = tmp1.get(i);
							List<Card> tmp = new ArrayList<>();
							for (Card card : fromCards) {
								if (card.num == group.getCardNum()) {
									tmp.add(card);
								}
							}
							tmp.add(tt1);
							list.add(tmp);
						}
					}
				}
			}
			List<List<Card>> removes = new ArrayList<List<Card>>();
			// 移除带双王的情况
			for (List<Card> cards : list) {
				boolean dw = false;
				boolean xw = false;
				for (Card card : cards) {
					if (card.num == Card.DW_NUM) {
						dw = true;
					}
					if (card.num == Card.XW_NUM) {
						xw = true;
					}
				}
				if (dw && xw) {
					removes.add(cards);
				}
			}
			list.removeAll(removes);
		}
			break;
		case SAN_DAI_DUI: {
			// 判断牌里面所有的三带双
			// 1.首先判断对子是否够1个
			List<List<Card>> tmp1 = new ArrayList<>();
			for (GroupCard group : groups) {
				if (group.getCount() == 2) {
					// 对子
					List<Card> tt = new ArrayList<>();
					tt.addAll(group.getList());
					tmp1.add(tt);
				}
			}
			if (tmp1.isEmpty()) {
				// 没有对子,不做处理
			} else {
				// 2.再判断3张是否存在
				for (GroupCard group : groups) {
					if (group.getCount() == 3) {
						for (int i = 0; i < tmp1.size(); i++) {
							List<Card> tt1 = tmp1.get(i);
							List<Card> tmp = new ArrayList<>();
							tmp.addAll(group.getList());
							tmp.addAll(tt1);
							list.add(tmp);
						}
					}
				}
			}
		}
			break;
		case DUI: {
			// 判断牌里面所有的对子
			List<List<Card>> tmp1 = new ArrayList<>();
			for (GroupCard group : groups) {
				if (group.getCount() == 2) {
					// 对子
					List<Card> tt = new ArrayList<>();
					for (Card card : fromCards) {
						if (card.num == group.getCardNum()) {
							tt.add(card);
						}
					}
					tmp1.add(tt);
				}
			}
			list.addAll(tmp1);
		}
			break;
		case DAN: {
			// 判断牌里面所有的单张
			for (GroupCard group : groups) {
				if (group.getCount() == 1 && group.cardNum <= 15) {
					// 单张
					List<Card> tmp1 = new ArrayList<>();
					tmp1.addAll(group.getList());
					list.add(tmp1);
				}
			}
		}
			break;
		case SHUN: {
			// 判断牌里面所有的顺子(区分花色顺子比较麻烦)
			List<Card> tmp = new ArrayList<>();
			tmp.addAll(fromCards);
			Collections.sort(tmp, new Comparator<Card>() {

				@Override
				public int compare(Card o1, Card o2) {
					return o1.num - o2.num;// 按点数正序排序
				}
			});
			// 开始去重
			List<Integer> uniques = new ArrayList<>();
			List<Card> uniqueCards = new ArrayList<>();
			for (int i = 0; i < tmp.size(); i++) {
				if (!uniques.contains(tmp.get(i).num) && tmp.get(i).num >= 3 && tmp.get(i).num <= 14) {
					uniques.add(tmp.get(i).num);
					uniqueCards.add(tmp.get(i));
				}
			}
			for (int i = 0; i < uniqueCards.size(); i++) {
				List<Card> out = new ArrayList<>();
				Card card0 = uniqueCards.get(i);
				out.add(card0);
				for (int j = i + 1; j < uniqueCards.size(); j++) {
					Card card1 = uniqueCards.get(j);
					if (card1.num - card0.num == j - i) {
						out.add(card1);
						if (out.size() >= 5) {
							if (!list.contains(out)) {
								List<Card> tt = new ArrayList<>();
								tt.addAll(out);
								list.add(tt);
							}
						}
					} else {
						out.clear();
						break;
					}
				}
			}
			// 开始替换重复不同花色的集合

		}
			break;
		case LIAN_DUI: {
			// 判断牌里面所有的连对(区分花色顺子比较麻烦)
			List<Card> cards = new ArrayList<>();
			for (GroupCard group : groups) {
				if (group.count >= 2) {
					cards.addAll(group.getList());
				}
			}
			List<Card> tmp = new ArrayList<>();
			tmp.addAll(cards);
			Collections.sort(tmp, new Comparator<Card>() {

				@Override
				public int compare(Card o1, Card o2) {
					return o1.num - o2.num;// 按点数正序排序
				}
			});
			// 开始去重
			List<Integer> uniques = new ArrayList<>();
			List<Card> uniqueCards = new ArrayList<>();
			for (int i = 0; i < tmp.size(); i++) {
				if (!uniques.contains(tmp.get(i).num) && tmp.get(i).num >= 3 && tmp.get(i).num <= 14) {
					uniques.add(tmp.get(i).num);
					uniqueCards.add(tmp.get(i));
				}
			}
			for (int i = 0; i < uniqueCards.size(); i++) {
				List<Card> out = new ArrayList<>();
				Card card0 = uniqueCards.get(i);
				out.add(card0);
				for (int j = i + 1; j < uniqueCards.size(); j++) {
					Card card1 = uniqueCards.get(j);
					if (card1.num - card0.num == j - i) {
						out.add(card1);
						if (out.size() >= 3) {
							if (!list.contains(out)) {
								List<Card> tt = new ArrayList<>();
								for (Card card : out) {
									int num = card.num;
									// 拿两张
									int count = 0;
									for (Card card2 : fromCards) {
										if (card2.num == num) {
											tt.add(card2);
											count++;
											if (count >= 2) {
												break;
											}
										}
									}
								}
								list.add(tt);
							}
						}
					} else {
						out.clear();
						break;
					}
				}
			}
			// 开始替换重复不同花色的集合
		}
			break;
		case SAN_ZHANG: {
			// 判断牌里面所有的三张
			for (GroupCard group : groups) {
				if (group.getCount() == 3) {
					List<Card> tmp = new ArrayList<>();
					tmp.addAll(group.getList());
					list.add(tmp);
				}
			}
		}
			break;
		case FEI_JI: {
			// 判断牌里面所有的飞机
			List<GroupCard> groupList = new ArrayList<>();
			for (GroupCard groupCard : groups) {
				if (groupCard.count == 3 && groupCard.getCardNum() >= 3 && groupCard.getCardNum() <= 14) {
					groupList.add(groupCard);
				}
			}
			Collections.sort(groupList, new Comparator<GroupCard>() {

				@Override
				public int compare(GroupCard o1, GroupCard o2) {
					return o1.getCardNum() - o2.getCardNum();
				}
			});
			List<List<GroupCard>> ttt = new ArrayList<>();
			// 2.再判断飞机是否存在
			for (int i = 0; i < groupList.size() - 1; i++) {
				List<GroupCard> out = new ArrayList<>();
				GroupCard group0 = groupList.get(i);
				out.add(group0);
				for (int j = i + 1; j < groupList.size(); j++) {
					GroupCard group1 = groupList.get(j);
					if (group1.getCardNum() - group0.getCardNum() == j - i) {
						// 可以构成飞机
						out.add(group1);
						if (out.size() >= 2) {
							if (!ttt.contains(out)) {
								List<GroupCard> tt = new ArrayList<>();
								tt.addAll(out);
								ttt.add(tt);
							}
						}
					} else {
						out.clear();
						break;
					}
				}
			}
			for (List<GroupCard> t : ttt) {
				List<Card> cards = new ArrayList<>();
				for (GroupCard group : t) {
					cards.addAll(group.getList());
				}
				list.add(cards);
			}
		}
			break;
		case FEI_JI_DAI_DAN: {
			// 判断牌里面所有的飞机带单翅膀
			List<GroupCard> groupList = new ArrayList<>();
			for (GroupCard groupCard : groups) {
				if (groupCard.count == 3 && groupCard.getCardNum() >= 3 && groupCard.getCardNum() <= 14) {
					groupList.add(groupCard);
				}
			}
			Collections.sort(groupList, new Comparator<GroupCard>() {

				@Override
				public int compare(GroupCard o1, GroupCard o2) {
					return o1.getCardNum() - o2.getCardNum();
				}
			});
			List<List<GroupCard>> ttt = new ArrayList<>();
			// 2.再判断飞机是否存在
			for (int i = 0; i < groupList.size() - 1; i++) {
				List<GroupCard> out = new ArrayList<>();
				GroupCard group0 = groupList.get(i);
				out.add(group0);
				for (int j = i + 1; j < groupList.size(); j++) {
					GroupCard group1 = groupList.get(j);
					if (group1.getCardNum() - group0.getCardNum() == j - i) {
						// 可以构成飞机
						out.add(group1);
						if (out.size() >= 2) {
							if (!ttt.contains(out)) {
								List<GroupCard> tt = new ArrayList<>();
								tt.addAll(out);
								ttt.add(tt);
							}
						}
					} else {
						out.clear();
						break;
					}
				}
			}
			List<Card> tmp1 = new ArrayList<>();
			for (GroupCard group : groups) {
				if (group.getCount() == 1) {
					// 单张
					tmp1.addAll(group.getList());
				}
				if(group.getCount()==2) {
					tmp1.addAll(group.getList());
				}
			}
			for (int i = 0; i < ttt.size(); i++) {
				List<GroupCard> tt = ttt.get(i);
				int size = tt.size();
				// 判断是否有这么多单张存在
				if (tmp1.size() >= size) {
					// 可以配合成为飞机,开始组合牌
					List<List<Card>> out = new ArrayList<>();
					Combine.combine(tmp1, out, size);// 组合单张
					// 再加3张的
					for (List<Card> tmp : out) {
						for (GroupCard groupCard : tt) {
							tmp.addAll(groupCard.getList());
						}
					}
					list.addAll(out);
				}
			}

			List<List<Card>> removes = new ArrayList<List<Card>>();
			// 移除带双王的情况
			for (List<Card> cards : list) {
				boolean dw = false;
				boolean xw = false;
				for (Card card : cards) {
					if (card.num == Card.DW_NUM) {
						dw = true;
					}
					if (card.num == Card.XW_NUM) {
						xw = true;
					}
				}
				if (dw && xw) {
					removes.add(cards);
				}
			}
			list.removeAll(removes);
		}
			break;
		case FEI_JI_DAI_DUI: {
			// 判断牌里面所有的飞机带双翅膀
			List<GroupCard> groupList = new ArrayList<>();
			for (GroupCard groupCard : groups) {
				if (groupCard.count == 3 && groupCard.getCardNum() >= 3 && groupCard.getCardNum() <= 14) {
					groupList.add(groupCard);
				}
			}
			Collections.sort(groupList, new Comparator<GroupCard>() {

				@Override
				public int compare(GroupCard o1, GroupCard o2) {
					return o1.getCardNum() - o2.getCardNum();
				}
			});
			List<List<GroupCard>> ttt = new ArrayList<>();
			// 2.再判断飞机是否存在
			for (int i = 0; i < groupList.size() - 1; i++) {
				List<GroupCard> out = new ArrayList<>();
				GroupCard group0 = groupList.get(i);
				out.add(group0);
				for (int j = i + 1; j < groupList.size(); j++) {
					GroupCard group1 = groupList.get(j);
					if (group1.getCardNum() - group0.getCardNum() == j - i) {
						// 可以构成飞机
						out.add(group1);
						if (out.size() >= 2) {
							if (!ttt.contains(out)) {
								List<GroupCard> tt = new ArrayList<>();
								tt.addAll(out);
								ttt.add(tt);
							}
						}
					} else {
						out.clear();
						break;
					}
				}
			}
			List<GroupCard> tmp1 = new ArrayList<>();
			for (GroupCard group : groups) {
				if (group.getCount() == 2) {
					// 单张
					tmp1.add(group);
				}
			}
			List<List<Card>> tmp = new ArrayList<>();
			for (int i = 0; i < ttt.size(); i++) {
				List<GroupCard> tt = ttt.get(i);
				int size = tt.size();
				// 判断是否有这么多对子存在
				if (tmp1.size() >= size) {
					// 可以配合成为飞机,开始组合牌
					List<List<GroupCard>> out = new ArrayList<>();
					Combine.combine(tmp1, out, size);// 组合单张
					// 再加3张的
					for (List<GroupCard> t : out) {
						List<Card> cards = new ArrayList<>();
						for (GroupCard groupCard : tt) {
							cards.addAll(groupCard.getList());
						}
						for (GroupCard g : t) {
							cards.addAll(g.getList());
						}
						tmp.add(cards);
					}
					list.addAll(tmp);
				}
			}
		}
			break;
		default:
			break;
		}
		return list;
	}

	public static List<GroupCard> groupByCardNum(Collection<Card> fromCards) {
		Map<Integer, GroupCard> map = new HashMap<>();
		for (Card card : fromCards) {
			if (!map.containsKey(card.num)) {
				GroupCard group = new GroupCard(card.num, 1);
				group.getList().add(card);
				map.put(card.num, group);

			} else {
				GroupCard groupCard = map.get(card.num);
				groupCard.getList().add(card);
				groupCard.setCount(groupCard.getCount() + 1);
			}
		}
		Collection<GroupCard> groupsColletion = map.values();
		List<GroupCard> list = new ArrayList<>();
		list.addAll(groupsColletion);
		Collections.sort(list, new Comparator<GroupCard>() {
			@Override
			public int compare(GroupCard o1, GroupCard o2) {
				int num = o1.getCount() - o2.getCount();
				if (num == 0) {
					num = o1.getCardNum() - o2.getCardNum();
				}
				return num;
			}
		});
		return list;
	}

	public static class GroupCard {
		private int cardNum;
		private int count;
		private final List<Card> list = new ArrayList<>();

		public GroupCard() {
			super();
		}

		public GroupCard(int cardNum, int count) {
			super();
			this.cardNum = cardNum;
			this.count = count;
		}

		public int getCardNum() {
			return this.cardNum;
		}

		public void setCardNum(int cardNum) {
			this.cardNum = cardNum;
		}

		public List<Card> getList() {
			return this.list;
		}

		public int getCount() {
			return this.count;
		}

		public void setCount(int count) {
			this.count = count;
		}
	}
}

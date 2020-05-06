package com.dmg.zhajinhuaserver.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuqd
 * @Date 2017年8月29日
 * @Desc 牌型
 */
public class Segment {

	private int type;
	private List<Poker> member = new ArrayList<>();
	private int star; // 炸弹星级
	private int phase; // 相
	//

	public Segment() {
		this.type = 0;
	}

	public Segment(int type, List<Poker> member) {
		this.type = type;
		this.member = member;
		this.star = 0;
		this.phase = 0;
	}

	public Segment(int type, List<Poker> member, int star, int phase) {
		this.type = type;
		this.member = member;
		this.star = star;
		this.phase = phase;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<Poker> getMember() {
		return member;
	}

	public void setMember(List<Poker> member) {
		this.member = member;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

	@Override
	public String toString() {
		return "[" + member.toString() + "]";
	}

}

package com.zyhy.lhj_server.game.yzhx;

public class YzhxWildInfo {
	// wild所在轴
	private int id;
	// wild所在位置
	private int index;
	
	public YzhxWildInfo() {
		super();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	
	@Override
	public String toString() {
		return "WildInfo [id=" + id + ", index=" + index + "]";
	}
	
	
}

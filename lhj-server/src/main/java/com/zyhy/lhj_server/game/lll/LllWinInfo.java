/**
 * 
 */
package com.zyhy.lhj_server.game.lll;

/**
 * @author ASUS
 *
 */
public class LllWinInfo {

	//新路id
	private int id;
	//位置1
	private LllWindowInfo index1;
	//位置2
	private LllWindowInfo index2;
	//位置3
	private LllWindowInfo index3;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LllWindowInfo getIndex1() {
		return index1;
	}
	public void setIndex1(LllWindowInfo index1) {
		this.index1 = index1;
	}
	public LllWindowInfo getIndex2() {
		return index2;
	}
	public void setIndex2(LllWindowInfo index2) {
		this.index2 = index2;
	}
	public LllWindowInfo getIndex3() {
		return index3;
	}
	public void setIndex3(LllWindowInfo index3) {
		this.index3 = index3;
	}
	@Override
	public String toString() {
		return "LllWinInfo [id=" + id + ", index1=" + index1 + ", index2="
				+ index2 + ", index3=" + index3 + "]";
	}
	
}

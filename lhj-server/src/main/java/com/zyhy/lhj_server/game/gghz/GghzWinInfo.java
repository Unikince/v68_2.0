/**
 * 
 */
package com.zyhy.lhj_server.game.gghz;

/**
 * @author ASUS
 *
 */
public class GghzWinInfo {

	//新路id
	private int id;
	//位置1
	private GghzWindowInfo index1;
	//位置2
	private GghzWindowInfo index2;
	//位置3
	private GghzWindowInfo index3;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public GghzWindowInfo getIndex1() {
		return index1;
	}
	public void setIndex1(GghzWindowInfo index1) {
		this.index1 = index1;
	}
	public GghzWindowInfo getIndex2() {
		return index2;
	}
	public void setIndex2(GghzWindowInfo index2) {
		this.index2 = index2;
	}
	public GghzWindowInfo getIndex3() {
		return index3;
	}
	public void setIndex3(GghzWindowInfo index3) {
		this.index3 = index3;
	}
	@Override
	public String toString() {
		return "GghzWinInfo [id=" + id + ", index1=" + index1 + ", index2="
				+ index2 + ", index3=" + index3 + "]";
	}
	
}

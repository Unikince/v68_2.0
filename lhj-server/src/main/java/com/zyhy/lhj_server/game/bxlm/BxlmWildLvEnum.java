/**
 * 
 */
package com.zyhy.lhj_server.game.bxlm;


/**
 * @author ASUS
 *
 */
public enum BxlmWildLvEnum{
	A(1, 1000,500,5,1,0),
	B(2, 3000,100,50,5,0),
	C(3, 2000,1000,50,5,0),
	D(4,2000,1000,50,5,0),
	E(5,2000,1000,50,5,0),
	F(6,2000,1000,50,5,0),
	G(7,2000,1000,50,5,0),
	H(8,2000,1000,50,5,0);
	private int id;  // 使用的图标id
	private int lv1;	 // wild出现1列的概率
	private int lv2;	 // wild出现2列的概率
	private int lv3;	 // wild出现3列的概率
	private int lv4;	 // wild出现4列的概率
	private int lv5;	 // wild出现5列的概率
	
	private BxlmWildLvEnum(int id, int lv1, int lv2, int lv3, int lv4, int lv5) {
		this.id = id;
		this.lv1 = lv1;
		this.lv2 = lv2;
		this.lv3 = lv3;
		this.lv4 = lv4;
		this.lv5 = lv5;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLv1() {
		return lv1;
	}

	public void setLv1(int lv1) {
		this.lv1 = lv1;
	}

	public int getLv2() {
		return lv2;
	}

	public void setLv2(int lv2) {
		this.lv2 = lv2;
	}
	
	public int getLv3() {
		return lv3;
	}

	public void setLv3(int lv3) {
		this.lv3 = lv3;
	}

	public int getLv4() {
		return lv4;
	}

	public void setLv4(int lv4) {
		this.lv4 = lv4;
	}

	public int getLv5() {
		return lv5;
	}

	public void setLv5(int lv5) {
		this.lv5 = lv5;
	}

	public static BxlmWildLvEnum getLvById(int id){
		for (BxlmWildLvEnum e : values()) {
			if (e.getId() == id) {
				return e;
			}
		}
		return null;
	}
	
}

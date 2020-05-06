/**
 * 
 */
package com.zyhy.lhj_server.game.lqjx;


/**
 * @author ASUS
 *
 */
public enum LqjxWildLvEnum{
	A(1, 200,50),
	B(2, 300,100),
	C(3, 500,100),
	D(4,500,100),
	E(5,1000,100),
	F(6,1000,200),
	G(7,1000,200),
	H(8,1000,200);
	private int id;  // 使用的图标id
	private int lv1;	 // wild出现1列的概率
	private int lv2;	 // wild出现2列的概率
	

	private LqjxWildLvEnum(int id, int lv1, int lv2) {
		this.id = id;
		this.lv1 = lv1;
		this.lv2 = lv2;
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
	
	public static LqjxWildLvEnum getLvById(int id){
		for (LqjxWildLvEnum e : values()) {
			if (e.getId() == id) {
				return e;
			}
		}
		return null;
	}
	
}

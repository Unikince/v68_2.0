/**
 * 
 */
package com.zyhy.lhj_server.game.bqtp;


/**
 * @author ASUS
 *
 */
public enum BqtpWildLvEnum{
	A(1, 100,10,5),
	B(2, 100,50,5),
	C(3, 100,50,5),
	D(4,500,50,10),
	E(5,500,50,5),
	F(6,500,100,5),
	G(7,500,100,10),
	H(8,500,100,10);
	private int id;  // 使用的图标id
	private int lv1;	 // wild出现1列的概率
	private int lv2;	 // wild出现2列的概率
	private int lv3;	 // wild出现3列的概率
	

	private BqtpWildLvEnum(int id, int lv1, int lv2, int lv3) {
		this.id = id;
		this.lv1 = lv1;
		this.lv2 = lv2;
		this.lv3 = lv3;
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

	public static BqtpWildLvEnum getLvById(int id){
		for (BqtpWildLvEnum e : values()) {
			if (e.getId() == id) {
				return e;
			}
		}
		return null;
	}
	
}

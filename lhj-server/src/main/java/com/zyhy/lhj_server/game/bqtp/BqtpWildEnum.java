/**
 * 
 */
package com.zyhy.lhj_server.game.bqtp;


/**
 * @author ASUS
 *
 */
public enum BqtpWildEnum{
// 2,3,4对应转轴3,4,5
	one1(1, 2),
	one2(2, 3),
	one3(3, 4),
	Two1(4,2,3),
	Two2(5,2,4),
	Two3(6,3,4),
	Three(7, 2, 3, 4);
//	one1(1, 2,3),
//	one2(2, 3,4),
//	one3(3, 4,3),
//	Two1(4,2,3),
//	Two2(5,2,4),
//	Two3(6,3,4),
//	Three(7, 2, 3, 4);
	private int id;
	private int[] index;	//wild出现的列数
	
	BqtpWildEnum(int id, int... index){
		this.id = id;
		this.index = index;
	}

	public int getId() {
		return id;
	}

	public static BqtpWildEnum getById(int id) {
		for(BqtpWildEnum v:values()){
			if(v.getId() == id){
				return v;
			}
		}
		return null;
	}

	public int[] getIndex() {
		return index;
	}
}

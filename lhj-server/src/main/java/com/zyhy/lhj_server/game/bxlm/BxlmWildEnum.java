/**
 * 
 */
package com.zyhy.lhj_server.game.bxlm;


/**
 * @author ASUS
 *
 */
public enum BxlmWildEnum{

	one1(1, 1),
	one2(2, 2),
	one3(3, 3),
	one4(4, 4),
	one5(5, 5),
	
	Two1(6,1,2),
	Two2(7,2,3),
	Two3(8,3,4),
	Two4(9,4,5),
	Two5(10,5,1),
	Two6(11,1,3),
	Two7(12,1,4),
	Two8(13,2,4),
	Two9(14,2,5),
	Two10(15,3,5),
	
	Three1(16, 1, 2, 3),
	Three2(17, 1, 2, 4),
	Three3(18, 1, 2, 5),
	Three4(19, 1, 3, 4),
	Three5(20, 1, 3, 5),
	Three6(21, 2, 3, 4),
	Three7(22, 2, 3, 5),
	Three8(23, 2, 4, 5),
	Three9(24, 3, 4, 5),
	
	four1(25, 1, 2, 3,4),
	four2(26, 1, 2, 3,5),
	four3(27, 2, 3, 4,5),
	
	five1(28, 1, 2, 3, 4,5),
	;
	private int id;
	private int[] index;	//wild出现的列数
	
	BxlmWildEnum(int id, int... index){
		this.id = id;
		this.index = index;
	}

	public int getId() {
		return id;
	}

	public static BxlmWildEnum getById(int id) {
		for(BxlmWildEnum v:values()){
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

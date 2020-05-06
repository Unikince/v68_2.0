/**
 * 
 */
package com.zyhy.lhj_server.game.bqtp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.RandomUtil;

/**
 * @author ASUS
 * 轮子权重
 */
public enum BqtpRollerWeightEnum_backup {

	//A10(1, 9, BqtpIconEnum.SCATTER, 300),
	A1(1, 0, BqtpIconEnum.S1, 10),
	A2(1, 1, BqtpIconEnum.A1, 15),
	A3(1, 2, BqtpIconEnum.A2, 20),
	A4(1, 3, BqtpIconEnum.A3, 25),
	A5(1, 4, BqtpIconEnum.A, 100),
	A6(1, 5, BqtpIconEnum.K, 150),
	A7(1, 6, BqtpIconEnum.Q, 200),
	A8(1, 7, BqtpIconEnum.J, 250),
	A9(1, 8, BqtpIconEnum.T10, 300),
	A11(1, 10, BqtpIconEnum.A, 100),
	A12(1, 11, BqtpIconEnum.K, 150),
	A13(1, 12, BqtpIconEnum.Q, 200),
	A14(1, 13, BqtpIconEnum.J, 250),
	A19(1, 18, BqtpIconEnum.T10, 300),
	A10(1, 9, BqtpIconEnum.SCATTER, 100),
	
	//B10(2, 9, BqtpIconEnum.SCATTER, 300),
	B1(2, 0, BqtpIconEnum.S1, 100),
	B2(2, 1, BqtpIconEnum.A1, 150),
	B3(2, 2, BqtpIconEnum.A2, 200),
	B4(2, 3, BqtpIconEnum.A3, 250),
	B5(2, 4, BqtpIconEnum.A, 50),
	B6(2, 5, BqtpIconEnum.K, 60),
	B7(2, 6, BqtpIconEnum.Q, 70),
	B8(2, 7, BqtpIconEnum.J, 80),
	B9(2, 8, BqtpIconEnum.T10, 90),
	B10(2, 9, BqtpIconEnum.SCATTER, 100),
	B11(2, 10, BqtpIconEnum.S1, 100),
	B12(2, 11, BqtpIconEnum.A1, 150),
	B13(2, 12, BqtpIconEnum.A2, 200),
	B14(2, 13, BqtpIconEnum.A3, 250),
	
	
	//C10(3, 9, BqtpIconEnum.SCATTER, 300),
	C1(3, 0, BqtpIconEnum.S1, 20),
	C2(3, 1, BqtpIconEnum.A1, 30),
	C3(3, 2, BqtpIconEnum.A2, 40),
	C4(3, 3, BqtpIconEnum.A3, 50),
	C5(3, 4, BqtpIconEnum.A, 100),
	C6(3, 5, BqtpIconEnum.K, 150),
	C7(3, 6, BqtpIconEnum.Q, 200),
	C8(3, 7, BqtpIconEnum.J, 250),
	C9(3, 8, BqtpIconEnum.T10, 300),
	C10(3, 9, BqtpIconEnum.SCATTER, 100),
	C11(3, 10, BqtpIconEnum.A, 100),
	C12(3, 11, BqtpIconEnum.K, 150),
	C13(3, 12, BqtpIconEnum.Q, 200),
	C14(3, 13, BqtpIconEnum.J, 250),
	C19(3, 18, BqtpIconEnum.T10, 300),
	C20(3, 19, BqtpIconEnum.WILD, 50),
	
	
	//D10(4, 9, BqtpIconEnum.SCATTER, 300),
	D1(4, 0, BqtpIconEnum.S1, 10),
	D2(4, 1, BqtpIconEnum.A1, 15),
	D3(4, 2, BqtpIconEnum.A2, 20),
	D4(4, 3, BqtpIconEnum.A3, 25),
	D5(4, 4, BqtpIconEnum.A, 100),
	D6(4, 5, BqtpIconEnum.K, 150),
	D7(4, 6, BqtpIconEnum.Q, 200),
	D8(4, 7, BqtpIconEnum.J, 250),
	D9(4, 8, BqtpIconEnum.T10, 300),
	D10(4, 9, BqtpIconEnum.SCATTER, 100),
	D11(4, 10, BqtpIconEnum.A, 100),
	D12(4, 11, BqtpIconEnum.K, 150),
	D13(4, 12, BqtpIconEnum.Q, 200),
	D14(4, 13, BqtpIconEnum.J, 250),
	D15(4, 10, BqtpIconEnum.A, 100),
	D16(4, 11, BqtpIconEnum.K, 150),
	D17(4, 12, BqtpIconEnum.Q, 200),
	D18(4, 13, BqtpIconEnum.J, 250),
	D19(4, 18, BqtpIconEnum.T10, 300),
	D20(4, 19, BqtpIconEnum.WILD, 50),
	
	//E10(5, 9, BqtpIconEnum.SCATTER, 300),
	E1(5, 0, BqtpIconEnum.S1, 10),
	E2(5, 1, BqtpIconEnum.A1, 15),
	E3(5, 2, BqtpIconEnum.A2, 20),
	E4(5, 3, BqtpIconEnum.A3, 25),
	E5(5, 4, BqtpIconEnum.A, 100),
	E6(5, 5, BqtpIconEnum.K, 150),
	E7(5, 6, BqtpIconEnum.Q, 200),
	E8(5, 7, BqtpIconEnum.J, 250),
	E9(5, 8, BqtpIconEnum.T10, 300),
	E10(5, 9, BqtpIconEnum.SCATTER, 100),
	E11(5, 10, BqtpIconEnum.A, 100),
	E12(5, 11, BqtpIconEnum.K, 150),
	E13(5, 12, BqtpIconEnum.Q, 200),
	E14(5, 13, BqtpIconEnum.J, 250),
	E15(5, 14, BqtpIconEnum.A, 300),
	E16(5, 15, BqtpIconEnum.K, 300),
	E17(5, 16, BqtpIconEnum.Q, 300),
	E18(5, 17, BqtpIconEnum.J, 300),
	E19(5, 18, BqtpIconEnum.T10, 400),
	E20(5, 19, BqtpIconEnum.WILD, 50),
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private BqtpIconEnum icon;
	//权重
	private int weight;
	
	private BqtpRollerWeightEnum_backup(int id, int index, BqtpIconEnum icon, int weight){
		this.id = id;
		this.index = index;
		this.icon = icon;
		this.weight = weight;
	}

	public int getId() {
		return id;
	}

	public int getIndex() {
		return index;
	}

	public BqtpIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	public static BqtpRollerWeightEnum_backup random(int c) {
		List<BqtpRollerWeightEnum_backup> all = new ArrayList<BqtpRollerWeightEnum_backup>();
		for(BqtpRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(BqtpRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		for(BqtpRollerWeightEnum_backup e : all){
			int w = v + e.getWeight();
			if(num >= v && num < w){
				return e;
			}else{
				v = w;
			}
		}
		return null;
	}

	/**
	 * 获取
	 * @param i
	 * @return
	 */
	public static WindowInfo windowInfo(int c,int d,List<WindowInfo> list) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<BqtpRollerWeightEnum_backup> all = new ArrayList<BqtpRollerWeightEnum_backup>();
		for(BqtpRollerWeightEnum_backup e : values()){
			if(e.getId() == c+1){
				all.add(e);
			}
		}
		int total = 0;
		for(BqtpRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		BqtpRollerWeightEnum_backup roller = null;
		Iterator<BqtpRollerWeightEnum_backup> it = all.iterator();
		while(roller == null){
			while(it.hasNext()){
				BqtpRollerWeightEnum_backup e = it.next();
				int w = v + e.getWeight();
				if(num >= v && num <= w){
					roller = e;
					break;
				}else{
					v = w;
				}
			}
		}
		
		if (list.size() == 0) {
			ws.add(new WindowInfo(c, d, roller.getIcon()));
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getIcon() == roller.getIcon() 
						&& roller.getIcon() == BqtpIconEnum.SCATTER) {
					ws.clear();
					ws.add(windowInfo(c,d,list));
				}else {
					ws.clear();
					ws.add(new WindowInfo(c, d, roller.getIcon()));
				}
			}
		}
		
		/*if (list.size() == 0) {
			ws.add(new WindowInfo(c, d, roller.getIcon()));
		} else {
			for (WindowInfo wi : list) {
				if (wi.getIcon() == roller.getIcon() 
						&& roller.getIcon() == BqtpIconEnum.SCATTER) {
					ws.add(windowInfo(c,d,list));
				} else {
					ws.add(new WindowInfo(c, d, roller.getIcon()));
				}
			}
		}*/
		return ws.get(0);
	}
}

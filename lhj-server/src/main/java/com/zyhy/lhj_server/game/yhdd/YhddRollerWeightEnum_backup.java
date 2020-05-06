/**
 * 
 */
package com.zyhy.lhj_server.game.yhdd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.RandomUtil;

/**
 * @author ASUS
 * 轮子权重
 */
public enum YhddRollerWeightEnum_backup {
	
	
	
	A3(1, 3, YhddIconEnum.A2, 200),
	//A4(1, 4, YhddIconEnum.A, 360),
	//A5(1, 5, YhddIconEnum.K, 280),
	A16(1, 16, YhddIconEnum.BONUS, 20),
	A6(1, 6, YhddIconEnum.A1, 250),
	//A7(1, 7, YhddIconEnum.Q, 200),
	A1(1, 1, YhddIconEnum.SCATTER, 30),
	//A8(1, 8, YhddIconEnum.J, 200),
	A11(1, 11, YhddIconEnum.A3, 300),
	//A12(1, 12, YhddIconEnum.Q, 200),
	A13(1, 13, YhddIconEnum.S1, 350),
	A14(1, 14, YhddIconEnum.J, 200),
	A17(1, 17, YhddIconEnum.K, 200),
	//A22(1, 17, YhddIconEnum.J, 200),
	A21(1, 17, YhddIconEnum.A, 300),
	A23(1, 17, YhddIconEnum.Q, 200),
	A26(1, 17, YhddIconEnum.S1, 300),
	A18(1, 18, YhddIconEnum.A3, 200),
	A19(1, 19, YhddIconEnum.A2, 250),
	A20(1, 20, YhddIconEnum.A1, 300),
	A27(1, 18, YhddIconEnum.A3, 200),
	A28(1, 19, YhddIconEnum.A2, 250),
	A29(1, 20, YhddIconEnum.A1, 300),
	
	B2(2, 0, YhddIconEnum.WILD, 250),
	B3(2, 4, YhddIconEnum.A2, 200),
	B4(2, 3, YhddIconEnum.A1, 250),
	B1(2, 1, YhddIconEnum.SCATTER, 30),
	B5(2, 8, YhddIconEnum.Q, 200),
	B6(2, 2, YhddIconEnum.S1, 350),
	B7(2, 9, YhddIconEnum.J, 200),
	//B10(2, 12, YhddIconEnum.A, 360),
	B11(2, 5, YhddIconEnum.A3, 300),
	//B12(2, 13, YhddIconEnum.K, 280),
	//B13(2, 14, YhddIconEnum.Q, 200),
	//B14(2, 15, YhddIconEnum.J, 200),
	B21(2, 14, YhddIconEnum.A, 300),
	B17(2, 14, YhddIconEnum.K, 200),
	B22(2, 14, YhddIconEnum.J, 200),
	B18(2, 15, YhddIconEnum.A3, 200),
	//B23(2, 15, YhddIconEnum.Q, 200),
	B19(2, 16, YhddIconEnum.A2, 250),
	B25(2, 16, YhddIconEnum.S1, 350),
	B20(2, 17, YhddIconEnum.A1, 300),
	B24(2, 17, YhddIconEnum.A1, 300),
	B27(2, 17, YhddIconEnum.A2, 300),
	B26(2, 17, YhddIconEnum.A3, 300),
	
	C5(3, 4, YhddIconEnum.A2, 200),
	C4(3, 3, YhddIconEnum.A1, 250),
	C9(3, 8, YhddIconEnum.Q, 200),
	C2(3, 1, YhddIconEnum.SCATTER, 30),
	C10(3, 9, YhddIconEnum.J, 200),
	//C13(3, 12, YhddIconEnum.A, 360),
	C6(3, 5, YhddIconEnum.A3, 300),
	//C14(3, 13, YhddIconEnum.K, 280),
	//C15(3, 14, YhddIconEnum.Q, 200),
	C25(3, 2, YhddIconEnum.S1, 350),
	//C16(3, 15, YhddIconEnum.J, 200),
	C23(3, 17, YhddIconEnum.A, 300),
	C19(3, 14, YhddIconEnum.K, 200),
	C27(3, 14, YhddIconEnum.S1, 300),
	//C26(3, 14, YhddIconEnum.Q, 200),
	C20(3, 15, YhddIconEnum.A3, 200),
	//C24(3, 15, YhddIconEnum.J, 200),
	C28(3, 15, YhddIconEnum.WILD, 200),
	C21(3, 16, YhddIconEnum.A2, 250),
	C22(3, 17, YhddIconEnum.A1, 300),
	C30(3, 17, YhddIconEnum.A1, 300),
	C31(3, 17, YhddIconEnum.A2, 300),
	C29(3, 17, YhddIconEnum.A3, 300),
	
	D1(4, 0, YhddIconEnum.WILD, 250),
	D2(4, 1, YhddIconEnum.SCATTER, 30),
	D26(4, 4, YhddIconEnum.S1, 300),
	D5(4, 4, YhddIconEnum.A2, 200),
	D4(4, 3, YhddIconEnum.A1, 250),
	//D9(4, 8, YhddIconEnum.Q, 200),
	//D10(4, 9, YhddIconEnum.J, 200),
	//D13(4, 12, YhddIconEnum.A, 360),
	D6(4, 5, YhddIconEnum.A3, 300),
	//D14(4, 13, YhddIconEnum.K, 280),
	//D15(4, 14, YhddIconEnum.Q, 200),
	//D16(4, 15, YhddIconEnum.J, 200),
	D3(4, 2, YhddIconEnum.S1, 350),
	D23(4, 2, YhddIconEnum.A, 300),
	D19(4, 14, YhddIconEnum.K, 200),
	D20(4, 15, YhddIconEnum.A3, 200),
	D25(4, 15, YhddIconEnum.Q, 200),
	D24(4, 15, YhddIconEnum.J, 200),
	D21(4, 16, YhddIconEnum.A2, 250),
	D22(4, 17, YhddIconEnum.A1, 300),
	D27(4, 17, YhddIconEnum.A2, 300),
	D28(4, 17, YhddIconEnum.A3, 300),
	D29(4, 17, YhddIconEnum.A1, 300),
	
	E2(5, 1, YhddIconEnum.SCATTER, 30),
	E5(5, 4, YhddIconEnum.A2, 200),
	E4(5, 3, YhddIconEnum.A1, 250),
	E9(5, 8, YhddIconEnum.Q, 100),
	E10(5, 9, YhddIconEnum.J, 200),
	E25(5, 2, YhddIconEnum.S1, 350),
	//E13(5, 12, YhddIconEnum.A, 360),
	E6(5, 5, YhddIconEnum.A3, 300),
	//E14(5, 13, YhddIconEnum.K, 280),
	//E15(5, 14, YhddIconEnum.Q, 200),
	//E16(5, 15, YhddIconEnum.J, 300),
	E18(5, 17, YhddIconEnum.BONUS, 30), 
	E23(5, 17, YhddIconEnum.A, 300), 
	//E24(5, 17, YhddIconEnum.J, 300), 
	E19(5, 14, YhddIconEnum.K, 300),
	E20(5, 15, YhddIconEnum.A3, 300),
	E21(5, 16, YhddIconEnum.A2, 250),
	//E26(5, 16, YhddIconEnum.Q, 250),
	E27(5, 16, YhddIconEnum.S1, 350),
	E22(5, 17, YhddIconEnum.A1, 300),
	E28(5, 17, YhddIconEnum.A1, 300),
	E29(5, 17, YhddIconEnum.A2, 300),
	E30(5, 17, YhddIconEnum.A3, 300),
	
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private YhddIconEnum icon;
	//权重
	private int weight;
	
	private YhddRollerWeightEnum_backup(int id, int index, YhddIconEnum icon, int weight){
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

	public YhddIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	public static YhddRollerWeightEnum_backup random(int c) {
		List<YhddRollerWeightEnum_backup> all = new ArrayList<YhddRollerWeightEnum_backup>();
		for(YhddRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(YhddRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		for(YhddRollerWeightEnum_backup e : all){
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
	public static Collection<? extends Window> windowInfo(int c) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<YhddRollerWeightEnum_backup> all = new ArrayList<YhddRollerWeightEnum_backup>();
		for(YhddRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(YhddRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		YhddRollerWeightEnum_backup roller = null;
		Iterator<YhddRollerWeightEnum_backup> it = all.iterator();
		while(it.hasNext()){
			YhddRollerWeightEnum_backup e = it.next();
			int w = v + e.getWeight();
			if(num >= v && num <= w){
				roller = e;
				break;
			}else{
				v = w;
			}
		}
		ws.add(new WindowInfo(c, 1, roller.getIcon()));
		int i=2;
		int index = 0;
		for(;i<=3;i++){
			if(it.hasNext()){
				roller = it.next();
			}else{
				roller = all.get(index);
				index++;
			}
			ws.add(new WindowInfo(c, i, roller.getIcon()));
		}
		return ws;
	}
}

/**
 * 
 */
package com.zyhy.lhj_server.game.hjws;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.RandomUtil;

/**
 * @author ASUS
 * 轮子权重
 */
public enum HjwsRollerWeightEnum_backup {
	
	//A12(1, 11, HjwsIconEnum.SCATTER, 500),
	A1(1, 0, HjwsIconEnum.S1, 100),
	A2(1, 1, HjwsIconEnum.A1, 200),
	A3(1, 2, HjwsIconEnum.A2, 200),
	A4(1, 3, HjwsIconEnum.A3, 200),
	A5(1, 4, HjwsIconEnum.A4, 200),
	A6(1, 5, HjwsIconEnum.A, 300),
	A7(1, 6, HjwsIconEnum.K, 300),
	A8(1, 7, HjwsIconEnum.Q, 300),
	A9(1, 8, HjwsIconEnum.J, 300),
	A10(1, 9, HjwsIconEnum.T10, 400),
	A11(1, 10, HjwsIconEnum.T9, 400),
	A12(1, 11, HjwsIconEnum.SCATTER, 150),
	A13(1, 12, HjwsIconEnum.S1, 100),
	A14(1, 13, HjwsIconEnum.A1, 200),
	A15(1, 14, HjwsIconEnum.A2, 200),
	A16(1, 15, HjwsIconEnum.A3, 200),
	A17(1, 16, HjwsIconEnum.A4, 200),
	A18(1, 17, HjwsIconEnum.A, 300),
	A19(1, 18, HjwsIconEnum.K, 300),
	A20(1, 19, HjwsIconEnum.Q, 300),
	A21(1, 20, HjwsIconEnum.J, 300),
	A22(1, 21, HjwsIconEnum.T10, 400),
	A23(1, 22, HjwsIconEnum.T9, 400),
	
	//B12(2, 11, HjwsIconEnum.SCATTER, 500),
	B1(2, 0, HjwsIconEnum.S1, 100),
	B2(2, 1, HjwsIconEnum.A1, 200),
	B3(2, 2, HjwsIconEnum.A2, 200),
	B4(2, 3, HjwsIconEnum.A3, 200),
	B5(2, 4, HjwsIconEnum.A4, 200),
	B6(2, 5, HjwsIconEnum.A, 300),
	B7(2, 6, HjwsIconEnum.K, 300),
	B8(2, 7, HjwsIconEnum.Q, 300),
	B9(2, 8, HjwsIconEnum.J, 300),
	B10(2, 9, HjwsIconEnum.T10, 400),
	B11(2, 10, HjwsIconEnum.T9, 400),
	B12(2, 11, HjwsIconEnum.SCATTER, 150),
	B13(2, 12, HjwsIconEnum.S1, 100),
	B14(2, 13, HjwsIconEnum.A1, 200),
	B15(2, 14, HjwsIconEnum.A2, 200),
	B16(2, 15, HjwsIconEnum.A3, 200),
	B17(2, 16, HjwsIconEnum.A4, 200),
	B18(2, 17, HjwsIconEnum.A, 300),
	B19(2, 18, HjwsIconEnum.K, 300),
	B20(2, 19, HjwsIconEnum.Q, 300),
	B21(2, 20, HjwsIconEnum.J, 300),
	B22(2, 21, HjwsIconEnum.T10, 400),
	B23(2, 21, HjwsIconEnum.T9, 400),
	B24(2, 22, HjwsIconEnum.WILD, 200),
	
	//C12(3, 11, HjwsIconEnum.SCATTER, 500),
	C1(3, 0, HjwsIconEnum.S1, 100),
	C2(3, 1, HjwsIconEnum.A1, 200),
	C3(3, 2, HjwsIconEnum.A2, 200),
	C4(3, 3, HjwsIconEnum.A3, 200),
	C5(3, 4, HjwsIconEnum.A4, 200),
	C6(3, 5, HjwsIconEnum.A, 300),
	C7(3, 6, HjwsIconEnum.K, 300),
	C8(3, 7, HjwsIconEnum.Q, 300),
	C9(3, 8, HjwsIconEnum.J, 300),
	C10(3, 9, HjwsIconEnum.T10, 400),
	C11(3, 10, HjwsIconEnum.T9, 400),
	C12(3, 11, HjwsIconEnum.SCATTER, 150),
	C13(3, 12, HjwsIconEnum.S1, 100),
	C14(3, 13, HjwsIconEnum.A1, 200),
	C15(3, 14, HjwsIconEnum.A2, 200),
	C16(3, 15, HjwsIconEnum.A3, 200),
	C17(3, 16, HjwsIconEnum.A4, 200),
	C18(3, 17, HjwsIconEnum.A, 300),
	C19(3, 18, HjwsIconEnum.K, 300),
	C20(3, 19, HjwsIconEnum.Q, 300),
	C21(3, 20, HjwsIconEnum.J, 300),
	C22(3, 21, HjwsIconEnum.T10, 400),
	C23(3, 21, HjwsIconEnum.T9, 400),
	C24(3, 22, HjwsIconEnum.WILD, 200),
	
	//D12(4, 11, HjwsIconEnum.SCATTER, 500),
	D1(4, 0, HjwsIconEnum.S1, 100),
	D2(4, 1, HjwsIconEnum.A1, 200),
	D3(4, 2, HjwsIconEnum.A2, 200),
	D4(4, 3, HjwsIconEnum.A3, 200),
	D5(4, 4, HjwsIconEnum.A4, 200),
	D6(4, 5, HjwsIconEnum.A, 300),
	D7(4, 6, HjwsIconEnum.K, 300),
	D8(4, 7, HjwsIconEnum.Q, 300),
	D9(4, 8, HjwsIconEnum.J, 300),
	D10(4, 9, HjwsIconEnum.T10, 400),
	D11(4, 10, HjwsIconEnum.T9, 400),
	D12(4, 11, HjwsIconEnum.SCATTER, 150),
	D13(4, 12, HjwsIconEnum.S1, 100),
	D14(4, 13, HjwsIconEnum.A1, 200),
	D15(4, 14, HjwsIconEnum.A2, 200),
	D16(4, 15, HjwsIconEnum.A3, 200),
	D17(4, 16, HjwsIconEnum.A4, 200),
	D18(4, 17, HjwsIconEnum.A, 300),
	D19(4, 18, HjwsIconEnum.K, 300),
	D20(4, 19, HjwsIconEnum.Q, 300),
	D21(4, 20, HjwsIconEnum.J, 300),
	D22(4, 21, HjwsIconEnum.T10, 400),
	D23(4, 22, HjwsIconEnum.WILD, 200),
	
	//E12(5, 11, HjwsIconEnum.SCATTER, 500),
	E1(5, 0, HjwsIconEnum.S1, 100),
	E2(5, 1, HjwsIconEnum.A1, 200),
	E3(5, 2, HjwsIconEnum.A2, 200),
	E4(5, 3, HjwsIconEnum.A3, 200),
	E5(5, 4, HjwsIconEnum.A4, 200),
	E6(5, 5, HjwsIconEnum.A, 300),
	E7(5, 6, HjwsIconEnum.K, 300),
	E8(5, 7, HjwsIconEnum.Q, 300),
	E9(5, 8, HjwsIconEnum.J, 300),
	E10(5, 9, HjwsIconEnum.T10, 400),
	E11(5, 10, HjwsIconEnum.T9, 400),
	E12(5, 11, HjwsIconEnum.SCATTER, 150),
	E13(5, 12, HjwsIconEnum.S1, 100),
	E14(5, 13, HjwsIconEnum.A1, 200),
	E15(5, 14, HjwsIconEnum.A2, 200),
	E16(5, 15, HjwsIconEnum.A3, 200),
	E17(5, 16, HjwsIconEnum.A4, 200),
	E18(5, 17, HjwsIconEnum.A, 300),
	E19(5, 18, HjwsIconEnum.K, 300),
	E20(5, 19, HjwsIconEnum.Q, 300),
	E21(5, 20, HjwsIconEnum.J, 300),
	E22(5, 21, HjwsIconEnum.T10, 400),
	E23(5, 22, HjwsIconEnum.T9, 400),
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private HjwsIconEnum icon;
	//权重
	private int weight;
	
	private HjwsRollerWeightEnum_backup(int id, int index, HjwsIconEnum icon, int weight){
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

	public HjwsIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	public static HjwsRollerWeightEnum_backup random(int c) {
		List<HjwsRollerWeightEnum_backup> all = new ArrayList<HjwsRollerWeightEnum_backup>();
		for(HjwsRollerWeightEnum_backup e : values()){
			if(e.getId() == c){
				all.add(e);
			}
		}
		int total = 0;
		for(HjwsRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		for(HjwsRollerWeightEnum_backup e : all){
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
	public static WindowInfo windowInfo(int c,int d) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<HjwsRollerWeightEnum_backup> all = new ArrayList<HjwsRollerWeightEnum_backup>();
		for(HjwsRollerWeightEnum_backup e : values()){
			if(e.getId() == c+1){
				all.add(e);
			}
		}
		int total = 0;
		for(HjwsRollerWeightEnum_backup e : all){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		HjwsRollerWeightEnum_backup roller = null;
		Iterator<HjwsRollerWeightEnum_backup> it = all.iterator();
		while(it.hasNext()){
			HjwsRollerWeightEnum_backup e = it.next();
			int w = v + e.getWeight();
			if(num >= v && num <= w){
				roller = e;
				break;
			}else{
				v = w;
			}
		}
		ws.add(new WindowInfo(c, d, roller.getIcon()));
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
		return ws.get(0);
	}
}

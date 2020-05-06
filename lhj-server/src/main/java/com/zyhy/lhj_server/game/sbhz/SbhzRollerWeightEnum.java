/**
 * 
 */
package com.zyhy.lhj_server.game.sbhz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.game.gsgl.GsglRollerWeightEnum;
import com.zyhy.lhj_server.game.sbhz.poi.template.SbhzOdds;
import com.zyhy.lhj_server.game.yzhx.YzhxIconEnum;
import com.zyhy.lhj_server.game.yzhx.poi.template.YzhxOdds;

/**
 * @author ASUS
 * 轮子权重
 */
public enum SbhzRollerWeightEnum {
	
	A1(1, 1, SbhzIconEnum.A5, 200),A2(1, 1, SbhzIconEnum.A2, 200),A3(1, 1, SbhzIconEnum.Monkey, 200),A4(1, 1, SbhzIconEnum.A3, 200),
	A5(1, 1, SbhzIconEnum.A4, 200),A6(1, 1, SbhzIconEnum.A5, 200),A7(1, 1, SbhzIconEnum.A2, 200),A8(1, 1, SbhzIconEnum.Sun, 200),
	A9(1, 1, SbhzIconEnum.A3, 200),A10(1, 1, SbhzIconEnum.A4, 200),A11(1, 1, SbhzIconEnum.A5, 200),A12(1, 1, SbhzIconEnum.A2, 200),
	A13(1, 1, SbhzIconEnum.Monkey, 200),A14(1, 1, SbhzIconEnum.A3, 200),A15(1, 1, SbhzIconEnum.A4, 200),A16(1, 1, SbhzIconEnum.A5, 200),
	A17(1, 1, SbhzIconEnum.A2, 200),A18(1, 1, SbhzIconEnum.Monkey, 200),A19(1, 1, SbhzIconEnum.A3, 200),A20(1, 1, SbhzIconEnum.A4, 200),
	A21(1, 1, SbhzIconEnum.A5, 200),A22(1, 1, SbhzIconEnum.A2, 200),A23(1, 1, SbhzIconEnum.Sun, 200),A24(1, 1, SbhzIconEnum.A3, 200),
	A25(1, 1, SbhzIconEnum.A4, 200),A26(1, 1, SbhzIconEnum.A5, 200),A27(1, 1, SbhzIconEnum.A2, 200),A28(1, 1, SbhzIconEnum.Sun, 200),
	A29(1, 1, SbhzIconEnum.A3, 200),A30(1, 1, SbhzIconEnum.A4, 200),A31(1, 1, SbhzIconEnum.A5, 200),A32(1, 1, SbhzIconEnum.A2, 200),
	A33(1, 1, SbhzIconEnum.A1, 200),A34(1, 1, SbhzIconEnum.A3, 200),A35(1, 1, SbhzIconEnum.A4, 200),A36(1, 1, SbhzIconEnum.A5, 200),
	A37(1, 1, SbhzIconEnum.A2, 200),A38(1, 1, SbhzIconEnum.A1, 200),A39(1, 1, SbhzIconEnum.A3, 200),A40(1, 1, SbhzIconEnum.A4, 200),
	A41(1, 1, SbhzIconEnum.A5, 200),A42(1, 1, SbhzIconEnum.A2, 200),A43(1, 1, SbhzIconEnum.S1, 200),A44(1, 1, SbhzIconEnum.A3, 200),
	A45(1, 1, SbhzIconEnum.A4, 200),A46(1, 1, SbhzIconEnum.A5, 200),A47(1, 1, SbhzIconEnum.Monkey, 200),A48(1, 1, SbhzIconEnum.S1, 200),
	A49(1, 1, SbhzIconEnum.A1, 200),A50(1, 1, SbhzIconEnum.A4, 200),A51(1, 1, SbhzIconEnum.A5, 200),A52(1, 1, SbhzIconEnum.S1, 200),
	A53(1, 1, SbhzIconEnum.A1, 200),A54(1, 1, SbhzIconEnum.A3, 200),A55(1, 1, SbhzIconEnum.S1, 200),A56(1, 1, SbhzIconEnum.A5, 200),
	A57(1, 1, SbhzIconEnum.A1, 200),A58(1, 1, SbhzIconEnum.Sun, 200),A59(1, 1, SbhzIconEnum.Monkey, 200),A60(1, 1, SbhzIconEnum.S1, 200),
	A61(1, 1, SbhzIconEnum.A1, 200),A62(1, 1, SbhzIconEnum.Sun, 200),A63(1, 1, SbhzIconEnum.Monkey, 200),A64(1, 1, SbhzIconEnum.A1, 200),
	A65(1, 1, SbhzIconEnum.S1, 200),A66(1, 1, SbhzIconEnum.Sun, 200),A67(1, 1, SbhzIconEnum.A4, 200),A68(1, 1, SbhzIconEnum.S1, 200),
	A69(1, 1, SbhzIconEnum.A1, 200),A70(1, 1, SbhzIconEnum.Sun, 200),A71(1, 1, SbhzIconEnum.S1, 200),A72(1, 1, SbhzIconEnum.A1, 200),
	
	B1(2, 1, SbhzIconEnum.A5, 200),B2(2, 1, SbhzIconEnum.A4, 200),B3(2, 1, SbhzIconEnum.A3, 200),B4(2, 1, SbhzIconEnum.A2, 200),
	B5(2, 1, SbhzIconEnum.A1, 200),B6(2, 1, SbhzIconEnum.S1, 200),B7(2, 1, SbhzIconEnum.A5, 200),B8(2, 1, SbhzIconEnum.A4, 200),
	B9(2, 1, SbhzIconEnum.A3, 200),B10(2, 1, SbhzIconEnum.A2, 200),B11(2, 1, SbhzIconEnum.A1, 200),B12(2, 1, SbhzIconEnum.Monkey, 200),
	B13(2, 1, SbhzIconEnum.S1, 200),B14(2, 1, SbhzIconEnum.Sun, 200),B15(2, 1, SbhzIconEnum.A5, 200),B16(2, 1, SbhzIconEnum.A4, 200),
	B17(2, 1, SbhzIconEnum.A3, 200),B18(2, 1, SbhzIconEnum.A2, 200),B19(2, 1, SbhzIconEnum.A1, 200),B20(2, 1, SbhzIconEnum.Sun, 200),
	B21(2, 1, SbhzIconEnum.S1, 200),B22(2, 1, SbhzIconEnum.Monkey, 200),B23(2, 1, SbhzIconEnum.A5, 200),B24(2, 1, SbhzIconEnum.A4, 200),
	B25(2, 1, SbhzIconEnum.A3, 200),B26(2, 1, SbhzIconEnum.A2, 200),B27(2, 1, SbhzIconEnum.A1, 200),B28(2, 1, SbhzIconEnum.Sun, 200),
	B29(2, 1, SbhzIconEnum.S1, 200),B30(2, 1, SbhzIconEnum.Monkey, 200),B31(2, 1, SbhzIconEnum.S1, 200),B32(2, 1, SbhzIconEnum.A5, 200),
	B33(2, 1, SbhzIconEnum.A4, 200),B34(2, 1, SbhzIconEnum.A3, 200),B35(2, 1, SbhzIconEnum.A2, 200),B36(2, 1, SbhzIconEnum.A1, 200),
	B37(2, 1, SbhzIconEnum.Sun, 200),B38(2, 1, SbhzIconEnum.S1, 200),B39(2, 1, SbhzIconEnum.A5, 200),B40(2, 1, SbhzIconEnum.A4, 200),
	B41(2, 1, SbhzIconEnum.A3, 200),B42(2, 1, SbhzIconEnum.A2, 200),B43(2, 1, SbhzIconEnum.A1, 200),B44(2, 1, SbhzIconEnum.Monkey, 200),
	B45(2, 1, SbhzIconEnum.A5, 200),B46(2, 1, SbhzIconEnum.A4, 200),B47(2, 1, SbhzIconEnum.A3, 200),B48(2, 1, SbhzIconEnum.A2, 200),
	B49(2, 1, SbhzIconEnum.A1, 200),B50(2, 1, SbhzIconEnum.A5, 200),B51(2, 1, SbhzIconEnum.A4, 200),B52(2, 1, SbhzIconEnum.A3, 200),
	B53(2, 1, SbhzIconEnum.A2, 200),B54(2, 1, SbhzIconEnum.A1, 200),B55(2, 1, SbhzIconEnum.Sun, 200),B56(2, 1, SbhzIconEnum.S1, 200),
	B57(2, 1, SbhzIconEnum.Sun, 200),B58(2, 1, SbhzIconEnum.A5, 200),B59(2, 1, SbhzIconEnum.A4, 200),B60(2, 1, SbhzIconEnum.A3, 200),
	B61(2, 1, SbhzIconEnum.A2, 200),B62(2, 1, SbhzIconEnum.A1, 200),B63(2, 1, SbhzIconEnum.Monkey, 200),B64(2, 1, SbhzIconEnum.A5, 200),
	B65(2, 1, SbhzIconEnum.A4, 200),B66(2, 1, SbhzIconEnum.Sun, 200),B67(2, 1, SbhzIconEnum.S1, 200),B68(2, 1, SbhzIconEnum.A3, 200),
	B69(2, 1, SbhzIconEnum.A2, 200),B70(2, 1, SbhzIconEnum.A1, 200),B71(2, 1, SbhzIconEnum.A5, 200),B72(2, 1, SbhzIconEnum.Monkey, 200),
	
	C1(3, 1, SbhzIconEnum.A4, 200),C2(3, 1, SbhzIconEnum.A2, 200),C3(3, 1, SbhzIconEnum.A5, 200),C4(3, 1, SbhzIconEnum.A1, 200),
	C5(3, 1, SbhzIconEnum.A3, 200),C6(3, 1, SbhzIconEnum.Monkey, 200),C7(3, 1, SbhzIconEnum.A5, 200),C8(3, 1, SbhzIconEnum.A4, 200),
	C9(3, 1, SbhzIconEnum.A2, 200),C10(3, 1, SbhzIconEnum.Sun, 200),C11(3, 1, SbhzIconEnum.A1, 200),C12(3, 1, SbhzIconEnum.A5, 200),
	C13(3, 1, SbhzIconEnum.A3, 200),C14(3, 1, SbhzIconEnum.Monkey, 200),C15(3, 1, SbhzIconEnum.Sun, 200),C16(3, 1, SbhzIconEnum.A1, 200),
	C17(3, 1, SbhzIconEnum.A5, 200),C18(3, 1, SbhzIconEnum.A4, 200),C19(3, 1, SbhzIconEnum.A2, 200),C20(3, 1, SbhzIconEnum.S1, 200),
	C21(3, 1, SbhzIconEnum.Monkey, 200),C22(3, 1, SbhzIconEnum.A3, 200),C23(3, 1, SbhzIconEnum.Sun, 200),C24(3, 1, SbhzIconEnum.A1, 200),
	C25(3, 1, SbhzIconEnum.A5, 200),C26(3, 1, SbhzIconEnum.A4, 200),C27(3, 1, SbhzIconEnum.A2, 200),C28(3, 1, SbhzIconEnum.Monkey, 200),
	C29(3, 1, SbhzIconEnum.S1, 200),C30(3, 1, SbhzIconEnum.A3, 200),C31(3, 1, SbhzIconEnum.A4, 200),C32(3, 1, SbhzIconEnum.A5, 200),
	C33(3, 1, SbhzIconEnum.A2, 200),C34(3, 1, SbhzIconEnum.A1, 200),C35(3, 1, SbhzIconEnum.Monkey, 200),C36(3, 1, SbhzIconEnum.A5, 200),
	C37(3, 1, SbhzIconEnum.A3, 200),C38(3, 1, SbhzIconEnum.S1, 200),C39(3, 1, SbhzIconEnum.A1, 200),C40(3, 1, SbhzIconEnum.A5, 200),
	C41(3, 1, SbhzIconEnum.A4, 200),C42(3, 1, SbhzIconEnum.A2, 200),C43(3, 1, SbhzIconEnum.A1, 200),C44(3, 1, SbhzIconEnum.Sun, 200),
	C45(3, 1, SbhzIconEnum.A5, 200),C46(3, 1, SbhzIconEnum.A3, 200),C47(3, 1, SbhzIconEnum.Monkey, 200),C48(3, 1, SbhzIconEnum.S1, 200),
	C49(3, 1, SbhzIconEnum.A4, 200),C50(3, 1, SbhzIconEnum.A2, 200),C51(3, 1, SbhzIconEnum.Sun, 200),C52(3, 1, SbhzIconEnum.A5, 200),
	C53(3, 1, SbhzIconEnum.A3, 200),C54(3, 1, SbhzIconEnum.Monkey, 200),C55(3, 1, SbhzIconEnum.A1, 200),C56(3, 1, SbhzIconEnum.A4, 200),
	C57(3, 1, SbhzIconEnum.A2, 200),C58(3, 1, SbhzIconEnum.S1, 200),C59(3, 1, SbhzIconEnum.A5, 200),C60(3, 1, SbhzIconEnum.A3, 200),
	C61(3, 1, SbhzIconEnum.Sun, 200),C62(3, 1, SbhzIconEnum.S1, 200),C63(3, 1, SbhzIconEnum.A4, 200),C64(3, 1, SbhzIconEnum.A2, 200),
	C65(3, 1, SbhzIconEnum.A3, 200),C66(3, 1, SbhzIconEnum.S1, 200),C67(3, 1, SbhzIconEnum.A1, 200),C68(3, 1, SbhzIconEnum.A3, 200),
	C69(3, 1, SbhzIconEnum.S1, 200),C70(3, 1, SbhzIconEnum.Sun, 200),C71(3, 1, SbhzIconEnum.A2, 200),C72(3, 1, SbhzIconEnum.A4, 200),
	

	/*A1(1, 0, SbhzIconEnum.Sun, 100),
	A2(1, 1, SbhzIconEnum.S1, 100),
	A3(1, 2, SbhzIconEnum.Monkey, 50),
	A4(1, 3, SbhzIconEnum.A1, 60),
	A5(1, 4, SbhzIconEnum.A2, 60),
	A6(1, 5, SbhzIconEnum.A3, 60),
	A7(1, 6, SbhzIconEnum.A4, 60),
	A8(1, 7, SbhzIconEnum.A5, 60),
	
	
	B1(2, 0, SbhzIconEnum.Sun, 100),
	B2(2, 1, SbhzIconEnum.S1, 100),
	B3(2, 2, SbhzIconEnum.Monkey, 50),
	B4(2, 3, SbhzIconEnum.A1, 60),
	B5(2, 4, SbhzIconEnum.A2, 60),
	B6(2, 5, SbhzIconEnum.A3, 60),
	B7(2, 6, SbhzIconEnum.A4, 60),
	B8(2, 7, SbhzIconEnum.A5, 60),
	
	C1(3, 0, SbhzIconEnum.Sun, 100),
	C2(3, 1, SbhzIconEnum.S1, 100),
	C3(3, 2, SbhzIconEnum.Monkey, 50),
	C4(3, 3, SbhzIconEnum.A1, 60),
	C5(3, 4, SbhzIconEnum.A2, 60),
	C6(3, 5, SbhzIconEnum.A3, 60),
	C7(3, 6, SbhzIconEnum.A4, 60),
	C8(3, 7, SbhzIconEnum.A5, 60),*/
	
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private SbhzIconEnum icon;
	//权重
	private int weight;
	
	private SbhzRollerWeightEnum(int id, int index, SbhzIconEnum icon, int weight){
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

	public SbhzIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}
	
	/**
	 * 获取图标
	 * @param all 
	 * @param i
	 * @return
	 */
	public static Collection<? extends Window> windowInfo(int c, List<SbhzOdds> all) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<SbhzOdds> col = new ArrayList<>();
		List<SbhzIconEnum> icons = new ArrayList<>();
		// 取出一列的图标
		for (SbhzOdds odds : all) {
			if(odds.getCol() == c){
				col.add(odds);
			}
		}
		//System.out.println("col" + col);
		for (SbhzOdds odds : col) {
			SbhzIconEnum icon = null;
			for (SbhzIconEnum e : SbhzIconEnum.values()) {
				if (e.getName().equalsIgnoreCase(odds.getName())) {
					icon = e;
				}
			}
			icons.add(icon);
		}
		//System.out.println("icons" + icons);
		
		// 根据随机数取图标
		int random = RandomUtil.getRandom(0, icons.size() - 1);
		for (int i = 1; i <= 3; i++) {
			SbhzIconEnum icon = icons.get(random);
			ws.add(new WindowInfo(c, i, icon));
			random ++ ;
			if (random > (icons.size() - 1)) {
				random = 0;
			}
		}
		//System.out.println("本次取到的图标:" + ws);
		return ws;
	}
	
	/**
	 * 重转中获取图标
	 * @param all 
	 * @param i
	 * @return
	 */
	public static Collection<? extends Window> mokeyWindowInfo(int c,int random, List<SbhzOdds> all) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<SbhzOdds> col = new ArrayList<>();
		List<SbhzIconEnum> icons = new ArrayList<>();
		// 取出一列的图标
		for (SbhzOdds odds : all) {
			if(odds.getCol() == c){
				col.add(odds);
			}
		}
		//System.out.println("col" + col);
		for (SbhzOdds odds : col) {
			SbhzIconEnum icon = null;
			for (SbhzIconEnum e : SbhzIconEnum.values()) {
				if (e.getName().equalsIgnoreCase(odds.getName())) {
					icon = e;
				}
			}
			icons.add(icon);
		}
		//System.out.println("icons" + icons);
		
		int random1 = RandomUtil.getRandom(0, icons.size() - 1);
		SbhzIconEnum icon = icons.get(random1);
		
		if(random <= 10000){
			ws.add(new WindowInfo(c, 1, icon));
			ws.add(new WindowInfo(c, 2, icon));
			ws.add(new WindowInfo(c, 3, icon));
		}else if(random > 10000 && random <= 20000){
			ws.add(new WindowInfo(c, 1, icon));
			ws.add(new WindowInfo(c, 2, icon));
			if (random1 + 1 > (icons.size() - 1)) {
				random1 = 0;
			} else {
				random1 ++;
			}
			ws.add(new WindowInfo(c, 3, icons.get(random1)));
		}else{
			ws.add(new WindowInfo(c, 1, icon));
			if (random1 + 1 > (icons.size() - 1)) {
				random1 = 0;
			} else {
				random1 ++;
			}
			SbhzIconEnum icon2 = icons.get(random1);
			ws.add(new WindowInfo(c, 2, icon2));
			ws.add(new WindowInfo(c, 3, icon2));
		}
		
		int count = 0;
		for (WindowInfo wi : ws) {
			if (wi.getIcon() == SbhzIconEnum.Monkey) {
				count ++;
			}
		}
		
		if (count > 0 && count < 3) {
			return mokeyWindowInfo(c,random,all);
		}
		return ws;
	}
}

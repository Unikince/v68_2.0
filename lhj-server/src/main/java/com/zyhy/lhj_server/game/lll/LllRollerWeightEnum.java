/**
 * 
 */
package com.zyhy.lhj_server.game.lll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.game.gghz.GghzRollerInfo;
import com.zyhy.lhj_server.game.gghz.GghzWindowInfo;
import com.zyhy.lhj_server.game.lll.poi.template.LllOdds;

/**
 * @author ASUS
 * 轮子权重
 */
public enum LllRollerWeightEnum {

	A1(1, 1, LllIconEnum.T1, 4),
	A2(1, 2, null, 8),
	A3(1, 3, LllIconEnum.T2, 5),
	A4(1, 4, null, 8),
	A5(1, 5, LllIconEnum.T3, 6),
	A6(1, 6, null, 8),
	A7(1, 7, LllIconEnum.T1, 4),
	A8(1, 8, null, 8),
	A9(1, 9, LllIconEnum.T2, 5),
	A10(1, 10, null, 10),
	A11(1, 11, LllIconEnum.T3, 6),
	
	B1(2, 1, LllIconEnum.T1, 4),
	B2(2, 2, null, 8),
	B3(2, 3, LllIconEnum.T2, 5),
	B4(2, 4, null, 8),
	B5(2, 5, LllIconEnum.T3, 6),
	B6(2, 6, null, 8),
	B7(2, 7, LllIconEnum.T1, 4),
	B8(2, 8, null, 8),
	B9(2, 9, LllIconEnum.T2, 5),
	B10(2, 10, null, 10),
	B11(2, 11, LllIconEnum.T3, 6),

	C1(3, 1, LllIconEnum.T1, 4),
	C2(3, 2, null, 8),
	C3(3, 3, LllIconEnum.T2, 5),
	C4(3, 4, null, 8),
	C5(3, 5, LllIconEnum.T3, 6),
	C6(3, 6, null, 8),
	C7(3, 7, LllIconEnum.T1, 5),
	C8(3, 8, null, 8),
	C9(3, 9, LllIconEnum.T2, 5),
	C10(3, 10, null, 9),
	C11(3, 11, LllIconEnum.T3, 6),
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private LllIconEnum icon;
	//权重
	private int weight;
	
	private LllRollerWeightEnum(int id, int index, LllIconEnum icon, int weight){
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

	public LllIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	/**
	 * 获取
	 * @param i
	 * @return
	 */
	public static Collection<? extends LllWindowInfo> windowInfo(int c, List<LllOdds> all) {
		
		List<LllWindowInfo> ws = new ArrayList<LllWindowInfo>();
		List<LllRollerWeightEnum> rollers = new ArrayList<>();
		List<LllOdds> col = new ArrayList<>();
		List<IconInfo> icons = new ArrayList<>();
		// 取出一列的图标
		for (LllOdds odds : all) {
			if(odds.getCol() == c){
				col.add(odds);
			}
		}
		//System.out.println("col" + col);
		for (LllOdds odds : col) {
			LllIconEnum icon = null;
			for (LllIconEnum e : LllIconEnum.values()) {
				if (e.getName().equalsIgnoreCase(odds.getName())) {
					icon = e;
					break;
				}
			}
			IconInfo info = new IconInfo();
			info.setIcon(icon);
			info.setWeight(odds.getWeight());
			icons.add(info);
		}
		
		for (LllRollerWeightEnum roller : values()) {
			if (roller.getId() == c) {
				rollers.add(roller);
			}
		}
		
		for (LllRollerWeightEnum roller : rollers) {
			Iterator<IconInfo> iterator = icons.iterator();
			if (iterator.hasNext()) {
				IconInfo next = iterator.next();
				roller.setIcon(next.getIcon());
				roller.setWeight(next.getWeight());
				iterator.remove();
			}
		}
		
		int total = 0;
		for(LllRollerWeightEnum e : rollers){
			total += e.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		LllRollerWeightEnum roller1 = null;
		LllRollerWeightEnum roller2 = null;
		LllRollerWeightEnum roller3 = null;
		Iterator<LllRollerWeightEnum> it = rollers.iterator();
		while(it.hasNext()){
			LllRollerWeightEnum e = it.next();
			int w = v + e.getWeight();
			if(num >= v && num <= w){
				roller2 = e;
				break;
			}else{
				roller1 = e;
				v = w;
			}
		}
		if(roller1 == null){
			roller1 = rollers.get(rollers.size() - 1);
		}
		if(it.hasNext()){
			roller3 = it.next();
		}else{
			roller3 = rollers.get(0);
		}
		
		
		if ("NULL".equals(roller1.getIcon().getName())) {
			ws.add(new LllWindowInfo(c, 1, 
					new LllRollerInfo(roller1.getId(),roller1.getIndex(),null)));
		} else {
			ws.add(new LllWindowInfo(c, 1, 
					new LllRollerInfo(roller1.getId(),roller1.getIndex(),roller1.getIcon())));
		}
		
		if ("NULL".equals(roller2.getIcon().getName())) {
			ws.add(new LllWindowInfo(c, 2, 
					new LllRollerInfo(roller2.getId(),roller2.getIndex(),null)));
		} else {
			ws.add(new LllWindowInfo(c, 2, 
					new LllRollerInfo(roller2.getId(),roller2.getIndex(),roller2.getIcon())));
		}
		
		if ("NULL".equals(roller3.getIcon().getName())) {
			ws.add(new LllWindowInfo(c, 3, 
					new LllRollerInfo(roller3.getId(),roller3.getIndex(),null)));
		} else {
			ws.add(new LllWindowInfo(c, 3, 
					new LllRollerInfo(roller3.getId(),roller3.getIndex(),roller3.getIcon())));
		}
		//System.out.println("本列取回的图标" + ws);
		return ws;
	}

	public void setIcon(LllIconEnum icon) {
		this.icon = icon;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
}

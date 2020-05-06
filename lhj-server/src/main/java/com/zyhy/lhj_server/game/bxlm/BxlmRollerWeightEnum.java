/**
 * 
 */
package com.zyhy.lhj_server.game.bxlm;

import java.util.ArrayList;
import java.util.List;

import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.game.bxlm.poi.template.BxlmOdds;

/**
 * @author ASUS
 * 轮子权重
 */
public enum BxlmRollerWeightEnum {
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private BxlmIconEnum icon;
	//权重
	private int weight;
	
	private BxlmRollerWeightEnum(int id, int index, BxlmIconEnum icon, int weight){
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

	public BxlmIconEnum getIcon() {
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
	public static WindowInfo windowInfo(int c,int d,List<WindowInfo> list, List<BxlmOdds> all) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<BxlmOdds> col = new ArrayList<>();
		List<BxlmIconEnum> icons = new ArrayList<>();
		// 取出一列的图标
		for (BxlmOdds odds : all) {
			if(odds.getCol() == c + 1){
				col.add(odds);
			}
		}
		//System.out.println("col" + col);
		for (BxlmOdds odds : col) {
			BxlmIconEnum icon = null;
			for (BxlmIconEnum e : BxlmIconEnum.values()) {
				if (e.getName().equalsIgnoreCase(odds.getName())) {
					icon = e;
				}
			}
			icons.add(icon);
		}
		//System.out.println("icons" + icons);
		
		// 根据随机数取图标
		int random = RandomUtil.getRandom(0, icons.size() - 1);
		BxlmIconEnum icon = icons.get(random);
		
		if (list.size() == 0) {
			ws.add(new WindowInfo(c, d, icon));
		} else {
			int scatter = 0;
			for (WindowInfo wi : list) {
				if (wi.getIcon().isScatter()) {
					scatter ++;
				}
			}	
			if (scatter > 0 && icon.isScatter()) {
				ws.clear();
				ws.add(windowInfo(c,d,list,all));
			} else {
				ws.clear();
				ws.add(new WindowInfo(c, d, icon));
			}
		}
		//System.out.println("本次取到的图标:" + ws);
		return ws.get(0);
	}
}

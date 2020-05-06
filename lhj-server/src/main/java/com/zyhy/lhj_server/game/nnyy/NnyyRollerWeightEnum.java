/**
 * 
 */
package com.zyhy.lhj_server.game.nnyy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.game.nnyy.poi.template.NnyyOdds;

/**
 * @author ASUS
 * 轮子权重
 */
public enum NnyyRollerWeightEnum {
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private NnyyIconEnum icon;
	//权重
	private int weight;
	
	private NnyyRollerWeightEnum(int id, int index, NnyyIconEnum icon, int weight){
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

	public NnyyIconEnum getIcon() {
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
	public static Collection<? extends Window> windowInfo(int c, List<NnyyOdds> all) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<NnyyOdds> col = new ArrayList<>();
		List<NnyyIconEnum> icons = new ArrayList<>();
		// 取出一列的图标
		for (NnyyOdds odds : all) {
			if(odds.getCol() == c){
				col.add(odds);
			}
		}
		//System.out.println("col" + col);
		for (NnyyOdds odds : col) {
			NnyyIconEnum icon = null;
			for (NnyyIconEnum e : NnyyIconEnum.values()) {
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
			NnyyIconEnum icon = icons.get(random);
			ws.add(new WindowInfo(c, i, icon));
			random ++ ;
			if (random > (icons.size() - 1)) {
				random = 0;
			}
		}
		//System.out.println("本次取到的图标:" + ws);
		return ws;
	}
}

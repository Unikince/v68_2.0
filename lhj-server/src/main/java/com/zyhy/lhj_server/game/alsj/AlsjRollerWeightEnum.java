/**
 * 
 */
package com.zyhy.lhj_server.game.alsj;

import java.util.ArrayList;
import java.util.List;

import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.game.alsj.poi.template.AlsjOdds;

/**
 * @author ASUS
 * 轮子权重
 */
public enum AlsjRollerWeightEnum {
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private AlsjIconEnum icon;
	//权重
	private int weight;
	
	private AlsjRollerWeightEnum(int id, int index, AlsjIconEnum icon, int weight){
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

	public AlsjIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}


	/**
	 * 获取
	 * @param j 
	 * @param i
	 * @return
	 */
	public static List<WindowInfo> windowInfo(int c, List<AlsjOdds> all) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<AlsjOdds> col = new ArrayList<>();
		List<AlsjIconEnum> icons = new ArrayList<>();
		// 取出一列的图标
		for (AlsjOdds odds : all) {
			if(odds.getCol() == c){
				col.add(odds);
			}
		}
		//System.out.println("col" + col);
		for (AlsjOdds odds : col) {
			AlsjIconEnum icon = null;
			for (AlsjIconEnum e : AlsjIconEnum.values()) {
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
			AlsjIconEnum icon = icons.get(random);
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
	 * 获取指定位置图标,排除指定图标
	 * @param i
	 * @return
	 */
	public static WindowInfo windowInfo(int c,int d,List<WindowInfo> list, List<AlsjOdds> all) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<AlsjOdds> col = new ArrayList<>();
		List<AlsjIconEnum> icons = new ArrayList<>();
		// 取出一列的图标
		for (AlsjOdds odds : all) {
			if(odds.getCol() == c + 1){
				col.add(odds);
			}
		}
		//System.out.println("col" + col);
		for (AlsjOdds odds : col) {
			AlsjIconEnum icon = null;
			for (AlsjIconEnum e : AlsjIconEnum.values()) {
				if (e.getName().equalsIgnoreCase(odds.getName())) {
					icon = e;
				}
			}
			icons.add(icon);
		}
		//System.out.println("icons" + icons);
		
		// 根据随机数取图标
		int random = RandomUtil.getRandom(0, icons.size() - 1);
		AlsjIconEnum icon = icons.get(random);
		
		if (list.size() == 0) {
			ws.add(new WindowInfo(c, d, icon));
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getIcon() == icon 
						&& icon == AlsjIconEnum.SCATTER) {
					ws.clear();
					ws.add(windowInfo(c,d,list,all));
				}else {
					ws.clear();
					ws.add(new WindowInfo(c, d, icon));
				}
			}
		}
		return ws.get(0);
	}
}

/**
 * 
 */
package com.zyhy.lhj_server.game.hjws;

import java.util.ArrayList;
import java.util.List;

import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.game.hjws.poi.template.HjwsOdds;

/**
 * @author ASUS
 * 轮子权重
 */
public enum HjwsRollerWeightEnum {
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private HjwsIconEnum icon;
	//权重
	private int weight;
	
	private HjwsRollerWeightEnum(int id, int index, HjwsIconEnum icon, int weight){
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

	/**
	 * 获取
	 * @param i
	 * @return
	 */
	public static WindowInfo windowInfo(int c,int d, List<WindowInfo> list, List<HjwsOdds> all) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<HjwsOdds> col = new ArrayList<>();
		List<HjwsIconEnum> icons = new ArrayList<>();
		// 取出一列的图标
		for (HjwsOdds odds : all) {
			if(odds.getCol() == c + 1){
				col.add(odds);
			}
		}
		//System.out.println("col" + col);
		for (HjwsOdds odds : col) {
			HjwsIconEnum icon = null;
			for (HjwsIconEnum e : HjwsIconEnum.values()) {
				if (e.getName().equalsIgnoreCase(odds.getName())) {
					icon = e;
				}
			}
			icons.add(icon);
		}
		//System.out.println("icons" + icons);
		
		// 根据随机数取图标
		int random = RandomUtil.getRandom(0, icons.size() - 1);
		HjwsIconEnum icon = icons.get(random);
		//System.out.println("本次取到的图标:" + ws);
		
		if (list.size() == 0) {
			ws.add(new WindowInfo(c, d, icon));
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getIcon() == icon 
						&& icon == HjwsIconEnum.SCATTER) {
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

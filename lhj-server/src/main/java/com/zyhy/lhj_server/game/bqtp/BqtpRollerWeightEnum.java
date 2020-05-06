/**
 * 
 */
package com.zyhy.lhj_server.game.bqtp;

import java.util.ArrayList;
import java.util.List;

import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.game.bqtp.poi.template.BqtpOdds;

/**
 * @author ASUS
 * 轮子权重
 */
public enum BqtpRollerWeightEnum {
	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private BqtpIconEnum icon;
	//权重
	private int weight;
	
	private BqtpRollerWeightEnum(int id, int index, BqtpIconEnum icon, int weight){
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

	/**
	 * 获取
	 * @param i
	 * @return
	 */
	public static WindowInfo windowInfo(int c,int d,List<WindowInfo> list, List<BqtpOdds> all) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<BqtpOdds> col = new ArrayList<>();
		List<BqtpIconEnum> icons = new ArrayList<>();
		// 取出一列的图标
		for (BqtpOdds odds : all) {
			if(odds.getCol() == c + 1){
				col.add(odds);
			}
		}
		//System.out.println("col" + col);
		for (BqtpOdds odds : col) {
			BqtpIconEnum icon = null;
			for (BqtpIconEnum e : BqtpIconEnum.values()) {
				if (e.getName().equalsIgnoreCase(odds.getName())) {
					icon = e;
				}
			}
			icons.add(icon);
		}
		//System.out.println("icons" + icons);
		
		// 根据随机数取图标
		int random = RandomUtil.getRandom(0, icons.size() - 1);
		BqtpIconEnum icon = icons.get(random);
		
		if (list.size() == 0) {
			ws.add(new WindowInfo(c, d, icon));
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getIcon() == icon 
						&& icon == BqtpIconEnum.SCATTER) {
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

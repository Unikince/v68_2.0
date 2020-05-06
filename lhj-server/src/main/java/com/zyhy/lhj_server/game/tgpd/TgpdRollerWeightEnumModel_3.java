/**
 * 
 */
package com.zyhy.lhj_server.game.tgpd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.game.tgpd.poi.template.TgpdOdds;

/**
 * @author ASUS
 * 轮子权重
 */
public enum TgpdRollerWeightEnumModel_3 {

	;
	//轴
	private int id;
	//位置
	private int index;
	//图标
	private TgpdIconEnum icon;
	//权重
	private int weight;
	
	private TgpdRollerWeightEnumModel_3(int id, int index, TgpdIconEnum icon, int weight){
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

	public TgpdIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	/**
	 * 按指定轴和位置取图标,不重复取指定图标
	 * @param 
	 * @return
	 */
	public static WindowInfo windowInfo(int c,int d,List<WindowInfo> list,TgpdIconEnum icon,List<TgpdOdds> col) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		TgpdOdds roller = getIcon(col);
		if (roller == null) {
			roller = getIcon(col);
		}
		
		TgpdIconEnum te = null;
		for (TgpdIconEnum e : TgpdIconEnum.values()) {
			if (e.getName().equalsIgnoreCase(roller.getName())) {
				te = e;
			}
		}
		//System.out.println("te=======>" + te);
		
		if (list.size() == 0) {
			ws.add(new WindowInfo(c, d, te));
		} else {
			int number = 0;
			for (WindowInfo wi : list) {
				if (wi.getIcon() == icon) {
					number ++;
				}
			}	
			if (number > 0 && te == icon) {
				ws.clear();
				ws.add(windowInfo(c,d,list,icon,col));
			} else {
				ws.clear();
				ws.add(new WindowInfo(c, d, te));
			}
		} 
		return ws.get(0);
	}
	
	/**
	 * 获取图标
	 * @param col
	 * @return
	 */
	private static TgpdOdds getIcon(List<TgpdOdds> col){
		int total = 0;
		for(TgpdOdds tgpdOdds : col){
			total += tgpdOdds.getWeight();
		}
		int num = RandomUtil.getRandom(1, total);
		int v = 0;
		TgpdOdds roller = null;
		Iterator<TgpdOdds> it = col.iterator();
		while(it.hasNext()){
			TgpdOdds e = it.next();
			int w = v + e.getWeight();
			if(num >= v && num < w){
				roller = e;
				break;
			}else{
				v = w;
			}
		}
		//System.out.println("roller=====>" + roller);
		return roller;
	}
	/**
	 * 按指定轴和位置取图标,排除指定图标
	 * @param 
	 * @return
	 */
	public static WindowInfo windowInfo(int c,int d,TgpdIconEnum icon,List<TgpdOdds> col) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		
		TgpdOdds roller = getIcon(col);
		if (roller == null) {
			roller = getIcon(col);
		}
		
		TgpdIconEnum te = null;
		for (TgpdIconEnum e : TgpdIconEnum.values()) {
			if (e.getName().equalsIgnoreCase(roller.getName())) {
				te = e;
			}
		}
		//System.out.println("te=======>" + te);
		
		if (te != icon) {
			ws.add(new WindowInfo(c, d, te));
		} else {
			ws.add(windowInfo(c , d , icon, col));
		}
		return ws.get(0);
	}
}

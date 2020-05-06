/**
 * 
 */
package com.zyhy.lhj_server.game.gsgl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.game.gsgl.poi.template.GsglOdds;

/**
 * @author ASUS
 * 轮子权重
 */
public enum GsglRollerWeightEnum {
	;

	//轴
	private int id;
	//位置
	private int index;
	//图标
	private GsglIconEnum icon;
	//权重
	private int weight;
	
	private GsglRollerWeightEnum(int id, int index, GsglIconEnum icon, int weight){
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

	public GsglIconEnum getIcon() {
		return icon;
	}

	public int getWeight() {
		return weight;
	}

	/**
	 * 获取
	 * @param baseinfos 
	 * @param i
	 * @return
	 */
	public static Collection<? extends Window> windowInfo(int c, List<GsglOdds> all) {
		List<WindowInfo> ws = new ArrayList<WindowInfo>();
		List<GsglOdds> col = new ArrayList<>();
		for (GsglOdds gsglOdds : all) {
			if(gsglOdds.getCol() == c){
				col.add(gsglOdds);
			}
		}
		//System.out.println("获取到的轮子:" + col);
		int random = RandomUtil.getRandom(0, col.size() - 1);
		for (int i = 1; i <= 3; i++) {
			GsglIconEnum icon = null;
			GsglOdds gsglOdds = col.get(random);
			//System.out.println("要对比的图标:" + gsglOdds);
			for (GsglIconEnum e : GsglIconEnum.values()) {
				if (e.getName().equalsIgnoreCase(gsglOdds.getName())) {
					icon = e;
				}
			}
			ws.add(new WindowInfo(c, i, icon));
			random ++ ;
			if (random > (col.size() - 1)) {
				random = 0;
			}
		}
		//System.out.println("本次取到的图标:" + ws);
		return ws;
	}
}

package com.zyhy.lhj_server.game.xywjs;

import com.zyhy.common_server.util.RandomUtil;

public enum IconEnum {
	ICON1(0, "BAR", 2,3),
	ICON2(1, "双七", 14,15),
	ICON3(2, "双星", 19,20),
	ICON4(3, "西瓜", 7,8),
	ICON5(4, "铃铛", 1,13,23),
	ICON6(5, "芒果", 6,17,18),
	ICON7(6, "橙子", 11,12,24),
	ICON8(7, "苹果", 4,5,10,16,22),
	;
	
	// 图标唯一id
	private int id;
	// 名字
	private String name;
	// 所有图标
	private int[] icon;
	
	private IconEnum(int id, String name, int ... icon) {
		this.id = id;
		this.name = name;
		this.icon = icon;
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int[] getIcon() {
		return icon;
	}

	/**
	 * 从指定图标的序列中,随机取出一个图标
	 * @return
	 */
	public static int getIconIdByRandom(int id){
		for (IconEnum e : values()) {
			if (e.getId() == id) {
				int[] icons = e.getIcon();
				return icons[RandomUtil.getRandom(0, icons.length - 1)];
			}
		}
		return -1;
	}
	
	/**
	 * 根据图标id获取id所代表的图标
	 * @return
	 */
	public static int[] getIconsById(int id){
		for (IconEnum e : values()) {
			if (e.getId() == id) {
				return e.getIcon();
			}
		}
		return null;
	}
	
	/**
	 * 根据图标id获取id所代表的图标信息
	 * @return
	 */
	public static IconEnum getIconEnumById(int id){
		for (IconEnum e : values()) {
			int[] icons = e.getIcon();
			for (int i : icons) {
				if (i == id) {
					return e;
				}
			}
		}
		return null;
	}
	
	/**
	 * 通过图标获取下注id
	 * @return 
	 */
	public static int getBetIdByicon(int icon){
		for (IconEnum e : values()) {
			int[] icons = e.getIcon();
			for (int i : icons) {
				if (i == icon) {
					return e.getId();
				}
			}
		}
		return -1;
	}
	
}

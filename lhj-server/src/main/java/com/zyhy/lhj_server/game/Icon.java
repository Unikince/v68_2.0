/**
 * 
 */
package com.zyhy.lhj_server.game;

/**
 * @author ASUS
 *
 */
public interface Icon {

	/**
	 * 获取id
	 * @return
	 */
	public int getId();
	
	/**
	 *   获取名称
	 */
	public String getName();
	
	/**
	 * 獲取描述
	 * @return
	 */
	public String getDesc();
	
	/**
	 *   一倍
	 */
	public double getTr1();
	
	/**
	 * 2倍
	 */
	public double getTr2();
	
	/**
	 * 3倍
	 */
	public double getTr3();
}

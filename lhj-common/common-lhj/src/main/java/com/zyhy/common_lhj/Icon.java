/**
 * 
 */
package com.zyhy.common_lhj;

/**
 * @author ASUS
 *
 */
public interface Icon extends Bonus{

	/**
	 * 获取id
	 * @return
	 */
	public int getId();
	
	/**
	 * 获取名称
	 */
	public String getName();
	
	/**
	 * 獲取描述
	 * @return
	 */
	public String getDesc();
	
	/**
	 * 倍数
	 */
	public double[] getTrs();
	
	public boolean isScatter();
	
	public boolean isBar();
	
	public boolean isWild();
	
}

/**
 * 
 */
package com.zyhy.common_lhj.pool;

import com.zyhy.common_lhj.Weight;

/**
 * @author ASUS
 *
 */
public class DragonItem implements Weight{

	private String name;
	
	private int weight;
	
	public DragonItem(String name, int weight){
		this.name = name;
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "DragonItem [name=" + name + ", weight=" + weight + "]";
	}
	
}

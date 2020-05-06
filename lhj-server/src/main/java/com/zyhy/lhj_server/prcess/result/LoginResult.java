/**
 * 
 */
package com.zyhy.lhj_server.prcess.result;

import com.zyhy.common_server.result.HttpMessageResult;

/**
 * @author ASUS
 *
 */
public class LoginResult extends HttpMessageResult{

	private String uuid;
	
	private double gold;
	// 汇率
	private double exchangeRate; 
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public double getGold() {
		return gold;
	}

	public void setGold(double gold) {
		this.gold = gold;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	
	
}

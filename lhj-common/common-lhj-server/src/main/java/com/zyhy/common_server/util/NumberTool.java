/**
 * 
 */
package com.zyhy.common_server.util;

import java.math.BigDecimal;

/**
 * @author ASUS
 *
 */
public class NumberTool {

	public static BigDecimal add(Number a, Number b){
		return new BigDecimal(a.toString()).add(new BigDecimal(b.toString()));
	}
	
	public static BigDecimal subtract(Number a, Number b){
		return new BigDecimal(a.toString()).subtract(new BigDecimal(b.toString()));
	}
	
	public static BigDecimal multiply(Number a, Number b){
		return new BigDecimal(a.toString()).multiply(new BigDecimal(b.toString()));
	}
	
	public static BigDecimal divide(Number a, Number b){
		return new BigDecimal(a.toString()).divide(new BigDecimal(b.toString()),5,4);
	}

}

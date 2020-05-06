/**
 * 
 */
package com.zyhy.common_lhj;

import java.util.Iterator;
import java.util.List;

import com.zyhy.common_server.util.RandomUtil;

/**
 * @author ASUS
 *
 */
public class WeightUtils {

	@SuppressWarnings("unchecked")
	public static <T extends Weight> T random(List<T> ws){
		int total = 0;
		for(Weight w : ws){
			total += w.getWeight();
		}
		int num = RandomUtil.getRandom(0, total);
		int v = 0;
		Weight roller = null;
		Iterator<? extends Weight> it = ws.iterator();
		while(it.hasNext()){
			Weight e = it.next();
			int w = v + e.getWeight();
			if(num >= v && num < w){
				roller = e;
				break;
			}else{
				v = w;
			}
		}
		return (T) roller;
	}
}

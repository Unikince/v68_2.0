package com.zyhy.common_server.util;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @Description Json处理类
 * @author linanjun
 * @date 2012-12-25 下午04:52:34
 */

public class JsonUtils {
	
	public static <T> T parseObject(String text, Class<T> clazz){
		return JSON.parseObject(text, clazz);
	}
	
	public static <T> List<T> parseArray(String text, Class<T> clazz){
		return JSON.parseArray(text, clazz);
	}
	
	public static String object2json(Object obj){
		return JSON.toJSONString(obj);
	}
	
	public static JSONObject parseJsonObject(String text){
		return JSONObject.parseObject(text);
	}
}

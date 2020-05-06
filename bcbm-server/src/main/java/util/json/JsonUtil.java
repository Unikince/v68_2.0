package util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @Date: 2015年9月22日 上午11:21:32
 * @Author: zhuqd
 * @Description:
 */
public class JsonUtil {
	private static IntPropertyFilter filter = new IntPropertyFilter();

	/**
	 * 创建一个{@link Json}实例
	 * 
	 * @return
	 */
	public static Json create() {
		return new Json();
	}

	/**
	 * object to json stirng.<br>
	 * 禁用循环引用
	 * 
	 * @param object
	 * @return
	 */
	public static String toJSONString(Object object) {
		return JSON.toJSONString(object, filter, SerializerFeature.DisableCircularReferenceDetect);
	}
}

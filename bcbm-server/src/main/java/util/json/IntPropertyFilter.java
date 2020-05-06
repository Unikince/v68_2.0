package util.json;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.serializer.NameFilter;

/**
 * @author zhuqd
 * @Date 2017年8月10日
 * @Desc 将Map<Integer,Object>的数据格式化为Map<String,Object>
 */
public class IntPropertyFilter implements NameFilter {

	@SuppressWarnings("unchecked")
	@Override
	public String process(Object source, String name, Object value) {
		if (value instanceof Map) {
			Set<?> set = ((Map<?, ?>) value).keySet();
			boolean intKey = false;
			for (Object key : set) {
				if (key instanceof Integer) {
					intKey = true;
					break;
				}
			}
			if (intKey) {
				@SuppressWarnings("rawtypes")
				Map map = new HashMap();
				for (Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
					map.put("" + entry.getKey(), entry.getValue());
				}
				value = map;
			}
		}
		return name;
	}

}

package vequn.lobby.test;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * @author zhuqd
 * @Date 2017年8月22日
 * @Desc
 */
public class TestJsonPack {
	private MessagePack pack = new MessagePack();

	public void testEncode() {
		JSONObject json = new JSONObject();
		json.put("cmd", 1001);
		json.put("time", 100001213120L);
		json.put("name", "测试");
		//
		List<Integer> list = new ArrayList<>();
		for (int i = 1; i < 5; i++) {
			list.add(i);
		}
		json.put("list", list);
		//
		JSONObject object = new JSONObject();
		object.put("id", 10);
		object.put("sign", "safdnakjc7ci3w3c");
		json.put("user", object);
		//

		pack.encode(json);
		//

	}

	public static void main(String[] args) {
		TestJsonPack t = new TestJsonPack();
		t.testEncode();
	}

}

package vequn.lobby.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import util.ByteHelper;

/**
 * @author zhuqd
 * @Date 2017年8月22日
 * @Desc
 */
public class MessagePack {

	public String encode(JSONObject json) {
		if (json == null) {
			return null;
		}
		ByteBuf buffer = Unpooled.buffer();
		JSONObject parse = JSON.parseObject(json.toJSONString());
		loopEncode(buffer, parse);

		byte[] byt = new byte[buffer.readableBytes()];
		buffer.readBytes(byt);
		for (int i = 0; i < byt.length; i++) {
			System.out.println(byt[i]);
		}
		System.out.println("--------");
		// int value = ByteHelper.byteToInt(byt);
		long value = ByteHelper.byteToLong(byt);
		System.out.println(value);
		String template = "{cmd: 'number',time: 'number', name: 'string'}";
		// decode(byt, template);
		return "" + value;
	}

	private void loopEncode(ByteBuf buffer, JSONObject json) {
		Set<String> keySet = json.keySet();
		List<String> keyList = new ArrayList<>();
		for (String key : keySet) {
			keyList.add(key);
		}
		Collections.sort(keyList);
		// System.out.println(keyList);
		// byte[] message = new byte[1];

		// buffer.writeBytes(src)
		for (String key : keyList) {
			Object value = json.get(key);
			encodeObject(buffer, value);
		}
	}

	private void encodeObject(ByteBuf buffer, Object value) {
		if (value instanceof Integer) {
			byte[] byt = ByteHelper.toLengthByte((Integer) value);
			buffer.writeBytes(byt);
		} else if (value instanceof String) {
			String string = (String) value;
			byte[] len = ByteHelper.toByte(string.length());
			buffer.writeBytes(len);
			buffer.writeBytes(string.getBytes());
		} else if (value instanceof Long) {
			byte[] len = ByteHelper.toByte(8);
			byte[] byt = ByteHelper.longToByte((Long) value);
			buffer.writeBytes(len);
			buffer.writeBytes(byt);
		} else if (value instanceof JSONArray) {
			JSONArray array = (JSONArray) value;
			for (int i = 0; i < array.size(); i++) {
				encodeObject(buffer, array.get(i));
			}
		} else if (value instanceof JSONObject) {
			loopEncode(buffer, (JSONObject) value);
		} else {
		}
	}

	/**
	 * 
	 * @param byt
	 * @param template
	 * @return
	 */
	public JSONObject decode(byte[] byt, String template) {
		int index = 0;
		// for(int )
		return null;
	}
}

package util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author zhuqd
 * @Date 2017年7月27日
 * @Desc
 */
public class Base64Util {

	/**
	 * 加密
	 * 
	 * @param string
	 * @return
	 */
	public static String encode(String string) {
		String encode = "";
		try {
			byte[] encodeBase64 = Base64.getEncoder().encode(string.getBytes("UTF-8"));
			encode = new String(encodeBase64);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encode;
	}

	/**
	 * 解密
	 * 
	 * @param string
	 * @return
	 */
	public static String decode(String string) {
		String decode = "";
		try {
			byte[] encodeBase64 = Base64.getDecoder().decode(string.getBytes("UTF-8"));
			decode = new String(encodeBase64);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decode;
	}
}

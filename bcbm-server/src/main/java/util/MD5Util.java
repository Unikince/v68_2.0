package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Date: 2015年8月17日 下午2:17:54
 * @Author: zhuqd
 * @Description:
 */
public class MD5Util {

	/**
	 * 生成MD5
	 * 
	 * @param parameters
	 * @return
	 */
	public static String md5(Object... parameters) {
		StringBuilder sBuilder = new StringBuilder();
		for (Object parameter : parameters) {
			sBuilder.append(parameter.toString());
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sBuilder.toString().getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			//
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}

}

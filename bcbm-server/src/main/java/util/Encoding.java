package util;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 编码转换
 * 
 * @author zhuqiandong
 *
 */
public class Encoding {
	public static Logger log = LoggerFactory.getLogger(Encoding.class);

	/**
	 * OUT ISO-8859-1 转 UTF-8
	 * 
	 * @param str
	 * @return
	 */
	public static String ISO2UTF(String str) {
		try {
			str = new String(str.getBytes("UTF-8"), "ISO-8859-1").toString();
		} catch (UnsupportedEncodingException e) {
			log.error("转码出错");
		}
		return str;
	}

	/**
	 * IN UTF-8 转 ISO-8859-1
	 * 
	 * @param str
	 * @return
	 */
	public static String UTF2ISO(String str) {
		try {
			str = new String(str.getBytes("ISO-8859-1"), "UTF-8").toString();
		} catch (UnsupportedEncodingException e) {
			log.error("转码出错");
		}
		return str;
	}

	/**
	 * IN UTF-8 转 GBK
	 * 
	 * @param str
	 * @return
	 */
	public static String UTF2GBK(String str) {
		try {
			str = new String(str.getBytes("GBK"), "UTF-8").toString();
		} catch (UnsupportedEncodingException e) {
			log.error("转码出错");
		}
		return str;
	}

	/**
	 * OUT UTF-8 转 GBK
	 * 
	 * @param str
	 * @return
	 */
	public static String GBK2UTF(String str) {
		try {
			str = new String(str.getBytes("UTF-8"), "GBK").toString();
		} catch (UnsupportedEncodingException e) {
			log.error("转码出错");
		}
		return str;
	}

	/**
	 * GBK 转 ISO
	 * 
	 * @param str
	 * @return
	 */
	public static String GBK2ISO(String str) {
		try {
			str = new String(str.getBytes("SO-8859-1"), "GBK").toString();
		} catch (UnsupportedEncodingException e) {
			log.error("转码出错");
		}
		return str;
	}
}

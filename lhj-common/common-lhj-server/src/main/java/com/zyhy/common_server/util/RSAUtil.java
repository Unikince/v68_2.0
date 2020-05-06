package com.zyhy.common_server.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * AnySDK 支付通知验签算法
 * 
 * @author libo<libo@chukong-inc.com>
 * @date 2014-08-13
 */
public class RSAUtil{
	
	/**
	 * 全局数组,用于base64
	 */
    private static final String[] _strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public RSAUtil () {}		
	
	/**
	 * 验证签名
	 *
	 * @param String paramValues 待签字符串
	 * @param String originSign 从AnySDK接收到的sign值
	 * @return boolean 
	 */
	public static String checkSign(Map<String, String> params, String privatekey) {
		String paramValues = getSignDataStr(params);
		String newSign = getSign(paramValues, privatekey);
		return newSign;
	}
	
	 public static String getSignDataStr(Map<String, String> params)
	{
		StringBuffer content = new StringBuffer();

		// 按照key做排序
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			if ("sign".equals(key)) {
				continue;
			}
			String value = (String) params.get(key);
			if (value != null) {
				content.append((i == 0 ? "" : "&") + key + "=" + value);
			} else {
				content.append((i == 0 ? "" : "&") + key + "=");
			}

		}
		return content.toString();
	}
	
	/**
	 * 计算待签字符串的sign值
	 * 
	 * @param String paramValues 待签字符串
	 * @return String 计算所得到的sign签名
	 */
	public static String getSign(String paramValues, String privateKey){
		String md5Values=MD5Encode(paramValues);
		md5Values=MD5Encode(md5Values.toLowerCase() + privateKey).toLowerCase();
		return md5Values;
	}
	
	/**
	 * MD5编码算法
	 * 
	 * @param String sourceStr 待计算的字符串
	 * @return String md5值
	 */
	public static String MD5Encode(String sourceStr) {
		String signStr=null;
		try {
			byte[] bytes = sourceStr.getBytes("utf-8");
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(bytes);
			byte[] md5Byte = md5.digest();
			if (md5Byte != null) {
				signStr = _byteToString( md5Byte);
			}
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		}
		return signStr;
	}
	
    /**
	 * 返回形式为数字跟字符串
	 */
    private static String _byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return _strDigits[iD1] + _strDigits[iD2];
    }

    /**
	 * 转换字节数组为16进制字串
	 */
    private static String _byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(_byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }
}

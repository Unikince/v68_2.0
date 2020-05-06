package com.zyhy.common_server.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 从request里面获得IP
 * @author pengfei.he
 * @date 2011-7-1
 */
public class IpUtils {

	public static String getIpAddress(HttpServletRequest request) {
		// 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("Proxy-Client-IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("WL-Proxy-Client-IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_CLIENT_IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getRemoteAddr();  
            }  
        } else if (ip.length() > 15) {  
            String[] ips = ip.split(",");  
            for (int index = 0; index < ips.length; index++) {  
                String strIp = (String) ips[index];  
                if (!("unknown".equalsIgnoreCase(strIp))) {  
                    ip = strIp;  
                    break;  
                }  
            }  
        }  
        return ip.trim(); 
	}

	/**
	 * 
	 * @Description 返回所有网络接口
	 * 
	 * @author
	 * @date 2013-6-4 下午05:58:53
	 * @return
	 * @throws SocketException
	 *             NetworkInterface[]
	 */
	public static NetworkInterface[] getAllNetworkInterface()
			throws SocketException {
		Enumeration<NetworkInterface> enumeration = NetworkInterface
				.getNetworkInterfaces();
		List<NetworkInterface> list = new ArrayList<NetworkInterface>();

		for (; enumeration.hasMoreElements();) {
			list.add(enumeration.nextElement());
		}

		return list.toArray(new NetworkInterface[list.size()]);
	}

	/**
	 * 
	 * @Description 返回物理地址, 每一字节用冒号分割.
	 * @author
	 * @date 2013-6-4 下午05:46:54
	 * @return String
	 * @throws SocketException
	 */
	public static String getPhysicalAddress(NetworkInterface networkInterface)
			throws SocketException {
		byte[] bytearray = networkInterface.getHardwareAddress();

		return bytesToHexString(bytearray);
	}

	/**
	 * 
	 * @Description
	 * @author
	 * @date 2013-6-4 下午05:54:35
	 * @param src
	 * @return String
	 */
	public static String bytesToHexString(byte[] src) {
		if (src == null || src.length == 0) {
			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();

		if (src == null || src.length <= 0) {
			return null;
		}

		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;

			String hv = Integer.toHexString(v);

			if (hv.length() < 2) {
				stringBuilder.append(0);
			}

			stringBuilder.append(hv).append(":");
		}

		return stringBuilder
				.substring(0, stringBuilder.toString().length() - 1);
	}

	public static String getFirstPhysicalAddress() throws SocketException {

		NetworkInterface[] networkIntefaceArray = getAllNetworkInterface();
		NetworkInterface networkInterface = null;
		String result = null;

		for (int i = 0; i < networkIntefaceArray.length; i++) {
			networkInterface = networkIntefaceArray[i];

			result = getPhysicalAddress(networkInterface);

			if (result != null) {
				return result;
			}
		}

		return null;
	}

	/**
	 * unicode 转换成 中文
	 * 
	 * @author fanhui 2007-3-15
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed      encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}
}

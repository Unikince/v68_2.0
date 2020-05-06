/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.zyhy.common_server.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import com.zyhy.common_server.exception.ZgqpGameException;



public final class Base64Util
{
	/**
	 * 编码
	 * @param value
	 * @param charset
	 * @return
	 */
	public static byte[] encodeToByte(String value, String charset){
		try {
			return encodeToByte(value.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			throw new ZgqpGameException("Base64Util encodeToByte error");
		}
	}
	
	/**
	 * 编码
	 * @param value
	 * @return
	 */
	public static byte[] encodeToByte(byte[] value){
		return Base64.getEncoder().encode(value);
	}
	
	/**
	 * 编码
	 * @param value
	 * @param charset
	 * @return
	 */
	public static String encodeToString(String value, String charset){
		try {
			return Base64.getEncoder().encodeToString(value.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			throw new ZgqpGameException("Base64Util encodeToString error");
		}
	}
	
	/**
	 * 解码
	 * @param value
	 * @param charset
	 * @return
	 */
	public static String decodeToString(String value, String charset){
		try {
			byte[] bytes = Base64.getDecoder().decode(value);
			String result = new String(bytes, charset);
			return result;
		} catch (UnsupportedEncodingException e) {
			throw new ZgqpGameException("Base64Util decodeToString error");
		}
	}
	
	/**
	 * 解码
	 * @param value
	 * @param charset
	 * @return
	 */
	public static String decodeToString(byte[] value, String charset){
		try {
			byte[] bytes = Base64.getDecoder().decode(value);
			String result = new String(bytes, charset);
			return result;
		} catch (UnsupportedEncodingException e) {
			throw new ZgqpGameException("Base64Util decodeToString error");
		}
	}
	
	/**
	 * 解码
	 * @param value
	 * @param charset
	 * @return
	 */
	public static byte[] decodeToByte(String value){
		return Base64.getDecoder().decode(value);
	}
}

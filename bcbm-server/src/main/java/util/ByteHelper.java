package util;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @Date: 2015年8月11日 下午3:05:49
 * @Author: zhuqd
 * @Description: 字节工具
 */
public class ByteHelper {

	/**
	 * 字节转换成int
	 * 
	 * @param byt
	 *            byte[4]
	 * @return
	 */
	public static int byteToInt(byte[] byt) {
		int value = 0;
		for (int i = 0; i < byt.length; i++) {
			int offset = i * 8;
			value += (byt[byt.length - 1 - i] & 0xFF) << offset;
		}
		return value;
	}

	/**
	 * 字节转换成long
	 * 
	 * @param byt
	 *            byte[4]
	 * @return
	 */
	public static long byteToLong(byte[] byt) {
		long s = 0;
		long s0 = byt[7] & 0xff;// 最低位
		long s1 = byt[6] & 0xff;
		long s2 = byt[5] & 0xff;
		long s3 = byt[4] & 0xff;
		long s4 = byt[3] & 0xff;// 最低位
		long s5 = byt[2] & 0xff;
		long s6 = byt[1] & 0xff;
		long s7 = byt[0] & 0xff;

		// s0不变
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		s4 <<= 8 * 4;
		s5 <<= 8 * 5;
		s6 <<= 8 * 6;
		s7 <<= 8 * 7;
		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
		return s;
	}

	/**
	 * 将int转换成byte[4]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] intToByte(int value) {
		byte[] byt = new byte[4];
		byt[3] = (byte) (0xff & value);
		byt[2] = (byte) ((0xff00 & value) >> 8);
		byt[1] = (byte) ((0xff0000 & value) >> 16);
		byt[0] = (byte) ((0xff000000 & value) >> 24);
		return byt;
	}

	/**
	 * 将long转换成byte[8]
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] longToByte(long value) {
		byte[] byt = new byte[8];
		long temp = value;
		for (int i = 0; i < 8; i++) {
			byt[7 - i] = new Long(temp & 0xff).byteValue();//
			temp = temp >> 8;//
		}
		return byt;
	}

	/**
	 * 将int值转成byte
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toLengthByte(int value) {
		byte[] byt;
		if (value > -0xff && value < 0xff) {
			byt = new byte[2];
			byt[0] = (byte) (0xff & 1);
			byt[1] = (byte) (0xff & value);
		} else if (value > -0xffff && value < 0xffff) {
			byt = new byte[3];
			byt[0] = (byte) (0xff & 2);
			byt[1] = (byte) ((0xff00 & value) >> 8);
			byt[2] = (byte) (0xff & value);
		} else if (value > -0xffffff && value < 0xffffff) {
			byt = new byte[4];
			byt[0] = (byte) (0xff & 3);
			byt[1] = (byte) ((0xff00 & value) >> 16);
			byt[2] = (byte) ((0xff00 & value) >> 8);
			byt[3] = (byte) (0xff & value);
		} else {
			byt = new byte[5];
			byt[0] = (byte) (0xff & 4);
			byt[1] = (byte) ((0xff00 & value) >> 24);
			byt[2] = (byte) ((0xff00 & value) >> 16);
			byt[3] = (byte) ((0xff00 & value) >> 8);
			byt[4] = (byte) (0xff & value);
		}
		return byt;
	}

	/**
	 * 将int值转成byte
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] toByte(int value) {
		byte[] byt;
		if (value > -0xff && value < 0xff) {
			byt = new byte[1];
			byt[0] = (byte) (0xff & value);
		} else if (value > -0xffff && value < 0xffff) {
			byt = new byte[2];
			byt[1] = (byte) ((0xff00 & value) >> 8);
			byt[2] = (byte) (0xff & value);
		} else if (value > -0xffffff && value < 0xffffff) {
			byt = new byte[3];
			byt[0] = (byte) ((0xff00 & value) >> 16);
			byt[1] = (byte) ((0xff00 & value) >> 8);
			byt[2] = (byte) (0xff & value);
		} else {
			byt = intToByte(value);
		}
		return byt;
	}

	/**
	 * 加密
	 * 
	 * @param byt
	 * @param offset
	 */
	public static void byteDecrypt(byte[] byt, int offset) {
		for (int i = 0; i < byt.length; i++) {
			byt[i] = (byte) (byt[i] - offset);
		}
	}

	/**
	 * 解密
	 * 
	 * @param byt
	 * @param offset
	 */
	public static void byteEncrypt(byte[] byt, int offset) {
		for (int i = 0; i < byt.length; i++) {
			byt[i] = (byte) (byt[i] + offset);
		}
	}

	/**
	 * 生成消息
	 * 
	 * @param message
	 * @return
	 */
	public static ByteBuf createMessageByte(String message) {
		byte[] body = null;
		try {
			body = message.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ByteBuf buf = Unpooled.buffer(body.length);
		buf.writeBytes(body);
		return buf;
	}

	/**
	 * 生成消息，包括长度
	 * 
	 * @param message
	 * @return
	 */
	public static ByteBuf createMessageByteWithLength(String message) {
		byte[] len = ByteHelper.intToByte(message.length());
		byte[] body = message.getBytes();
		ByteBuf buf = Unpooled.buffer(body.length);
		buf.writeBytes(len);
		buf.writeBytes(body);
		return buf;
	}

	/**
	 * 打印byte[]数字
	 * 
	 * @param byt
	 */
	public static void printByte(byte[] byt) {
		for (int i = 0; i < byt.length; i++) {
			System.out.print(" " + byt[i]);
		}
		System.out.println();
	}

	/**
	 * 将指定的数据拼接成string,并转换成byte[]
	 * 
	 * @param args
	 * @return
	 */
	public static byte[] getBytes(Object... args) {
		String string = "";
		for (int i = 0; i < args.length; i++) {
			if (args != null) {
				string += args[i];
			}

		}
		return string.getBytes();
	}

	/**
	 * 创建websocket的返回消息
	 * 
	 * @param message
	 * @return
	 */
	public static Object createFrameMessage(String message) {
		TextWebSocketFrame frame = new TextWebSocketFrame(message);
		return frame;
	}
}

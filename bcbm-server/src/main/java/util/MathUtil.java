package util;

import java.util.Random;

/**
 * @Date: 2015年9月30日 下午3:27:03
 * @Author: zhuqd
 * @Description:
 */
public class MathUtil {
	public static final Random RANDOM = new Random();

	/**
	 * 保留N位小数
	 * 
	 * @param value
	 * @param keep
	 * @return
	 */
	public static double truncate(double value, int keep) {
		if (keep <= 0 || keep > 16) {
			throw new RuntimeException("keep precision error");
		}
		double power = value * Math.pow(10, keep);
		long temp = (long) (value * power);
		return temp / power;
	}

	/**
	 * 保留N位小数
	 * 
	 * @param value
	 * @param keep
	 *            0~16
	 * @return
	 */
	public static double keep(double value, int keep) {
		if (keep <= 0 || keep > 16) {
			throw new RuntimeException("keep precision error");
		}
		long power = (long) Math.pow(10, keep);
		long temp = (long) (value * power);
		if (temp < 1) {
			return 0;
		}
		return temp * 1.0 / power;
	}
}

package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Date: 2015年8月20日 上午10:25:54
 * @Author: zhuqd
 * @Description: 随机
 */
public class RandUtil {
	// private static Random random = new Random();
	private static Random random0 = new Random();
	private static final String[] LETTER_ARRAY = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "a",
			"b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
			"w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
			"R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	/**
	 * 生成随机int
	 * 
	 * @param max
	 * @return
	 */
	public static int randInt(int max) {
		if (max <= 0) {
			throw new RuntimeException("max must > 0");
		}
		return ThreadLocalRandom.current().nextInt(max);
		// return random.nextInt(max);
	}

	/**
	 * 随机生成double
	 * 
	 * @return 精度千万分之一
	 */
	public static double randDouble() {
		return random0.nextDouble();
	}

	/**
	 * 生成指定2个值之间的随机数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randBetween(int min, int max) {
		if (min == 0 && max == 0) {
			throw new RuntimeException("min and max can not be zero at the same time");
		}
		if (min > max) {
			return ThreadLocalRandom.current().nextInt(min - max) + max;
		}
		return ThreadLocalRandom.current().nextInt(max - min) + min;
	}

	/**
	 * 随机生成不重复的数字
	 * 
	 * @param size
	 * @return
	 */
	public static List<Integer> randNoRepeatArray(int size) {
		List<Integer> list = new ArrayList<>();
		if (size <= 0) {
			return list;
		}
		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		Collections.shuffle(list);
		return list;
	}

	/**
	 * 生成随机字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String randString(int length) {
		if (length <= 0) {
			throw new RuntimeException("need string length is less than 0");
		}
		String string = "";
		for (int i = 0; i < length; i++) {
			string += LETTER_ARRAY[randInt(LETTER_ARRAY.length)];
		}
		return string;
	}
}

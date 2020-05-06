package com.zyhy.common_server.util;

import java.util.Arrays;
import java.util.Random;

/**
 * 
 * @author
 * @date 2012-9-29下午01:27:02
 * 
 */
public final class RandomUtil {

	private RandomUtil() {

	}

	public static final Random RANDOM = new Random();

	/**
	 * 
	 * @Description 方法说明
	 * @author
	 * @date 2013-9-16 下午02:49:36
	 * @param size
	 * @return int
	 */
	public static int random(int size) {
		return RANDOM.nextInt(size);
	}
	
	/**
	 * 
	 * @Description 方法说明
	 * @author
	 * @date 2013-6-4 下午04:58:55
	 * @param array
	 * @return String
	 */
	public static String randomString(String[] array) {
		if (array == null || array.length == 0) {
			throw new IllegalArgumentException();
		}

		return array[RANDOM.nextInt(array.length)];
	}

	/**
	 * 
	 * @param count
	 *            数组的长度
	 * @param notcontain
	 *            不包含的元素
	 * @return
	 */
	public static int[] random(int count, int notcontain) {
		int[] array = new int[count];
		Arrays.copyOf(array, 0);
		int index = 0;
		int result = 0;

		do {

			result = RANDOM.nextInt(count);

			Arrays.sort(array);
			if (Arrays.binarySearch(array, result) >= 0) {
				continue;
			}

			array[index++] = result;

		} while (index < count);

		return array;
	}

	/**
	 * 
	 * @Description 产生随机数
	 * @author linanjun
	 * @date 2012-10-15 下午03:54:58
	 * @param start
	 * @param end
	 * @return int
	 */
	public static int getRandom(int start, int end) {
		return Math.round((float)(Math.random() * (end - start)) + start);
	}

	public static double getRandom(double start, double end) {
		int istart = (int) (start * 100);
		int iend = (int) (end * 100);

		return ((Math.random() * (iend - istart)) + istart) / 100D;
	}
	
	/**
	 * 计算数组中的随机概率 用随机数 与 0-数组里面数值之和相比 例子： int[] a = {2500,3500,4000} 计算概率区间：
	 * 0-2500 2501-6000 6001-10000 如果随机数位 1000，那么本方法返回2500
	 * 
	 * @param array
	 *            可以未排序
	 * @return 返回 中签的那个值
	 */
	public static int getRandom(Integer[] arr_typr_p) {
		Arrays.sort(arr_typr_p);// 排序
		int totalrr = 0;//总和
		for (int i = 0; i < arr_typr_p.length; i++) {
			int temp = arr_typr_p[i];
			totalrr += temp;
		}

		int rr = RandomUtil.getRandom(0, totalrr);
//		 System.out.println(rr);
//		 System.out.println("total:"+totalrr);
		int result = 0;

		for (int i = 0; i < arr_typr_p.length; i++) {
			int s = 0;
			int e = 0;
			if (i == 0) {
				s = 0;
				e = arr_typr_p[0];
			} else if (i == arr_typr_p.length - 1) {
				for (int j = i - 1; j >= 0; j--) {
					s += arr_typr_p[j];
				}
				s += 1;
				e = totalrr;
			} else {
				for (int j = i-1; j >=0; j--) {
					s += arr_typr_p[j];
				}
				for (int j = i; j >= 0; j--) {
					e += arr_typr_p[j];
				}
				s+=1;
			}
//			 System.out.println(s+"-"+e);
			if (rr >= s && rr <= e) {
				result = arr_typr_p[i];
//				 System.out.println("result:"+result);
				break;
			}
		}

		return result;
	}

	/**
	 * 
	 * @Description 随机范围
	 * @author
	 * @date 2014-6-29 上午11:47:43 
	 * @param limitArray
	 * @return int
	 */
	public static int randomLimit( int[] limitArray ) {
		
		int sum = 0;
		
		for( int i = 0; i < limitArray.length; i++ ) {
			sum = sum + limitArray[i];
		}
		
		if( sum == 0 ) {
			return random(limitArray.length);
		}
		
		int random = random(sum);
		int step = 0;
		
		for( int i = 0; i < limitArray.length; i++ ) {
			step = step + limitArray[i];
			if( random < step ) {
				return i;
			}
		}
		
		return 0;
	}
	
	/**
	 * 
	 * @Description 随机范围
	 * @author Hugh
	 * @date 2014-6-29 上午11:47:43 
	 * @param limitArray
	 * @return int
	 */
	public static int randomLimit( Integer[] limitArray ) {
		
		int sum = 0;
		
		for( int i = 0; i < limitArray.length; i++ ) {
			sum = sum + limitArray[i];
		}
		
		if( sum == 0 ) {
			return random(limitArray.length);
		}
		
		int random = random(sum);
		int step = 0;
		
		for( int i = 0; i < limitArray.length; i++ ) {
			step = step + limitArray[i];
			if( random < step ) {
				return i;
			}
		}
		
		return 0;
	}
	
	public static int randomNotContain( int[] array, int item ) {
		
		int result = 0;
		
		do {
			result = array[RANDOM.nextInt(array.length)];
		} while( result == item );
	
		return result;
	}
}

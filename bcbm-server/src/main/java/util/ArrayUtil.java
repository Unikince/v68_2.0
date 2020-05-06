package util;

/**
 * @Date: 2016年2月16日 上午10:56:13
 * @Author: zhuqd
 * @Description:
 */
public class ArrayUtil {

	/**
	 * 数组复制
	 * 
	 * @param array
	 * @return
	 */
	public static int[][] copy(int[][] array) {
		if (array == null) {
			return null;
		}
		int row = array.length;
		int col = array[0].length;
		//
		int[][] array0 = new int[row][col];
		for (int i = 0; i < row; i++) {
			for (int k = 0; k < col; k++) {
				array0[i][k] = array[i][k];
			}
		}
		return array0;
	}
}

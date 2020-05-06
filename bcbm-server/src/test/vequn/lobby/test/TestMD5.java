package vequn.lobby.test;

import com.dmg.lobby.def.Config;

import util.MD5Util;

/**
 * @author zhuqd
 * @Date 2017年9月25日
 * @Desc
 */
public class TestMD5 {

	public static void main(String[] args) {
		System.out.println("11111  " + MD5Util.md5(11111, Config.MD5_SALT));
		System.out.println("22222  " + MD5Util.md5(22222, Config.MD5_SALT));
		System.out.println("33333  " + MD5Util.md5(33333, Config.MD5_SALT));
		System.out.println("44444  " + MD5Util.md5(44444, Config.MD5_SALT));
		System.out.println("55555  " + MD5Util.md5(55555, Config.MD5_SALT));
		System.out.println("66666  " + MD5Util.md5(66666, Config.MD5_SALT));
		System.out.println("77777  " + MD5Util.md5(77777, Config.MD5_SALT));
		System.out.println("88888  " + MD5Util.md5(88888, Config.MD5_SALT));

	}

}

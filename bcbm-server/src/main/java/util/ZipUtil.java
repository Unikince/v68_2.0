package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @Date: 2015年11月9日 下午3:42:31
 * @Author: zhuqd
 * @Description:
 */
public class ZipUtil {

	/**
	 * 解压
	 * 
	 * @param zipFile
	 * @param destPath
	 */
	public static void unzip(String zipFile, String destPath) {
		File zip = new File(zipFile);
		if (!zip.exists()) {
			throw new RuntimeException("not find zip file :" + zipFile);
		}
		File dest = new File(destPath);
		if (!dest.exists()) {
			dest.mkdirs();
		}
		try {
			// 先指定压缩档的位置和档名，建立FileInputStream对象
			FileInputStream fins = new FileInputStream(zipFile);
			// 将fins传入ZipInputStream中
			ZipInputStream zins = new ZipInputStream(fins);
			ZipEntry ze = null;
			byte[] ch = new byte[256];
			while ((ze = zins.getNextEntry()) != null) {
				File zipDestPath = new File(destPath + File.separator + ze.getName());
				File fpath = new File(zipDestPath.getParentFile().getPath());
				if (ze.isDirectory()) {
					if (!zipDestPath.exists()) {
						zipDestPath.mkdirs();
					}
					zins.closeEntry();
				} else {
					if (!fpath.exists())
						fpath.mkdirs();
					FileOutputStream fouts = new FileOutputStream(zipDestPath);
					int i;
					while ((i = zins.read(ch)) != -1)
						fouts.write(ch, 0, i);
					zins.closeEntry();
					fouts.close();
				}
			}
			fins.close();
			zins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

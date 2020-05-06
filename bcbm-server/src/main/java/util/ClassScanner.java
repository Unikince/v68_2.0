package util;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Date: 2015年8月10日 下午6:14:42
 * @Author: zhuqd
 * @Description: 类扫描器，反射并实例化class
 * @see util.ClassUtils
 */
@Deprecated
public class ClassScanner {

	/**
	 * 获取项目中的所有class
	 * 
	 * @param scanPath
	 *            -- 扫描路径
	 * @param annotation
	 * @param keyword
	 *            --项目路径关键字
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static List<Class<?>> findAllClass(String scanPath, Class<? extends Annotation> annotation, String keyword)
			throws ClassNotFoundException {
		File dir = new File(scanPath);
		if (!dir.exists()) {
			throw new RuntimeException("the path is not correct");
		}
		if (dir.isDirectory()) {
			try {
				return findAllClassDirector(dir, annotation, keyword);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (scanPath.endsWith("jar")) {
			try {
				return findAllClassJarFile(scanPath, annotation, keyword);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<>();

	}

	/**
	 * 查询jar目录
	 * 
	 * @param jarName
	 * @param annotation
	 * @param keyword
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> findAllClassJarFile(String jarName, Class<? extends Annotation> annotation,
			String keyword) throws IOException, ClassNotFoundException {
		List<Class<?>> refClass = new ArrayList<>();
		JarFile jar = new JarFile(jarName);
		Enumeration<?> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = (JarEntry) entries.nextElement();
			if (!entry.isDirectory()) {
				// System.out.println(entry);
				if (entry.getName().endsWith("class") && (entry.getName().startsWith(keyword))) {
					String[] arr = entry.getName().split("\\.");
					String name = arr[0].replace("/", ".");
					Class<?> clazz = Class.forName(name);
					if (clazz.isAnnotationPresent(annotation)) {
						refClass.add(clazz);
					}
				}
			}
		}
		jar.close();
		return refClass;
	}

	private static List<Class<?>> findAllClassDirector(File scanPath, Class<? extends Annotation> annotation,
			String keyword) throws ClassNotFoundException, IOException {
		// 保存所有class的路径
		List<String> path = new ArrayList<>();
		// 读取所有文件的路径
		File[] fileList = scanPath.listFiles();
		// // 打压读取的文件
		// for (File file : fileList) {
		// System.out.println("dir : " + file.getAbsolutePath());
		// }
		for (File file : fileList) {
			if (file.isDirectory()) {
				readSrcFile(file, path);
			} else {
				path.add(file.getPath());
			}
		}

		// 反射路径
		List<Class<?>> refClass = new ArrayList<>();
		for (String filePath : path) {
			if (filePath.endsWith("jar")) {
				refClass.addAll(findAllClassJarFile(filePath, annotation, keyword));
				continue;
			}
			if (!filePath.contains("dmg")) {
				continue;
			}
			if (!filePath.endsWith("class")) {
				continue;
			}
			
//			 System.err.println("filePath find : " + filePath);
			String refPath = parserClassFilePath(filePath);
			if (!StringHelper.isEmpty(refPath)) {
				Class<?> clazz;
				try {
					clazz = Class.forName(refPath);
				} catch (Exception e) {
					continue;
				}
				if (clazz.isAnnotationPresent(annotation)) {
					refClass.add(clazz);
				}
			}
		}
		return refClass;
	}

	/**
	 * 处理class文件的路径.<br>
	 * 原始:....\bin\com\slot\core\ShutdownWork.class<br>
	 * 返回:com.slot.core.ShutdownWork
	 * 
	 * @param path
	 * @return
	 */
	private static String parserClassFilePath(String path) {
		if (!path.contains("com")) {
			return "";
		}
		return path.substring(path.indexOf("com"), path.length()).replace("\\", ".").replace(".class", "");
		// String[] array = path.split("com");
		// String className = "";
		// if (array.length == 1) {
		// className = array[0];
		// } else if (array.length == 2) {
		// className = array[1];
		// }
		// className = "com/" + className;
		// String[] arr = path.split("classes");
		// if (arr.length != 2) {
		// arr = path.split("bin");
		// if (arr.length != 2) {
		// return null;
		// }
		// }
		// String[] sp = arr[1].split("\\.");
		// String ref = sp[0];
		// ref = ref.replace("\\", ".");
		// ref = ref.replace("/", ".");
		// return ref.substring(1, ref.length());

	}

	/**
	 * 读取class文件路径
	 * 
	 * @param dir
	 */
	private static void readSrcFile(File dir, List<String> path) {
		File[] list = dir.listFiles();
		for (File file : list) {
			// System.out.println("file : " + file.getAbsolutePath());
			if (file.isDirectory()) {
				readSrcFile(file, path);
			} else {
				if (file.getName().endsWith("class")) {
					path.add(file.getPath());
				}
			}
		}
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> findClassByName(String name) throws ClassNotFoundException {
		return Class.forName(name);

	}
}

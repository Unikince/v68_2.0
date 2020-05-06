package util;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtils {

	@FunctionalInterface
	public interface ClassFilter {
		boolean matches(Class<?> clazz);
	}

	public static Set<Class<?>> searchByAnnotation(String packageName, final Class<? extends Annotation> annotationClass) {
		return scanPackage(packageName, cls -> cls.isAnnotationPresent(annotationClass));
	}

	public static Set<Class<?>> scanPackage(String packageName, ClassFilter filter) {
		Set<Class<?>> classes = new LinkedHashSet<>();
		if (packageName == null || packageName.isEmpty()) {
			scanJavaClassPath("", filter, classes);
		} else {
			packageName = normalizePackageName(packageName);
			scanResources(packageName, filter, classes);
		}
		return classes;
	}

	private static void scanJavaClassPath(String packageName, ClassFilter filter, Set<Class<?>> classes) {
		String prefix = toPath(packageName);
		String[] paths = getJavaClassPaths();
		for (String path : paths) {
			if (path.endsWith(".jar")) {
				processJarFile(toJarFile(path), prefix, filter, classes);
			} else {
				processFile(new File(path), packageName, filter, classes);
			}
		}
	}

	private static void scanResources(String packageName, ClassFilter filter, Set<Class<?>> classes) {
		String path = toPath(packageName);
		Enumeration<URL> resources = getResources(path);
		while (resources.hasMoreElements()) {
			URL url = resources.nextElement();
			String protocol = url.getProtocol();
			if ("file".equals(protocol)) {
				processFile(toFile(url), packageName, filter, classes);
			} else if ("jar".equals(protocol)) {
				processJarFile(toJarFile(url), path, filter, classes);
			}
		}
	}

	private static void processFile(final File file, final String packageName, final ClassFilter filter, final Set<Class<?>> classes) {
		processFile(file, file, packageName, filter, classes);
	}

	private static void processFile(final File root, final File file, final String packageName, final ClassFilter filter, final Set<Class<?>> classes) {
		if (file.isDirectory()) {
			file.listFiles(f -> {
				processFile(root, f, packageName, filter, classes);
				return false;
			});
			return;
		}

		String className = file.getAbsolutePath();
		if (!className.endsWith(".class")) {
			return;
		}
		className = className.substring(root.getAbsolutePath().length());
		if (className.charAt(0) == '/' || className.charAt(0) == '\\') {
			className = className.substring(1);
		}
		className = packageName + className.substring(0, className.length() - ".class".length());
		className = toClassName(className);
		addClass(classes, filter, className);
	}

	private static void processJarFile(JarFile jar, String prefix, ClassFilter filter, Set<Class<?>> classes) {
		Enumeration<JarEntry> entries = jar.entries();
		for (; entries.hasMoreElements();) {
			JarEntry entry = entries.nextElement();
			if (!entry.isDirectory() && entry.getName().endsWith(".class") && entry.getName().startsWith(prefix)) {
				addClass(classes, filter, toClassName(entry.getName()));
			}
		}
	}

	private static void addClass(Set<Class<?>> classes, ClassFilter filter, String className) {
		Class<?> cls = toClass(className);
		if (cls != null && (filter == null || filter.matches(cls))) {
			classes.add(cls);
		}
	}

	private static String normalizePackageName(String packageName) {
		packageName = packageName.replace('/', '.');
		return packageName.charAt(packageName.length() - 1) == '.' ? packageName : packageName + '.';
	}

	private static String toPath(String packageName) {
		return packageName.replace('.', '/');
	}

	private static String[] getJavaClassPaths() {
		return System.getProperty("java.class.path").split(System.getProperty("path.separator"));
	}

	private static Enumeration<URL> getResources(String path) {
		try {
			return getClassLoader().getResources(path);
		} catch (Exception e) {
			throw new IllegalArgumentException("get resources failed: " + path, e);
		}
	}

	private static File toFile(URL url) {
		try {
			return new File(url.toURI());
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("can't convert url to file: " + url, e);
		}
	}

	private static JarFile toJarFile(URL url) {
		try {
			return ((JarURLConnection) url.openConnection()).getJarFile();
		} catch (IOException e) {
			throw new IllegalArgumentException("can't convert url to jar file: " + url, e);
		}
	}

	private static JarFile toJarFile(String path) {
		try {
			return new JarFile(path);
		} catch (IOException e) {
			throw new IllegalArgumentException("it isn't a jar file: " + path, e);
		}
	}

	private static String toClassName(String path) {
		path = path.replace('/', '.').replace('\\', '.');
		return path.endsWith(".class") ? path.substring(0, path.length() - ".class".length()) : path;
	}

	private static Class<?> toClass(String className) {
		try {
			return getClassLoader().loadClass(className);
		} catch (Throwable e) {
			// IGNORE
		}
		try {
			return Class.forName(className);
		} catch (Throwable e) {
//			throw new IllegalArgumentException("class not found: " + className, e);
		}
		return null;
	}

	private static ClassLoader getClassLoader() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader != null)
			return classLoader;
		return ClassUtils.class.getClassLoader();
	}

//	public static void main(String[] args) throws Exception {
//		System.out.println(searchByAnnotation("com.dmg", Def.class));
//	}

}

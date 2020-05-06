package com.dmg.bcbm.core.manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.ClassUtils;
import util.FileUtil;
import util.Value;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.bcbm.core.abs.def.BaseDef;
import com.dmg.bcbm.core.abs.def.type.FileType;
import com.dmg.bcbm.core.abs.def.type.IDefType;
import com.dmg.bcbm.core.annotation.Def;

/**
 * @param <T>
 * @Date: 2015年10月8日 上午10:52:09
 * @Author: zhuqd
 * @Description:
 */
@Component("lobbyDefFactory")
public class DefFactory implements ApplicationContextAware {
	
	private static Logger logger = LoggerFactory.getLogger(DefFactory.class);
	private static DefFactory instance;
	private final Map<IDefType, Map<Object, Object>> map = new HashMap<>();

	private ApplicationContext context;

	private DefFactory() {
		instance = this;
	}

	public static DefFactory instance() {
		return instance;
	}

	/**
	 *
	 * @throws Exception
	 */
	public void init() throws Exception {
		updateDef();
		logger.info("[DEFFAC]INITED");
	}

	/**
	 * 更新配置
	 */
	public void updateDef() throws Exception {
		String scanPath = FileUtil.getProjectPath();
		Collection<Class<?>> classes = ClassUtils.searchByAnnotation("com.dmg", Def.class);
		for (Class<?> clazz : classes) {
			Component component = clazz.getAnnotation(Component.class);
			if (component != null) {
				addDefInstance((BaseDef<?, ?>) context.getBean(clazz));
				continue;
			}
			Def headDef = clazz.getAnnotation(Def.class);
			String config = headDef.value();
			ResourceLoader resourceLoader = new DefaultResourceLoader();
	        Resource resource = resourceLoader.getResource("classpath:" + config);
	        InputStream configFile = null;
	        if (ResourceUtils.isJarURL(resource.getURL())) {
	          configFile = resource.getInputStream();
	        } else {
	          configFile = new FileInputStream(ResourceUtils.getFile(resource.getURI()));
	        }
	        if (configFile == null) {
	          throw new RuntimeException("config not find .. " + scanPath + "/" + config);
	        }
			if (FileUtil.getFileType(config).equals(FileType.PROPERTIES)) {
				parserPropertiesConfig(configFile, clazz);
			} else if (FileUtil.getFileType(config).equals(FileType.XML)) {
				parserXMLConfig(configFile, clazz);
			} else if (FileUtil.getFileType(config).equals(FileType.JSON)) {
				parserJSONConfig(configFile, clazz);
			}
		}
	}

	/**
	 * 根据类型查询配置
	 *
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseDef<?, ?>> T getDef(IDefType type) {
		return (T) map.get(type).get(1);
	}

	/**
	 * 根据类型和id查询配置
	 *
	 * @param type
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseDef<?, ?>> T getDef(IDefType type, Object id) {
		Map<Object, Object> subMap = map.get(type);
		if (subMap == null) {
			return null;
		}
		return (T) subMap.get(id);
	}

	/**
	 * 获取一组配置
	 *
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseDef<?, ?>> List<T> getDefGroup(IDefType type) {
		Map<Object, Object> subMap = map.get(type);
		if (subMap == null) {
			return Collections.emptyList();
		}
		List<BaseDef<?, ?>> list = new ArrayList<>();
		Collection<Object> collection = subMap.values();
		for (Object object : collection) {
			list.add((T) object);
		}
		return (List<T>) list;
	}

	/**
	 * 读取xml配置文件
	 *
	 * @param configFile
	 * @param clazz
	 */
	private void parserXMLConfig(InputStream configFile, Class<?> clazz) throws Exception {
		Map<String, String> configMap = new HashMap<>();
		//
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document docment = db.parse(configFile);
		NodeList nodeList = docment.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				String key = element.getAttribute("id").trim();
				String value = node.getFirstChild().getNodeValue().trim();
				configMap.put(key, value);
			}
		}
		//
		Object defInstance = clazz.newInstance();
		//
		Field[] fieldArray = defInstance.getClass().getDeclaredFields();
		for (Entry<String, String> entry : configMap.entrySet()) {
			for (Field field : fieldArray) {
				if (field.isAnnotationPresent(Def.class)) {
					Def def = field.getAnnotation(Def.class);
					if (def.value().equals(entry.getKey())) {
						field.setAccessible(true);
						Class<?> type = field.getType();
						if (type == int.class) {
							field.set(defInstance, Integer.parseInt(entry.getValue()));
						} else if (type == String.class) {
							field.set(defInstance, entry.getValue());
						} else if (type == boolean.class) {
							field.set(defInstance, Boolean.parseBoolean(entry.getValue()));
						} else if (type == long.class) {
							field.set(defInstance, Long.parseLong(entry.getValue()));
						} else if (type == double.class) {
							field.set(defInstance, Double.parseDouble(entry.getValue()));
						} else if (type == float.class) {
							field.set(defInstance, Float.parseFloat(entry.getValue()));
						} else if (type == String[].class) {
							String[] value = Value.parserStringToArray(entry.getValue());
							field.set(defInstance, value);
						} else if (type == int[].class) {
							int[] value = Value.parserStringToIntArray(entry.getValue());
							field.set(defInstance, value);
						} else if (type == long[].class) {
							long[] value = Value.parserStringToLongArray(entry.getValue());
							field.set(defInstance, value);
						}
					}
				}
			}
		}
		//
		addDefInstance((BaseDef<?, ?>) defInstance);
	}

	/**
	 * 读取json配置
	 *
	 * @param configFile
	 * @param clazz
	 * @throws IOException
	 */
	private void parserJSONConfig(InputStream configFile, Class<?> clazz) throws IOException {
		Def headDef = clazz.getAnnotation(Def.class);
		String config = headDef.value();
		String[] arr = config.split("\\.");
		String jsonText = FileUtil.fileToString(configFile);
		JSONObject json = null;
		try {
			json = JSON.parseObject(jsonText);
		} catch (Exception e) {
			logger.error("[PARSE JSON ERR]FILE[" + configFile + "]CLASS[" + clazz + "]", e);
		}
		String jsonString = json.getString(arr[0]);
		if (jsonString.startsWith("[")) {
			List<?> list = null;
			try {
				list = JSON.parseArray(jsonString, clazz);
			} catch (Exception e) {
				logger.error("[PARSE JSON ERR]FILE[" + configFile + "]CLASS[" + clazz + "]", e);
			}
			for (Object object : list) {
				addDefInstance((BaseDef<?, ?>) object);
			}
		} else {
			Object object = JSON.parseObject(jsonString, clazz);
			addDefInstance((BaseDef<?, ?>) object);
		}
	}

	/**
	 * 读取properties配置文件
	 *
	 * @param configFile
	 * @param clazz
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws NumberFormatException
	 * @throws InstantiationException
	 */
	private void parserPropertiesConfig(InputStream configFile, Class<?> clazz)
			throws NumberFormatException, IllegalArgumentException, IllegalAccessException, InstantiationException {
		Map<String, String> configMap = FileUtil.readProperties(configFile);
		Object defInstance = clazz.newInstance();
		//
		Field[] fieldArray = defInstance.getClass().getDeclaredFields();
		for (Entry<String, String> entry : configMap.entrySet()) {
			for (Field field : fieldArray) {
				if (field.isAnnotationPresent(Def.class)) {
					Def def = field.getAnnotation(Def.class);
					if (def.value().equals(entry.getKey())) {
						field.setAccessible(true);
						Class<?> type = field.getType();
						if (type == int.class) {
							field.set(defInstance, Integer.parseInt(entry.getValue()));
						} else if (type == String.class) {
							field.set(defInstance, entry.getValue());
						} else if (type == boolean.class) {
							field.set(defInstance, Boolean.parseBoolean(entry.getValue()));
						} else if (type == long.class) {
							field.set(defInstance, Long.parseLong(entry.getValue()));
						} else if (type == double.class) {
							field.set(defInstance, Double.parseDouble(entry.getValue()));
						}
						break;
					}
				}
			}
		}
		//
		addDefInstance((BaseDef<?, ?>) defInstance);
	}

	/**
	 * 将配置添加到Map里
	 *
	 * @param def
	 */
	private void addDefInstance(BaseDef<?, ?> def) {
		if (map.get(def.type()) == null) {
			map.put((IDefType) def.type(), new HashMap<Object, Object>());
		}
		map.get(def.type()).put(def.id(), def);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
}

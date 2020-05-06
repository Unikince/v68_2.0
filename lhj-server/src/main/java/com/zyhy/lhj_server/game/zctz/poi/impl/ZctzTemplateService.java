package com.zyhy.lhj_server.game.zctz.poi.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import com.zyhy.common_server.util.StringUtils;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.game.zctz.poi.ITemplateService;

/**
 * 
 * @Description 类描述
 * @author
 * @date 2013-7-2 下午04:28:37
 */
@Service
public class ZctzTemplateService implements ITemplateService {

	/** 所有通过模板文件转换而成的模板对象的实例 */
	private Map<Class<?>, Map<Object, TemplateObject>> templateObjects;
	private TemplateFileParser objectsParser;
	private String resourceFolder = "";
	
	public ZctzTemplateService() {
		objectsParser = new TemplateFileParser();
		try {
			if (Constants.ISDEBUG) {
				String path = System.getProperties().getProperty("user.dir");
				// 测试
				if( !path.endsWith(File.separator) ) {
					path += File.separator;
				}
				path += "src" + File.separator + "main" + File.separator + "resources" + File.separator + "script" + File.separator;
				resourceFolder = path;
				objectsParser = new TemplateFileParser();
				String cfgPath = resourceFolder + File.separator + "zctztemplates.xml";
				FileInputStream fileInputStream = new FileInputStream(cfgPath);
				this.init(fileInputStream);
			}else {
				// 正式 jar
				this.init(this.getClass().getResourceAsStream("/script/zctztemplates.xml"));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * (non-Javadoc)
	 * 
	 * @see com.zyhy.game_server.poi.mop.fish.core.template.ITemplateService#add(com.zyhy.game_server.poi.impl.mop.fish.core.template.TemplateObject)
	 */

	public <T extends TemplateObject> void add(T t) {
		Map<Object, TemplateObject> objs = templateObjects.get(t.getClass());
		if (objs == null || objs.containsKey(t.getId())) {
			return;
		}
		objs.put(t.getId(), t);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.zyhy.game_server.poi.mop.fish.core.template.ITemplateService#get(int, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T extends TemplateObject> T get(Object id, Class<T> clazz) {
		Map<Object, TemplateObject> map = templateObjects.get(clazz);
		return (T) map.get(id);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.zyhy.game_server.poi.mop.fish.core.template.ITemplateService#getAll(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T extends TemplateObject> Map<Object, T> getAll(Class<T> clazz) {
		return (Map<Object, T>) templateObjects.get(clazz);
	}
	
	/**
	 * 返回list
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public <T extends TemplateObject> List<T> getList(Class<T> clazz) {
		Map<Object, T> map = this.getAll(clazz);
		List<T> list = new ArrayList<T>();
		for (Map.Entry<Object, T> entry : map.entrySet()) {
		   list.add(entry.getValue());
		}
		return list;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.zyhy.game_server.poi.mop.fish.core.template.ITemplateService#init(java.net.URL)
	 */
	public void init(InputStream cfgFile) {
		List<TemplateConfig> templateConfigs = this.loadConfig(cfgFile);
		templateObjects = new HashMap<Class<?>, Map<Object, TemplateObject>>();
		
		InputStream is = null;
		String fileName = null;
		BufferedReader br = null;
		
		for (TemplateConfig cfg : templateConfigs) {
			try {
				fileName = cfg.getFileName();
				if (fileName == null) {
					this.getTemplateParser(cfg).parseXlsFile(cfg.getClasses(), templateObjects,
							null);
					continue;
				}
				if (!"".equals(resourceFolder)) {
					String path = resourceFolder + File.separator + cfg.getFileName();
					is = new FileInputStream(path);
				}else {
					is = this.getClass().getResourceAsStream("/script/" + cfg.getFileName());
				}
				if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
					this.getTemplateParser(cfg).parseXlsFile(cfg.getClasses(), templateObjects, is);
				}else if (fileName.contains(".json")) {
					InputStreamReader isr = new InputStreamReader(is, "UTF-8");
					br = new BufferedReader(isr);
					this.getTemplateParser(cfg).parseJsonArray(cfg.getClasses(), templateObjects, br);
				}
				System.out.println(fileName);
			} catch (Exception e) {
				e.printStackTrace();
				try {
					System.exit(0);
				} catch (Exception e2) {
				}
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		patchUpAndCheck();
	}

	/**
	 * 进行合法性校验，并构建模板间对象依赖关系
	 */
	private void patchUpAndCheck() {
		boolean hasError = false;
		Collection<Map<Object, TemplateObject>> tplObjMaps = templateObjects.values();
		for (Map<Object, TemplateObject> tplObjMap : tplObjMaps) {
			Collection<TemplateObject> templates = tplObjMap.values();
			for (TemplateObject templateObject : templates) {
				try {
					templateObject.patchUp();
				} catch (Exception e) {
					hasError = true;
				}
			}
		}
		for (Map<Object, TemplateObject> tplObjMap : tplObjMaps) {
			Collection<TemplateObject> templates = tplObjMap.values();
			for (TemplateObject templateObject : templates) {
				try {
					templateObject.check();
				} catch (Exception e) {
					hasError = true;
				}
			}
		}
		if (hasError) {
			System.exit(1);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.zyhy.game_server.poi.mop.fish.core.template.ITemplateService#isTemplateExist(int, java.lang.Class)
	 */
	public <T extends TemplateObject> boolean isTemplateExist(Object id, Class<T> clazz) {
		Map<Object, TemplateObject> map = templateObjects.get(clazz);
		if (null != map) {
			return null == map.get(id) ? false : true;
		}
		return false;
	}

	/**
	 * 返回所有类别的template列表
	 * 
	 * @return
	 */
	public Map<Class<?>, Map<Object, TemplateObject>> getAllClassTemplateMaps() {
		return Collections.unmodifiableMap(templateObjects);
	}

	/**
	 * 加载配置文件
	 * 
	 * @param cfgPath
	 */
	@SuppressWarnings("unchecked")
	private List<TemplateConfig> loadConfig(InputStream file) {
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(file);
		
			Element root = doc.getRootElement();
			List<TemplateConfig> templateConfigs = new ArrayList<TemplateConfig>();
			List<Element> fileElements = root.elements("file");
			for (Element fileElement : fileElements) {
				String fileName = fileElement.attributeValue("name");
				String parserClassName = fileElement.attributeValue("parser");
				List<Element> sheetElements = fileElement.elements("sheet");
				Class<?>[] fileSheetClasses = new Class<?>[sheetElements.size()];
				for (int i = 0; i < sheetElements.size(); i++) {
					Element sheet = sheetElements.get(i);
					String className = sheet.attributeValue("class");
					if (className == null || (className.trim().length() == 0)) {
						fileSheetClasses[i] = null;
						continue;
					}
					try {
						Class<?> clazz = Class.forName(className);
						fileSheetClasses[i] = clazz;
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						throw new ConfigException(e);
					}
				}
				TemplateConfig templateConfig = new TemplateConfig(fileName, fileSheetClasses);
				if (!StringUtils.isEmpty(parserClassName)) {
					templateConfig.setParserClassName(parserClassName.trim());
				}
				templateConfigs.add(templateConfig);
			}
			return templateConfigs;
		} catch (DocumentException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		
		return null;
	}

	/**
	 * 根据配置取得解析器
	 * 
	 * @param cfg
	 * @return
	 */
	private TemplateFileParser getTemplateParser(TemplateConfig cfg) {
		if (!StringUtils.isEmpty(cfg.getParserClassName())) {
			// 使用指定的解析器
			try {
				Class<?> clazz = Class.forName(cfg.getParserClassName());
				Constructor<?> constructor = clazz.getConstructor();
				return (TemplateFileParser) constructor.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			// 默认的解析器
			return objectsParser;
		}
	}

	@Override
	public <T extends TemplateObject> Map<Object, T> removeAll(Class<T> clazz) {
		return null;
	}
}

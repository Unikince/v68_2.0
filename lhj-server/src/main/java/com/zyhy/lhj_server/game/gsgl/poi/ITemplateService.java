package com.zyhy.lhj_server.game.gsgl.poi;

import java.io.InputStream;
import java.util.Map;

import com.zyhy.lhj_server.game.gsgl.poi.impl.TemplateObject;



/**
 * 
 * @Description 类描述
 * @author chen.su
 * @date 2013-7-2 下午04:28:09
 */
public interface ITemplateService {

	/**
	 * 初始化配置文件，并加载excel脚本
	 * 
	 * @param cfgpath
	 *            配置文件路径
	 */
	public void init(InputStream cfgFile);

	/**
	 * @param <T>
	 * @param id
	 * @param clazz
	 * @return
	 */
	public <T extends TemplateObject> T get(Object id, Class<T> clazz);

	/**
	 * @param <T>
	 * @param t
	 */
	public <T extends TemplateObject> void add(T t);

	/**
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public <T extends TemplateObject> Map<Object, T> getAll(Class<T> clazz);
	
	/**
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public <T extends TemplateObject> Map<Object, T> removeAll(Class<T> clazz);
	
	/**
	 * 
	 * @param <T>
	 * @param id
	 * @param clazz
	 * @return
	 */
	public <T extends TemplateObject> boolean isTemplateExist(Object id, Class<T> clazz);
	
}

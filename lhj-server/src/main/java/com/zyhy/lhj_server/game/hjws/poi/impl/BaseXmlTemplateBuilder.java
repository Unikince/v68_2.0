package com.zyhy.lhj_server.game.hjws.poi.impl;

import java.util.Map;



/** 
 * @description: 
 * @author 
 * @date 2011-6-10
 */

public interface BaseXmlTemplateBuilder {
	public void buildXml (String cfgPath, Map<Class<?>, Map<Object, TemplateObject>> templateObjects);
}

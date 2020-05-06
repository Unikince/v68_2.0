package com.zyhy.lhj_server.game.yhdd.poi.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/** 
 * @description: 简化了SGZ的加载，不使用注解，每个Template类自己维护一个Builder
 * @author liyuan
 * @date 2011-6-10
 */
public class TemplateFileParser {
	
	@SuppressWarnings("resource")
	public void parseXlsFile(Class<?>[] classes,
			Map<Class<?>, Map<Object, TemplateObject>> templateObjects,
			InputStream is) throws Exception {
		int i = 0;
		Workbook wb = null;
		try {
			wb = new XSSFWorkbook( is );
		} catch (Throwable e2) {
			e2.printStackTrace();
			wb = new HSSFWorkbook( is );
		} finally {
			is.close();
		}
		
		for (; i < classes.length; i++) {
			Sheet sheet = wb.getSheetAt(i);
			Class<?> curClazz = classes[i];
			if (curClazz == null)
				continue;
			Map<Object, TemplateObject> curSheetObjects = parseXlsSheet(sheet, curClazz);
			templateObjects.put(curClazz, curSheetObjects);
		}
	}
	
	protected void parseJsonArray(Class<?>[] classes, Map<Class<?>, Map<Object, TemplateObject>> templateObjects,
			BufferedReader br) {
		StringBuffer sb = new StringBuffer();
		String sname = null;  
		try {
			while ((sname = br.readLine()) != null) {  
				sb.append(sname.trim());
			}
			br.close();
			for (int i = 0; i < classes.length; i++) {
				Class<?> curClazz = classes[i];
				if (curClazz == null)
					continue;
				Map<Object, TemplateObject> curSheetObjects = parseJsonInfo(sb.toString(), curClazz);
				templateObjects.put(curClazz, curSheetObjects);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected Map<Object, TemplateObject> parseJsonInfo(String sb,
			Class<?> clazz) throws InstantiationException,IllegalAccessException{
		Map<Object, TemplateObject> map = new HashMap<Object, TemplateObject>(200, 0.8f);
		try {
			String loadClazzName = clazz.getName() + "Builder";
			Class<?> loadClazz=  Class.forName(loadClazzName);
			BaseJsonTemplateBuilder builder = (BaseJsonTemplateBuilder) loadClazz.newInstance();
			JSONObject jsonArray = JSONObject.parseObject(sb.toString());
			JSONArray jsonList = jsonArray.getJSONArray("list");
			for (int i = 0; i < jsonList.size(); i++) {
				JSONObject indexjson = jsonList.getJSONObject(i);
				if (indexjson == null || indexjson.isEmpty()) {
					continue;
				}
				TemplateObject to = builder.buildTemplate(indexjson);
				map.put(to.getId(), to);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	protected Map<Object, TemplateObject> parseXlsSheet(Sheet sheet,
			Class<?> clazz) throws InstantiationException,
			IllegalAccessException {
		Map<Object, TemplateObject> map = new HashMap<Object, TemplateObject>(
				200, 0.8f);
		String loadClazzName = clazz.getName() + "Builder";
		try {
			Class<?> loadClazz=  Class.forName(loadClazzName);
			BaseXlsTemplateBuilder builder = (BaseXlsTemplateBuilder) loadClazz.newInstance();
			// 第一行(原来的标题行)肯定有空,忽略不计
			for (int i = 1; i <= Short.MAX_VALUE; i++) {
				Row row = (Row)sheet.getRow(i);
				if (isEmpty(row)) {
					// 遇到空行就结束
					break;
				}
				TemplateObject obj = builder.buildTemplate(row);
				map.put(obj.getId(), obj);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	protected boolean isEmpty(Row row) {
		// 检测此行的第一个单元格是否为空
		if (row == null) {
			return true;
		}
		Cell cell = row.getCell(0);
		
		if( cell == null ) {
			return true;
		}
		return false;
	}
	
	public void parseXmlFile(Class<?>[] classes,
			Map<Class<?>, Map<Object, TemplateObject>> templateObjects,
			String path) throws Exception {
		int i = 0;
		for (; i < classes.length; i++) {
			Class<?> curClazz = classes[i];
			if (curClazz == null)
				continue;
			String loadClassName = curClazz.getName() + "Builder";
			Class<?> loadClazz = Class.forName(loadClassName);
			BaseXmlTemplateBuilder builder = (BaseXmlTemplateBuilder) loadClazz.newInstance();
			builder.buildXml(path, templateObjects);
		}
		
	}


}

package com.zyhy.lhj_server.game.lqjx.poi.impl;

import java.io.Serializable;




/**
 * 
 * 
 * @author zhangwh
 * @since 2010-4-8
 */
public abstract class TemplateObject implements Serializable {
	
	protected static final long serialVersionUID = 1L;

	public static int NULL_ID = 0;

	protected int id;
	
	public final int getId() {
		return id;
	}

	public final void setId(int id) {
		this.id = id;
	}

	/**
	 * <pre>
	 * 在{@link LqjxTemplateService}加载完所有的模板对象之后调用，主要用于检查各个模板
	 * 表配置是否正确，如果不正确，应抛出{@link ConfigException}类型的异常，并在异常
	 * 消息中记录详细的出错信息
	 * </pre>
	 * @throws TemplateConfigException
	 */
	public abstract void check() throws TemplateConfigException;

	/**
	 * <pre>
	 * 在{@link LqjxTemplateService}加载完所有的模板对象之后调用，主要用于构建各个模板
	 * 对象之间的依赖关系
	 * </pre>
	 * @throws Exception 
	 */
	public void patchUp() throws Exception {
	}
	
	/**
	 * 返回此模板的名字，可以写的更详细一点，哪个文件的那个页签
	 * 
	 * @return
	 */
	public String getTemplateName() {
		return "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TemplateObject other = (TemplateObject) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}

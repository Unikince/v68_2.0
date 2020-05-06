package com.dmg.gameconfigserver.model.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author mice
 * @email .com
 * @date 2019-12-03 14:45:22
 */
@Data
@TableName("gateway_routes")
public class GatewayRoutesBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 路由id
	 */
	private String routeId;
	/**
	 * 转发目标uri
	 */
	private String routeUri;
	/**
	 * 路由执行顺序
	 */
	private Integer routeOrder;
	/**
	 * 断言字符串集合，字符串结构：[{
                "name":"Path",
                "args":{
                   "pattern" : "/zy/**"
                }
              }]
	 */
	private String predicates;
	/**
	 * 过滤器字符串集合，字符串结构：{
              	"name":"StripPrefix",
              	 "args":{
              	 	"_genkey_0":"1"
              	 }
              }
	 */
	private String filters;
	/**
	 * 是否启用
	 */
	private Boolean isEbl;
	/**
	 * 是否删除
	 */
	private Boolean isDel;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;

}

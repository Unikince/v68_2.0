package com.dmg.lobbyserver.dao.bean;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author mice vnnn购买记录
 * @email .com
 * @date 2019-11-26 17:51:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_vnnn_recharge_log")
public class VnnnRechargeLogBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 请求流水id
	 */
	@TableId
	private String id;
	/**
	 * 用户code
	 */
	private String userCode;
	/**
	 * 游戏编号
	 */
	private String appid;
	/**
	 * 系统商品配置id
	 */
	private Long sysMallConfigId;
	/**
	 * 商品价格
	 */
	private Double itemPrice;
	/**
	 * 商品名称
	 */
	private String itemName;
	/**
	 * 请求类型
	 */
	private String action;
	/**
	 * vnnn请求源数据
	 */
	private String requestBody;
	/**
	 * 请求返回数据
	 */
	private String responseBody;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 请求次数
	 */
	private Integer requestTime;
	/**
	 * 订单状态
	 */
	private Integer orderStatus;

}

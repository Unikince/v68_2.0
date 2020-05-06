package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 版本控制
 * 
 * @author mice
 * @email .com
 * @date 2019-06-18 11:23:54
 */
@Data
@TableName("version_control")
public class VersionControlBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 心跳时间
	 */
	private Integer heartBeatTime;
	/**
	 * 
	 */
	private String version;
	/**
	 * 安卓分享地址
	 */
	private String shareAndroid;
	/**
	 * ios分享地址
	 */
	private String shareIos;
	/**
	 * 推广下载复制
	 */
	private String downLoadUrl;
	/**
	 * ios支付类型, 1 微信, 2 支付宝, 3 ios内置
	 */
	private String iosPayType;
	/**
	 * 安卓支付类型, 1 微信, 2 支付宝,
	 */
	private String androidPayType;
	/**
	 * 是否默认自动更新, 0 不自动更新
	 */
	private Integer androidDownload;
	/**
	 * 是否默认自动更新, 0 不自动更新
	 */
	private Integer iosDownload;

}

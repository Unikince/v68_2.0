package com.dmg.gameconfigserver.model.bean.config.bairen;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
@Data
@TableName("t_bairen_file_config")
public class BairenFileConfigBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 基础配置id
	 */
	private Integer fileBaseConfigId;
	/**
	 * 准入限制
	 */
	private Integer fileLimit;
	/**
	 * 房间下注条件下限
	 */
	private Integer roomBetLowLimit;

	/**
	 * 台红值
	 */
	private Integer redValue;
	/**
	 * 区域下注下限
	 */
	private Long areaBetLowLimit;
	/**
	 * 区域下注上限
	 */
	private Long areaBetUpLimit;
	/**
	 * 上庄下限
	 */
	private Integer applyBankerLimit;
	/**
	 * 自动下庄下限
	 */
	private Integer bankerGoldLowLimit;
	/**
	 * 连庄局数限制
	 */
	private Integer bankRoundLimit;
	/**
	 * 观战局数限制
	 */
	private Integer watchRoundLimit;
	/**
	 * 下注筹码
	 */
	private String betChips;

}

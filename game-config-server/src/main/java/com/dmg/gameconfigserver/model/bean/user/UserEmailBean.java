package com.dmg.gameconfigserver.model.bean.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:28 2020/3/23
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_dmg_user_email")
public class UserEmailBean implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 邮件id
	 */
	private Long emailId;
	/**
	 * 邮件名称
	 */
	private String emailName;
	/**
	 * 邮件内容信息
	 */
	private String emailContent;
	/**
	 * 过期时间
	 */
	private Date expireDate;
	/**
	 * 发送时间
	 */
	private Date sendDate;
	/**
	 * 物品类型
	 */
	private Integer itemType;
	/**
	 * 物品数量
	 */
	private String itemNum;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 是否领取
	 */
	private Boolean receive;
	/**
	 * 是否阅读
	 */
	private Boolean hasRead;
	/**
	 * 转账记录id
	 */
	private Long transferAccountId;
}

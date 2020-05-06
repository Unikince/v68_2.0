package com.dmg.clubserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 俱乐部战绩
 * 
 * @author mice
 * @email .com
 * @date 2019-05-25 14:18:53
 */
@Data
@TableName("club_game_record")
public class ClubGameRecordBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 俱乐部id
	 */
	private Integer clubId;
	/**
	 * 游戏id
	 */
	private Long gameId;
	/**
	 * 角色id
	 */
	private Integer roleId;
	/**
	 * 房间创建人id
	 */
	private Integer roomCreatorId;
	/**
	 * 游戏类型
	 */
	private Integer gameType;
	/**
	 * 房间id
	 */
	private Integer roomId;
	/**
	 * 分数
	 */
	private Integer score;
	/**
	 * 结束时间
	 */
	private Date endDate;
	/**
	 * 记录类型(1:总结算 2:明细)
	 */
	private Integer recordType;
	/**
	 * 场次
	 */
	private Integer round;
	/**
	 * 是否胡牌(0:未胡 1:胡)
	 */
	private Integer hu;
	/**
	 * 房卡消耗数量
	 */
	private Integer roomCardConsumeNum;

}

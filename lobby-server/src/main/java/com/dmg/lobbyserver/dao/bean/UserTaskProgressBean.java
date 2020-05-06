package com.dmg.lobbyserver.dao.bean;

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
 * 用户任务进度表
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_task_progress")
public class UserTaskProgressBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 任务id
	 */
	private Long taskId;
	/**
	 * 任务进度
	 */
	private Integer taskProgress;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 任务类型
	 */
	private Integer taskType;
	/**
	 * 是否领取奖励 0:可领取 1:未完成 2:已领取
	 */
	private Integer receiveAwardStatus;


}

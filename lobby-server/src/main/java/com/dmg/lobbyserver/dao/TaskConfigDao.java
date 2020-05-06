package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.TaskConfigBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * 任务配置表
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Mapper
public interface TaskConfigDao extends BaseMapper<TaskConfigBean> {
    /**
     * 查询活动任务
     */
	@Select("select *from task_config where id=#{id}  and task_type=5 and task_status=1 and  task_end_time>=#{time}")
    TaskConfigBean getBean(@Param("id")Integer id, @Param("time")Date time);
}

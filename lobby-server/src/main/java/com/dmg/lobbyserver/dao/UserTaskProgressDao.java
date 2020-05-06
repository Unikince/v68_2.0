package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.UserTaskProgressBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 用户任务进度表
 *
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Mapper
public interface UserTaskProgressDao extends BaseMapper<UserTaskProgressBean> {

    /**
     * @param userId
     * @return int
     * @description: 统计已经完成 且未领取奖励的任务数
     * @author mice
     * @date 2019/6/20
     */
    @Select("select count(1) from user_task_progress u left join task_config t on u.task_id=t.id where u.user_id=#{userId} and u.task_progress>=t.task_condition and u.receive_award_status=0")
    int countHasFinishAndNoRecieveAwardTasks(Long userId);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 14:10 2019/11/26
     **/
    @Update("update user_task_progress set task_progress =  CASE " +
            "WHEN (#{maxNumber} is not null and (task_progress + #{maxNumber}) >= #{taskCondition}) THEN #{taskCondition} " +
            "WHEN (#{maxNumber} is not null and (task_progress + #{maxNumber}) < #{taskCondition}) THEN (task_progress + #{maxNumber}) " +
            "WHEN ((task_progress + #{number}) >= #{taskCondition}) THEN #{taskCondition} ELSE (task_progress + #{number}) END where id = #{id}")
    void updateTaskProgress(Long id, Integer number, Integer taskCondition,Integer maxNumber);

}

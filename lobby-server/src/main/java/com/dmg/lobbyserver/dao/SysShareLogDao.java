package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.SysShareLogBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description 用户分享日志
 * @Author jock
 * @Date 2019/7/5 0005
 * @Version V1.0
 **/
@Mapper
public interface SysShareLogDao extends BaseMapper<SysShareLogBean> {
    /**
     * 今日是否分享
     */
    @Select("select * from sys_share_log where year(create_date)=year(now()) and month(create_date)=month(now()) and day(create_date)=day(now()) and user_id=#{userId} and share_type=1")
    SysShareLogBean coutShare(long userId);
}

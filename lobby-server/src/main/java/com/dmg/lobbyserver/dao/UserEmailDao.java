package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.UserEmailBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 17:11 2020/3/20
 */
@Mapper
public interface UserEmailDao extends BaseMapper<UserEmailBean> {

    @Update("update t_dmg_user_email set has_read = true where id = #{emailId}")
    int updateHasRead(Long emailId);

}

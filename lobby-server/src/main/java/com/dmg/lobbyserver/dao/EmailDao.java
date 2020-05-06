package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.EmailBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 邮件
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Mapper
public interface EmailDao extends BaseMapper<EmailBean> {
    /**
     * @description: 更新邮件_已读
     * @param emailId
     * @return int
     * @author mice
     * @date 2019/6/20
    */
    @Update("update email set has_read=1 where id = #{emailId}")
    int updateHasRead(Long emailId);
	
}

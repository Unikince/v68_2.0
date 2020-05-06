package com.dmg.clubserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.clubserver.dao.bean.ClubReportInfoBean;
import org.apache.ibatis.annotations.Mapper;

/**
 * 举报信息
 * 
 * @author mice
 * @email .com
 * @date 2019-06-05 15:54:21
 */
@Mapper
public interface ClubReportInfoDao extends BaseMapper<ClubReportInfoBean> {
	
}

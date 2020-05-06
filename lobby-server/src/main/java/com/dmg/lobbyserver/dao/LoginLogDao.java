package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.LoginLogBean;
import com.dmg.lobbyserver.dao.bean.SignLogBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface LoginLogDao extends BaseMapper<LoginLogBean> {
}

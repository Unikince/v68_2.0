package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.BankCardBean;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户银行卡
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Mapper
public interface BankCardDao extends BaseMapper<BankCardBean> {
	
}

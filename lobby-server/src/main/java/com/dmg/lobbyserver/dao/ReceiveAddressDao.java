package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.ReceiveAddressBean;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收货地址
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Mapper
public interface ReceiveAddressDao extends BaseMapper<ReceiveAddressBean> {
	
}

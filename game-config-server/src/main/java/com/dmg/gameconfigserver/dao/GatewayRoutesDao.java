package com.dmg.gameconfigserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.GatewayRoutesBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 
 * 
 * @author mice
 * @email .com
 * @date 2019-12-03 14:45:22
 */
@Mapper
public interface GatewayRoutesDao extends BaseMapper<GatewayRoutesBean> {

    @Select("select id from dynamic_route_version limit 1")
    Long selectVersion();

    @Update("update dynamic_route_version set id = (id+1),create_time = now()")
    void updateVersion();

    @Select("select route_id from gateway_routes where route_id like #{routeIdPrefix}")
    List<String> selectRouteIdsByRouteIdPrefix(String routeIdPrefix);

}

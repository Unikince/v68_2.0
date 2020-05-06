package com.dmg.gameconfigserver.service.gatewayroutes;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.gameconfigserver.dao.GatewayRoutesDao;
import com.dmg.gameconfigserver.model.bean.GatewayRoutesBean;
import com.dmg.gameconfigserver.model.dto.gatewayroutes.GatewayFilterDefinition;
import com.dmg.gameconfigserver.model.dto.gatewayroutes.GatewayPredicateDefinition;
import com.dmg.gameconfigserver.model.dto.gatewayroutes.GatewayRouteDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/3 14:47
 * @Version V1.0
 **/
@Service
@Slf4j
public class GatewayRoutesService {

    @Autowired
    private GatewayRoutesDao gatewayRoutesDao;

    @Cacheable(cacheNames = "ROUTE_VERSION",key = "1")
    public long getVersion(){
        return gatewayRoutesDao.selectVersion();
    }


    @CacheEvict(cacheNames = "ROUTE_VERSION",key = "1")
    public void updateVersion(){
        gatewayRoutesDao.updateVersion();
    }

    public List<GatewayRouteDefinition> getRoutes(){
        List<GatewayRoutesBean> gatewayRoutesBeans = gatewayRoutesDao.selectList(new LambdaQueryWrapper<>());
        List<GatewayRouteDefinition> gatewayRoutesDTOS = new ArrayList<>();
        for (GatewayRoutesBean gatewayRoutesBean : gatewayRoutesBeans){
            // 排除未启用 和 已删除的路由
            if (gatewayRoutesBean.getIsDel() || !gatewayRoutesBean.getIsEbl())continue;
            GatewayRouteDefinition gatewayRoutesDTO = new GatewayRouteDefinition();
            BeanUtils.copyProperties(gatewayRoutesBean,gatewayRoutesDTO);

            List<GatewayPredicateDefinition>  predicates = JSON.parseArray(gatewayRoutesBean.getPredicates() , GatewayPredicateDefinition.class);
            gatewayRoutesDTO.setPredicates(predicates);

            List<GatewayFilterDefinition>  filters = JSON.parseArray(gatewayRoutesBean.getFilters() , GatewayFilterDefinition.class);
            gatewayRoutesDTO.setFilters(filters);

            gatewayRoutesDTOS.add(gatewayRoutesDTO);
        }
        return gatewayRoutesDTOS;
    }


    public void closeRoutes(List<String> routeKeys){
        List<GatewayRoutesBean> gatewayRoutesBeans = gatewayRoutesDao.selectList(new LambdaQueryWrapper<GatewayRoutesBean>().in(GatewayRoutesBean::getRouteId,routeKeys));
        for (GatewayRoutesBean gatewayRoutesBean : gatewayRoutesBeans){
            // 更新启用标志
            gatewayRoutesBean.setIsEbl(false);
            gatewayRoutesDao.updateById(gatewayRoutesBean);
        }

        log.warn("关闭路由:{}",routeKeys);
    }

    public void closeRoutesByRouteIdPrefix(String routeIdPrefix){
        List<String> routeIds = gatewayRoutesDao.selectRouteIdsByRouteIdPrefix(routeIdPrefix.concat("%"));
        if (CollectionUtils.isEmpty(routeIds)){
            return;
        }
        this.closeRoutes(routeIds);
    }

    public void openRoutesByRouteIdPrefix(String routeIdPrefix){
        List<GatewayRoutesBean> gatewayRoutesBeans = gatewayRoutesDao.selectList(new LambdaQueryWrapper<GatewayRoutesBean>().likeRight(GatewayRoutesBean::getRouteId,routeIdPrefix));
        if (CollectionUtils.isEmpty(gatewayRoutesBeans))return;

        for (GatewayRoutesBean gatewayRoutesBean : gatewayRoutesBeans){
            // 更新启用标志
            gatewayRoutesBean.setIsEbl(true);
            gatewayRoutesDao.updateById(gatewayRoutesBean);
        }
    }


}
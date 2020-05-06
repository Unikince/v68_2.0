package com.dmg.clubserver.service;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson.JSON;
import com.dmg.clubserver.model.RoleInfoBean;
import com.zyhy.common_server.util.StringUtils;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/27 10:33
 * @Version V1.0
 **/
@Slf4j
@Service
public class SynchronousPlayerDataService {
    @Value("${lobby_server.ip}")
    private String lobby_server_ip;
    @Value("${lobby_server.http_port}")
    private String lobby_server_http_port;

    /**
     * @description:
     * @param roleIds
     * @return java.util.List<com.dmg.clubserver.model.RoleInfoBean>
     * @author mice
     * @date 2019/5/27
    */
    public List<RoleInfoBean> synchronousPlayerData(String roleIds){
        if (StringUtils.isEmpty(roleIds)){
            return new ArrayList<>();
        }
        String result = HttpUtil.get(lobby_server_ip+":"+lobby_server_http_port+"/synchronousPlayerData?roleIds="+roleIds);
        log.info(result);
        List<RoleInfoBean> roleInfos = JSON.parseArray(result,RoleInfoBean.class);
        return roleInfos;
    }

    /**
     * @description: 同步一个玩家的信息
     * @param roleId
     * @return com.dmg.clubserver.model.RoleInfoBean
     * @author mice
     * @date 2019/5/27
    */
    public RoleInfoBean getOnePlayerInfo(String roleId){
        if (StringUtils.isEmpty(roleId)){
            return null;
        }
        String result = HttpUtil.get(lobby_server_ip+":"+lobby_server_http_port+"/synchronousPlayerData?roleIds="+roleId);
        log.info(result);
        List<RoleInfoBean> roleInfos = JSON.parseArray(result,RoleInfoBean.class);
        if (CollectionUtils.isEmpty(roleInfos)){
            return null;
        }
        return roleInfos.get(0);
    }

}
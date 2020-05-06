package com.dmg.gameconfigserver.service.api;



import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/10/17 11:25
 * @Version V1.0
 **/
public interface ResourceServerApiService {



    /**
     * @description: 根据部署环境获取资源服务器列表
     * @param deployEnvironment
     * @return java.util.List<java.lang.String>
     * @author mice
     * @date 2019/10/25
    */
    List<String> getResourceServerList(Integer deployEnvironment);
}
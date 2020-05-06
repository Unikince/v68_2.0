package com.dmg.lobbyserver.service;

import com.dmg.lobbyserver.dao.bean.SysMallConfigBean;
import com.dmg.lobbyserver.model.dto.SysMallConfigDTO;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/26 15:58
 * @Version V1.0
 **/
public interface SysMallConfigService {

    /**
     * @description: 获取商城配置详情列表
     * @param
     * @return java.util.List<com.dmg.lobbyserver.model.dto.SysMallConfigDTO>
     * @author mice
     * @date 2019/11/26
    */
    List<SysMallConfigDTO> getSysMallConfig();

    /**
     * @description: 获取商品信息
     * @param id
     * @return com.dmg.lobbyserver.dao.bean.SysMallConfigBean
     * @author mice
     * @date 2019/11/26
    */
    SysMallConfigBean getSysMallConfigById(Long id);

    SysMallConfigBean getSysMallConfigByThirdId(String thirdId);
}
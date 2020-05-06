package com.dmg.gameconfigserver.service.config;


import com.dmg.gameconfigserver.model.bean.config.FileBaseConfigBean;
import com.dmg.gameconfigserver.model.bean.config.bairen.BairenFileConfigBean;
import com.dmg.gameconfigserver.model.dto.config.BairenFileConfigDTO;

import java.util.List;

/**
 * 
 *
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
public interface BairenFileConfigService {

    /**
     * @description: 获取场次配置详情
     * @param fileBaseConfigId
     * @return com.dmg.gameconfigserver.model.dto.config.BairenFileConfigDTO
     * @author mice
     * @date 2019/10/10
    */
    BairenFileConfigDTO getOne(int fileBaseConfigId);

    List<BairenFileConfigDTO> getFileConfigList(int gameId);

    void insert(FileBaseConfigBean fileBaseConfigBean, BairenFileConfigBean bairenFileConfigBean);

    void update(FileBaseConfigBean fileBaseConfigBean, BairenFileConfigBean bairenFileConfigBean);

    void delete(int fileBaseConfigId);



}


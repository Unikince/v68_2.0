package com.dmg.gameconfigserver.service.config;

import com.dmg.gameconfigserver.model.bean.config.bairen.BairenControlConfigBean;
import com.dmg.gameconfigserver.model.dto.config.BairenControlConfigDTO;

/**
 * 
 *
 * @author mice
 * @email .com
 * @date 2019-09-26 17:09:15
 */
public interface BairenControlConfigService {

    void insert(BairenControlConfigBean bairenControlConfigBean);

    void update(BairenControlConfigBean bairenControlConfigBean);

    void delete(Integer fileBaseConfigId);

    BairenControlConfigDTO getOne(Integer fileBaseConfigId);
}


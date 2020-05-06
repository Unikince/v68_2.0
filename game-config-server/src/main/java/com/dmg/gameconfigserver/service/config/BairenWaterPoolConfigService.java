package com.dmg.gameconfigserver.service.config;

import com.dmg.gameconfigserver.model.bean.config.bairen.BairenWaterPoolConfigBean;

import java.util.List;

/**
 * 
 *
 * @author mice
 * @email .com
 * @date 2019-09-26 17:09:15
 */
public interface BairenWaterPoolConfigService {

    void insert(BairenWaterPoolConfigBean bairenWaterPoolConfigBean);

    void update(BairenWaterPoolConfigBean bairenWaterPoolConfigBean);

    void delete(Integer id);

    void deleteByFileBaseConfigId(Integer fileBaseConfigId);

    List<BairenWaterPoolConfigBean> getList(Integer fileBaseConfigId);
}


package com.dmg.gameconfigserver.service.config;


import com.dmg.gameconfigserver.model.bean.config.FileBaseConfigBean;

import java.util.List;

/**
 * 
 *
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
public interface FileBaseConfigService {

    /**
     * @Author liubo
     * @Description //TODO 根据游戏id查询房间配置
     * @Date 11:21 2019/10/10
     **/
    List<FileBaseConfigBean> getBaseConfigByGameId(Integer gameId);


}


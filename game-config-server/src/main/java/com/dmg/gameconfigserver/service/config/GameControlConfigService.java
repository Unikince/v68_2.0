package com.dmg.gameconfigserver.service.config;

import com.dmg.gameconfigserver.model.dto.config.GameControlConfigDTO;
import com.dmg.gameconfigserver.model.vo.config.GameControlVO;


/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:10 2019/10/11
 */
public interface GameControlConfigService {

    /**
     * @Author liubo
     * @Description //TODO 根据基础配置id查询配置信息
     * @Date 11:56 2019/10/14
     **/
    GameControlConfigDTO getGameControlByBaseId(Integer baseId);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    void insert(GameControlVO vo);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    void update(GameControlVO vo);

    /**
     * @Author liubo
     * @Description //TODO 根据id删除
     * @Date 16:37 2019/9/27
     **/
    void deleteById(Integer baseId);

}

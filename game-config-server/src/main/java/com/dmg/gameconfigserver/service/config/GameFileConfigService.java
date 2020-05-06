package com.dmg.gameconfigserver.service.config;

import com.dmg.gameconfigserver.model.bean.config.FileBaseConfigBean;
import com.dmg.gameconfigserver.model.bean.config.GameFileBean;
import com.dmg.gameconfigserver.model.dto.config.GameFileDTO;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:10 2019/10/11
 */
public interface GameFileConfigService {

    /**
     * @Author liubo
     * @Description //TODO 根据游戏id查询游戏配置
     * @Date 14:16 2019/10/11
     **/
    List<GameFileDTO> getGameFileByGameId(Integer gameId);

    /**
     * @Author liubo
     * @Description //TODO 根据基础配置id查询配置信息
     * @Date 11:56 2019/10/14
     **/
    GameFileDTO getGameFileByBaseId(Integer baseId);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    void insert(FileBaseConfigBean fileBaseConfigBean, GameFileBean gameFileBean);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    void update(FileBaseConfigBean fileBaseConfigBean, GameFileBean gameFileBean);

    /**
     * @Author liubo
     * @Description //TODO 根据id删除
     * @Date 16:37 2019/9/27
     **/
    void deleteById(Integer baseId);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 17:36 2020/3/16
     **/
    List<FileBaseConfigBean> getFileDetailByGameId(Integer gameId);

}

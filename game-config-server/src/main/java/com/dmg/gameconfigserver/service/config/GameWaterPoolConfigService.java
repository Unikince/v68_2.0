package com.dmg.gameconfigserver.service.config;

import com.dmg.gameconfigserver.model.bean.config.GameWaterPoolConfigBean;
import com.dmg.gameconfigserver.model.dto.config.GameWaterPoolByWaterDTO;
import com.dmg.gameconfigserver.model.vo.config.GameWaterPoolConfigVO;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:32 2019/9/27
 */
public interface GameWaterPoolConfigService {

    /**
     * @Author liubo
     * @Description //TODO  获取所以游戏水池配置
     * @Date 16:34 2019/9/27
     **/
    List<GameWaterPoolConfigBean> getAllList();

    /**
     * @Author liubo
     * @Description //TODO  获取所以游戏水池配置
     * @Date 16:34 2019/9/27
     **/
    GameWaterPoolConfigBean getGameWaterPoolById(Integer id);

    /**
     * @Author liubo
     * @Description //TODO  获取所以游戏水池配置
     * @Date 16:34 2019/9/27
     **/
    List<GameWaterPoolConfigBean> getGameWaterPoolByGame(Integer gameId);

    /**
     * @Author liubo
     * @Description //TODO  获取所以游戏水池配置
     * @Date 16:34 2019/9/27
     **/
    List<GameWaterPoolConfigBean> getGameWaterPoolByGame(Integer gameId, Integer fileId);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    void insert(GameWaterPoolConfigBean gameWaterPoolConfigBean);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    void update(GameWaterPoolConfigVO vo);

    /**
     * @Author liubo
     * @Description //TODO 根据id删除
     * @Date 16:37 2019/9/27
     **/
    void deleteById(Integer id);

    /**
     * @Author liubo
     * @Description //TODO 根据金额&房间等级&游戏id查询水池配置
     * @Date 17:50 2019/9/29
     **/
    GameWaterPoolConfigBean getInfoByWater(GameWaterPoolByWaterDTO gameWaterPoolByWaterDTO);
}

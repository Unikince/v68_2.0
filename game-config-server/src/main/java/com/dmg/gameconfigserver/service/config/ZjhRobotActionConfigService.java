package com.dmg.gameconfigserver.service.config;

import com.dmg.gameconfigserver.model.bean.config.zjh.ZjhRobotActionConfigBean;
import com.dmg.gameconfigserver.model.vo.config.zjh.ZjhRobotActionConfigVO;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:45 2019/9/27
 */
public interface ZjhRobotActionConfigService {
    /**
     * @Author liubo
     * @Description //TODO  获取扎金花机器人动作概率配置
     * @Date 16:34 2019/9/27
     **/
    List<ZjhRobotActionConfigBean> getAllList();

    /**
     * @Author liubo
     * @Description //TODO  获取扎金花机器人动作概率配置
     * @Date 16:34 2019/9/27
     **/
    ZjhRobotActionConfigBean getZjhRobotActionById(Integer id);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    void insert(ZjhRobotActionConfigBean zjhRobotActionConfigBean);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    void update(ZjhRobotActionConfigVO vo);

    /**
     * @Author liubo
     * @Description //TODO 根据id删除
     * @Date 16:37 2019/9/27
     **/
    void deleteById(Integer id);

    /**
     * @Author liubo
     * @Description //TODO  根据是否看牌获取扎金花机器人动作概率配置
     * @Date 16:41 2019/9/30
     **/
    ZjhRobotActionConfigBean getRobotActionByIsSee(Boolean isSee);

    /**
     * @Author liubo
     * @Description //TODO 根据牌型获取扎金花机器人动作概率配置
     * @Date 16:42 2019/9/30
     **/
    ZjhRobotActionConfigBean getRobotActionByCard(Integer cardType, Integer card);


}

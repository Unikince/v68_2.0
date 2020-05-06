package com.dmg.gameconfigserver.service.config;

import com.dmg.gameconfigserver.model.bean.config.zjh.ZjhRobotSeeConfigBean;
import com.dmg.gameconfigserver.model.vo.config.zjh.ZjhRobotSeeConfigVO;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:43 2019/9/27
 */
public interface ZjhRobotSeeConfigService {
    /**
     * @Author liubo
     * @Description //TODO  获取扎金花机器人是否看牌概率配置
     * @Date 16:34 2019/9/27
     **/
    List<ZjhRobotSeeConfigBean> getAllList();

    /**
     * @Author liubo
     * @Description //TODO  获取扎金花机器人是否看牌概率配置
     * @Date 16:34 2019/9/27
     **/
    ZjhRobotSeeConfigBean getZjhRobotSeeById(Integer id);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    void insert(ZjhRobotSeeConfigBean zjhRobotSeeConfigBean);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    void update(ZjhRobotSeeConfigVO vo);

    /**
     * @Author liubo
     * @Description //TODO 根据id删除
     * @Date 16:37 2019/9/27
     **/
    void deleteById(Integer id);

    /**
     * @Author liubo
     * @Description //TODO 根据轮数获取扎金花机器人是否看牌概率配置
     * @Date 16:51 2019/9/30
     **/
    ZjhRobotSeeConfigBean getRobotSeeByRound(Integer round);
}

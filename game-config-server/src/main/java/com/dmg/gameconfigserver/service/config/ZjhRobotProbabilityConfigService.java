package com.dmg.gameconfigserver.service.config;

import com.dmg.gameconfigserver.model.bean.config.zjh.ZjhRobotProbabilityConfigBean;
import com.dmg.gameconfigserver.model.vo.config.zjh.ZjhRobotProbabilityConfigVO;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:41 2019/9/27
 */
public interface ZjhRobotProbabilityConfigService {
    /**
     * @Author liubo
     * @Description //TODO  获取扎金花机器人基础概率配置
     * @Date 16:34 2019/9/27
     **/
    List<ZjhRobotProbabilityConfigBean> getAllList();

    /**
     * @Author liubo
     * @Description //TODO  获取扎金花机器人基础概率配置
     * @Date 16:34 2019/9/27
     **/
    ZjhRobotProbabilityConfigBean getZjhRobotProbabilityById(Integer id);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    void insert(ZjhRobotProbabilityConfigBean zjhRobotProbabilityConfigBean);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    void update(ZjhRobotProbabilityConfigVO vo);

    /**
     * @Author liubo
     * @Description //TODO 根据id删除
     * @Date 16:37 2019/9/27
     **/
    void deleteById(Integer id);
}

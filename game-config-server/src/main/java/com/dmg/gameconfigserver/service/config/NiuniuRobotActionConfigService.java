package com.dmg.gameconfigserver.service.config;

import com.dmg.gameconfigserver.model.bean.config.niuniu.NiuniuRobotActionConfigBean;
import com.dmg.gameconfigserver.model.vo.config.niuniu.NiuniuRobotActionConfigVO;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:39 2019/9/27
 **/
public interface NiuniuRobotActionConfigService {

    /**
     * @Author liubo
     * @Description //TODO  获取抢庄牛牛机器人动作配置
     * @Date 16:34 2019/9/27
     **/
    List<NiuniuRobotActionConfigBean> getAllList();

    /**
     * @Author liubo
     * @Description //TODO  获取抢庄牛牛机器人动作配置
     * @Date 16:34 2019/9/27
     **/
    NiuniuRobotActionConfigBean getNiuniuRobotActionById(Integer id);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    void insert(NiuniuRobotActionConfigBean niuniuRobotActionConfigBean);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    void update(NiuniuRobotActionConfigVO vo);

    /**
     * @Author liubo
     * @Description //TODO 根据id删除
     * @Date 16:37 2019/9/27
     **/
    void deleteById(Integer id);

    /**
     * @Author liubo
     * @Description //TODO 根据牌型查询抢庄牛牛机器人动作配置
     * @Date 18:28 2019/9/29
     **/
    List<NiuniuRobotActionConfigBean> getRobInfoByCard(Integer card);

    /**
     * @Author liubo
     * @Description //TODO 根据牌型及抢类型查询抢庄牛牛机器人动作配置
     * @Date 18:33 2019/9/29
     **/
    List<NiuniuRobotActionConfigBean> getPressureInfoByRob(Integer card, Integer robType);
}

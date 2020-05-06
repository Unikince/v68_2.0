package com.dmg.gameconfigserver.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.dao.config.zjh.ZjhRobotProbabilityConfigDao;
import com.dmg.gameconfigserver.model.bean.config.zjh.ZjhRobotProbabilityConfigBean;
import com.dmg.gameconfigserver.model.vo.config.zjh.ZjhRobotProbabilityConfigVO;
import com.dmg.gameconfigserver.service.config.ZjhRobotProbabilityConfigService;
import com.dmg.server.common.util.DmgBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 17:04 2019/9/27
 */
@Service
public class ZjhRobotProbabilityConfigServiceImpl implements ZjhRobotProbabilityConfigService {

    @Autowired
    private ZjhRobotProbabilityConfigDao zjhRobotProbabilityConfigDao;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<ZjhRobotProbabilityConfigBean> getAllList() {
        return zjhRobotProbabilityConfigDao.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public ZjhRobotProbabilityConfigBean getZjhRobotProbabilityById(Integer id) {
        return zjhRobotProbabilityConfigDao.selectById(id);
    }

    @Override
    public void insert(ZjhRobotProbabilityConfigBean zjhRobotProbabilityConfigBean) {
        zjhRobotProbabilityConfigBean.setCreateDate(new Date());
        zjhRobotProbabilityConfigBean.setModifyDate(new Date());
        zjhRobotProbabilityConfigDao.insert(zjhRobotProbabilityConfigBean);
        this.redisData();
    }

    @Override
    public void update(ZjhRobotProbabilityConfigVO vo) {
        ZjhRobotProbabilityConfigBean zjhRobotProbabilityConfigBean = zjhRobotProbabilityConfigDao.selectById(vo.getId());
        if(zjhRobotProbabilityConfigBean == null){
            return;
        }
        DmgBeanUtils.copyProperties(vo, zjhRobotProbabilityConfigBean);
        zjhRobotProbabilityConfigBean.setModifyDate(new Date());
        zjhRobotProbabilityConfigDao.updateById(zjhRobotProbabilityConfigBean);
        this.cacheRemove();
        this.redisData();
    }

    @Override
    public void deleteById(Integer id) {
        zjhRobotProbabilityConfigDao.deleteById(id);
        this.cacheRemove();
        this.redisData();
    }

    @CacheEvict(cacheNames = Constant.ZJH_ROBOT_PROBABILITY)
    public void cacheRemove() {
    }

    private void redisData() {
        redisUtil.lSet(Constant.ZJH_ROBOT_PROBABILITY, this.getAllList());
    }
}

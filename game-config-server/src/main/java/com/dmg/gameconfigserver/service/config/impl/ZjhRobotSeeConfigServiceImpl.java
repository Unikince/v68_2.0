package com.dmg.gameconfigserver.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.dao.config.zjh.ZjhRobotSeeConfigDao;
import com.dmg.gameconfigserver.model.bean.config.zjh.ZjhRobotSeeConfigBean;
import com.dmg.gameconfigserver.model.vo.config.zjh.ZjhRobotSeeConfigVO;
import com.dmg.gameconfigserver.service.config.ZjhRobotSeeConfigService;
import com.dmg.server.common.util.DmgBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 17:06 2019/9/27
 */
@Service
public class ZjhRobotSeeConfigServiceImpl implements ZjhRobotSeeConfigService {

    @Autowired
    private ZjhRobotSeeConfigDao zjhRobotSeeConfigDao;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<ZjhRobotSeeConfigBean> getAllList() {
        return zjhRobotSeeConfigDao.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public ZjhRobotSeeConfigBean getZjhRobotSeeById(Integer id) {
        return zjhRobotSeeConfigDao.selectById(id);
    }

    @Override
    public void insert(ZjhRobotSeeConfigBean zjhRobotSeeConfigBean) {
        zjhRobotSeeConfigBean.setCreateDate(new Date());
        zjhRobotSeeConfigBean.setModifyDate(new Date());
        zjhRobotSeeConfigDao.insert(zjhRobotSeeConfigBean);
        this.redisData();
    }

    @Override
    public void update(ZjhRobotSeeConfigVO vo) {
        ZjhRobotSeeConfigBean zjhRobotSeeConfigBean = zjhRobotSeeConfigDao.selectById(vo.getId());
        if(zjhRobotSeeConfigBean == null){
            return;
        }
        DmgBeanUtils.copyProperties(vo, zjhRobotSeeConfigBean);
        zjhRobotSeeConfigBean.setModifyDate(new Date());
        zjhRobotSeeConfigDao.updateById(zjhRobotSeeConfigBean);
        this.cacheRemove();
        this.redisData();
    }

    @Override
    public void deleteById(Integer id) {
        zjhRobotSeeConfigDao.deleteById(id);
        this.cacheRemove();
        this.redisData();
    }

    @Override
    public ZjhRobotSeeConfigBean getRobotSeeByRound(Integer round) {
        return zjhRobotSeeConfigDao.selectList(new LambdaQueryWrapper<ZjhRobotSeeConfigBean>()
                .lt(ZjhRobotSeeConfigBean::getRoundMin, round)
                .ge(ZjhRobotSeeConfigBean::getRoundMax, round)).get(0);
    }

    @CacheEvict(cacheNames = Constant.ZJH_ROBOT_SEE)
    public void cacheRemove() {
    }

    private void redisData() {
        redisUtil.lSet(Constant.ZJH_ROBOT_SEE, this.getAllList());
    }
}

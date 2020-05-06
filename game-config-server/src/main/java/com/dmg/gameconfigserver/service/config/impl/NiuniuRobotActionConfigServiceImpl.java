package com.dmg.gameconfigserver.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.dao.config.niuniu.NiuniuRobotActionConfigDao;
import com.dmg.gameconfigserver.model.bean.config.niuniu.NiuniuRobotActionConfigBean;
import com.dmg.gameconfigserver.model.vo.config.niuniu.NiuniuRobotActionConfigVO;
import com.dmg.gameconfigserver.service.config.NiuniuRobotActionConfigService;
import com.dmg.server.common.util.DmgBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:53 2019/9/27
 */
@Service
public class NiuniuRobotActionConfigServiceImpl implements NiuniuRobotActionConfigService {

    @Autowired
    private NiuniuRobotActionConfigDao niuniuRobotActionConfigDao;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<NiuniuRobotActionConfigBean> getAllList() {
        return niuniuRobotActionConfigDao.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public NiuniuRobotActionConfigBean getNiuniuRobotActionById(Integer id) {
        return niuniuRobotActionConfigDao.selectById(id);
    }

    @Override
    public void insert(NiuniuRobotActionConfigBean niuniuRobotActionConfigBean) {
        niuniuRobotActionConfigBean.setCreateDate(new Date());
        niuniuRobotActionConfigBean.setModifyDate(new Date());
        niuniuRobotActionConfigDao.insert(niuniuRobotActionConfigBean);
        this.redisData();
    }

    @Override
    public void update(NiuniuRobotActionConfigVO vo) {
        NiuniuRobotActionConfigBean niuniuRobotActionConfigBean = niuniuRobotActionConfigDao.selectById(vo.getId());
        if (niuniuRobotActionConfigBean == null) {
            return;
        }
        DmgBeanUtils.copyProperties(vo, niuniuRobotActionConfigBean);
        niuniuRobotActionConfigBean.setModifyDate(new Date());
        niuniuRobotActionConfigDao.updateById(niuniuRobotActionConfigBean);
        this.cacheRemove();
        this.redisData();
    }

    @Override
    public void deleteById(Integer id) {
        niuniuRobotActionConfigDao.deleteById(id);
        this.cacheRemove();
        this.redisData();
    }

    @Override
    public List<NiuniuRobotActionConfigBean> getRobInfoByCard(Integer card) {
        return niuniuRobotActionConfigDao.selectList(new LambdaQueryWrapper<NiuniuRobotActionConfigBean>()
                .lt(NiuniuRobotActionConfigBean::getCardMin, card)
                .and(i -> i.isNotNull(NiuniuRobotActionConfigBean::getCardMax)
                        .ge(NiuniuRobotActionConfigBean::getCardMax, card)
                        .or().isNull(NiuniuRobotActionConfigBean::getCardMax)))
                .stream().collect(
                        Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getRobType()))),
                                ArrayList::new));
    }

    @Override
    public List<NiuniuRobotActionConfigBean> getPressureInfoByRob(Integer card, Integer robType) {
        return niuniuRobotActionConfigDao.selectList(new LambdaQueryWrapper<NiuniuRobotActionConfigBean>()
                .eq(NiuniuRobotActionConfigBean::getRobType, robType)
                .lt(NiuniuRobotActionConfigBean::getCardMin, card)
                .and(i -> i.isNotNull(NiuniuRobotActionConfigBean::getCardMax)
                        .ge(NiuniuRobotActionConfigBean::getCardMax, card)
                        .or().isNull(NiuniuRobotActionConfigBean::getCardMax)));
    }

    @CacheEvict(cacheNames = Constant.NN_ROBOT_ACTION)
    public void cacheRemove() {
    }

    private void redisData() {
        redisUtil.lSet(Constant.NN_ROBOT_ACTION, this.getAllList());
    }
}

package com.dmg.gameconfigserver.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.dao.config.zjh.ZjhRobotActionConfigDao;
import com.dmg.gameconfigserver.model.bean.config.zjh.ZjhRobotActionConfigBean;
import com.dmg.gameconfigserver.model.vo.config.zjh.ZjhRobotActionConfigVO;
import com.dmg.gameconfigserver.service.config.ZjhRobotActionConfigService;
import com.dmg.server.common.util.DmgBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 17:02 2019/9/27
 */
@Service
public class ZjhRobotActionConfigServiceImpl implements ZjhRobotActionConfigService {

    @Autowired
    private ZjhRobotActionConfigDao zjhRobotActionConfigDao;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<ZjhRobotActionConfigBean> getAllList() {
        return zjhRobotActionConfigDao.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public ZjhRobotActionConfigBean getZjhRobotActionById(Integer id) {
        return zjhRobotActionConfigDao.selectById(id);
    }

    @Override
    public void insert(ZjhRobotActionConfigBean zjhRobotActionConfigBean) {
        zjhRobotActionConfigBean.setCreateDate(new Date());
        zjhRobotActionConfigBean.setModifyDate(new Date());
        zjhRobotActionConfigDao.insert(zjhRobotActionConfigBean);
        this.redisData();
    }

    @Override
    public void update(ZjhRobotActionConfigVO vo) {
        ZjhRobotActionConfigBean zjhRobotActionConfigBean = zjhRobotActionConfigDao.selectById(vo.getId());
        if (zjhRobotActionConfigBean == null) {
            return;
        }
        DmgBeanUtils.copyProperties(vo, zjhRobotActionConfigBean);
        zjhRobotActionConfigBean.setModifyDate(new Date());
        zjhRobotActionConfigDao.updateById(zjhRobotActionConfigBean);
        this.cacheRemove();
        this.redisData();
    }

    @Override
    public void deleteById(Integer id) {
        zjhRobotActionConfigDao.deleteById(id);
        this.cacheRemove();
        this.redisData();
    }

    @Override
    public ZjhRobotActionConfigBean getRobotActionByIsSee(Boolean isSee) {
        return zjhRobotActionConfigDao.selectList(new LambdaQueryWrapper<ZjhRobotActionConfigBean>()
                .eq(ZjhRobotActionConfigBean::getIsSee, isSee)).get(0);
    }

    @Override
    public ZjhRobotActionConfigBean getRobotActionByCard(Integer cardType, Integer card) {
        return zjhRobotActionConfigDao.selectList(new LambdaQueryWrapper<ZjhRobotActionConfigBean>()
                .eq(ZjhRobotActionConfigBean::getCardType, cardType)
                .and(i -> i.isNotNull(ZjhRobotActionConfigBean::getCardMax)
                        .lt(ZjhRobotActionConfigBean::getCardMax, card)
                        .or().isNull(ZjhRobotActionConfigBean::getCardMax))
                .and(i -> i.isNotNull(ZjhRobotActionConfigBean::getCardMin)
                        .ge(ZjhRobotActionConfigBean::getCardMin, card)
                        .or().isNull(ZjhRobotActionConfigBean::getCardMin))).get(0);
    }

    @CacheEvict(cacheNames = Constant.ZJH_ROBOT_ACTION)
    public void cacheRemove() {
    }

    private void redisData() {
        redisUtil.lSet(Constant.ZJH_ROBOT_ACTION, this.getAllList());
    }
}

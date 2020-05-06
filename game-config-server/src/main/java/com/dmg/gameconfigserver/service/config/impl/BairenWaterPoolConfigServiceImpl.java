package com.dmg.gameconfigserver.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.gameconfigserver.dao.config.bairen.BairenWaterPoolConfigDao;
import com.dmg.gameconfigserver.model.bean.config.bairen.BairenWaterPoolConfigBean;
import com.dmg.gameconfigserver.service.config.BairenWaterPoolConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("waterPoolConfigService")
public class BairenWaterPoolConfigServiceImpl implements BairenWaterPoolConfigService {

    @Autowired
    private BairenWaterPoolConfigDao bairenWaterPoolConfigDao;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void insert(BairenWaterPoolConfigBean bairenWaterPoolConfigBean) {
        bairenWaterPoolConfigDao.insert(bairenWaterPoolConfigBean);
        redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_BAIREN_GAME_CONFIG_CHANNEL,"更新游戏配置");
    }

    @Override
    public void update(BairenWaterPoolConfigBean bairenWaterPoolConfigBean) {
        bairenWaterPoolConfigDao.updateById(bairenWaterPoolConfigBean);
        redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_BAIREN_GAME_CONFIG_CHANNEL,"更新游戏配置");
    }

    @Override
    public void delete(Integer id) {
        bairenWaterPoolConfigDao.deleteById(id);
        redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_BAIREN_GAME_CONFIG_CHANNEL,"更新游戏配置");
    }

    @Override
    public void deleteByFileBaseConfigId(Integer fileBaseConfigId) {
        bairenWaterPoolConfigDao.delete(new LambdaQueryWrapper<BairenWaterPoolConfigBean>().eq(BairenWaterPoolConfigBean::getFileBaseConfigId,fileBaseConfigId));
        redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_BAIREN_GAME_CONFIG_CHANNEL,"更新游戏配置");
    }

    @Override
    public List<BairenWaterPoolConfigBean> getList(Integer fileBaseConfigId) {
        List<BairenWaterPoolConfigBean> bairenWaterPoolConfigBeanList = bairenWaterPoolConfigDao.selectList(new LambdaQueryWrapper<BairenWaterPoolConfigBean>().eq(BairenWaterPoolConfigBean::getFileBaseConfigId,fileBaseConfigId));
        return bairenWaterPoolConfigBeanList;
    }


}

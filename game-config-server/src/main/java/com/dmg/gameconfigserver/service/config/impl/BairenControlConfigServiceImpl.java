package com.dmg.gameconfigserver.service.config.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.web.BusinessException;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.dao.config.FileBaseConfigDao;
import com.dmg.gameconfigserver.dao.config.bairen.BairenControlConfigDao;
import com.dmg.gameconfigserver.dao.config.bairen.BairenFileConfigDao;
import com.dmg.gameconfigserver.dao.config.bairen.BairenWaterPoolConfigDao;
import com.dmg.gameconfigserver.model.bean.config.bairen.BairenControlConfigBean;
import com.dmg.gameconfigserver.model.dto.config.BairenControlConfigDTO;
import com.dmg.gameconfigserver.service.config.BairenControlConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service("bairenControlConfigService")
public class BairenControlConfigServiceImpl implements BairenControlConfigService {

    @Autowired
    private BairenControlConfigDao bairenControlConfigDao;
    @Autowired
    private BairenWaterPoolConfigDao bairenWaterPoolConfigDao;
    @Autowired
    private FileBaseConfigDao fileBaseConfigDao;
    @Autowired
    private BairenFileConfigDao bairenFileConfigDao;
    @Autowired
    private StringRedisTemplate redisTemplate;


    public void insert(BairenControlConfigBean bairenControlConfigBean){
        BairenControlConfigBean check = bairenControlConfigDao.selectOne(new LambdaQueryWrapper<BairenControlConfigBean>()
                .eq(BairenControlConfigBean::getFileBaseConfigId, bairenControlConfigBean.getId()));
        if (check!=null){
            throw new BusinessException(ResultEnum.HAS_EXIT.getCode()+"",ResultEnum.HAS_EXIT.getMsg());
        }
        bairenControlConfigDao.insert(bairenControlConfigBean);
        redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_BAIREN_GAME_CONFIG_CHANNEL,"更新游戏配置");
    }

    public void update(BairenControlConfigBean bairenControlConfigBean){
        bairenControlConfigDao.update(bairenControlConfigBean,new LambdaQueryWrapper<BairenControlConfigBean>()
                .eq(BairenControlConfigBean::getFileBaseConfigId,bairenControlConfigBean.getFileBaseConfigId()));
        redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_BAIREN_GAME_CONFIG_CHANNEL,"更新游戏配置");
    }

    @Override
    public void delete(Integer fileBaseConfigId) {
        bairenControlConfigDao.delete(new LambdaQueryWrapper<BairenControlConfigBean>().eq(BairenControlConfigBean::getFileBaseConfigId,fileBaseConfigId));
        redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_BAIREN_GAME_CONFIG_CHANNEL,"更新游戏配置");
    }

    public BairenControlConfigDTO getOne(Integer fileBaseConfigId){
        BairenControlConfigBean bairenControlConfigBean = bairenControlConfigDao.selectOne(new LambdaQueryWrapper<BairenControlConfigBean>()
                .eq(BairenControlConfigBean::getFileBaseConfigId,fileBaseConfigId));
        BairenControlConfigDTO bairenControlConfigDTO = new BairenControlConfigDTO();
        BeanUtils.copyProperties(bairenControlConfigBean,bairenControlConfigDTO);
        bairenControlConfigDTO.setCardTypeMultiple(JSON.parseObject(bairenControlConfigBean.getCardTypeMultiple()));
        return bairenControlConfigDTO;
    }

}

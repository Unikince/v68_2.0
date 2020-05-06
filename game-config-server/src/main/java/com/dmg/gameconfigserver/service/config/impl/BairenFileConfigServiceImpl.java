package com.dmg.gameconfigserver.service.config.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.web.BusinessException;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.dao.config.FileBaseConfigDao;
import com.dmg.gameconfigserver.dao.config.bairen.BairenFileConfigDao;
import com.dmg.gameconfigserver.model.bean.config.FileBaseConfigBean;
import com.dmg.gameconfigserver.model.bean.config.bairen.BairenFileConfigBean;
import com.dmg.gameconfigserver.model.dto.config.BairenFileConfigDTO;
import com.dmg.gameconfigserver.service.config.BairenFileConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("bairenFileConfigService")
public class BairenFileConfigServiceImpl implements BairenFileConfigService {

    @Autowired
    private FileBaseConfigDao fileBaseConfigDao;
    @Autowired
    private BairenFileConfigDao bairenFileConfigDao;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public BairenFileConfigDTO getOne(int fileBaseConfigId) {
        BairenFileConfigDTO bairenFileConfigDTO = new BairenFileConfigDTO();
        FileBaseConfigBean fileBaseConfigBean = fileBaseConfigDao.selectOne(new LambdaQueryWrapper<FileBaseConfigBean>()
                .eq(FileBaseConfigBean::getId,fileBaseConfigId));
        if (fileBaseConfigBean == null){
            return bairenFileConfigDTO;
        }
        BeanUtils.copyProperties(fileBaseConfigBean,bairenFileConfigDTO);
        BairenFileConfigBean bairenFileConfigBean = bairenFileConfigDao.selectOne(new LambdaQueryWrapper<BairenFileConfigBean>()
                .eq(BairenFileConfigBean::getFileBaseConfigId,fileBaseConfigId));
        BeanUtils.copyProperties(bairenFileConfigBean,bairenFileConfigDTO);
        return bairenFileConfigDTO;
    }

    @Override
    public List<BairenFileConfigDTO> getFileConfigList(int gameId) {
        List<BairenFileConfigDTO> bairenFileConfigDTOS = new ArrayList<>();
        List<FileBaseConfigBean> fileBaseConfigBeans = fileBaseConfigDao.selectList(new LambdaQueryWrapper<FileBaseConfigBean>()
                .eq(FileBaseConfigBean::getGameId,gameId));
        if (CollectionUtil.isEmpty(fileBaseConfigBeans)){
            return bairenFileConfigDTOS;
        }
        for (FileBaseConfigBean fileBaseConfigBean : fileBaseConfigBeans){
            BairenFileConfigDTO bairenFileConfigDTO = new BairenFileConfigDTO();
            BairenFileConfigBean bairenFileConfigBean = bairenFileConfigDao.selectOne(new LambdaQueryWrapper<BairenFileConfigBean>()
                    .eq(BairenFileConfigBean::getFileBaseConfigId,fileBaseConfigBean.getId()));
            BeanUtils.copyProperties(fileBaseConfigBean,bairenFileConfigDTO);
            BeanUtils.copyProperties(bairenFileConfigBean,bairenFileConfigDTO);
            bairenFileConfigDTOS.add(bairenFileConfigDTO);
        }
        return bairenFileConfigDTOS;
    }

    @Override
    public void insert(FileBaseConfigBean fileBaseConfigBean, BairenFileConfigBean bairenFileConfigBean) {
        FileBaseConfigBean check = fileBaseConfigDao.selectOne(new LambdaQueryWrapper<FileBaseConfigBean>()
                .eq(FileBaseConfigBean::getGameId,fileBaseConfigBean.getGameId())
                .eq(FileBaseConfigBean::getFileId,fileBaseConfigBean.getFileId()));
        if (check != null){
            throw new BusinessException(String.valueOf(ResultEnum.HAS_EXIT.getCode()),ResultEnum.HAS_EXIT.getMsg());
        }
        fileBaseConfigDao.insert(fileBaseConfigBean);
        bairenFileConfigBean.setFileBaseConfigId(fileBaseConfigBean.getId());
        bairenFileConfigDao.insert(bairenFileConfigBean);
        redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_BAIREN_GAME_CONFIG_CHANNEL,"更新游戏配置");
    }

    @Override
    public void update(FileBaseConfigBean fileBaseConfigBean, BairenFileConfigBean bairenFileConfigBean) {
        fileBaseConfigBean.setId(bairenFileConfigBean.getFileBaseConfigId());
        fileBaseConfigDao.updateById(fileBaseConfigBean);
        bairenFileConfigDao.update(bairenFileConfigBean,new LambdaQueryWrapper<BairenFileConfigBean>()
                .eq(BairenFileConfigBean::getFileBaseConfigId,bairenFileConfigBean.getFileBaseConfigId()));
        redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_BAIREN_GAME_CONFIG_CHANNEL,"更新游戏配置");

    }

    @Override
    public void delete(int fileBaseConfigId) {
        fileBaseConfigDao.deleteById(fileBaseConfigId);
        bairenFileConfigDao.delete(new LambdaQueryWrapper<BairenFileConfigBean>()
                .eq(BairenFileConfigBean::getFileBaseConfigId,fileBaseConfigId));
        redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_BAIREN_GAME_CONFIG_CHANNEL,"更新游戏配置");
    }
}
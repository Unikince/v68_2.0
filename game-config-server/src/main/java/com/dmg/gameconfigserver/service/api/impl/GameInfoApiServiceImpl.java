package com.dmg.gameconfigserver.service.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.gameconfigserver.dao.GameInfoDao;
import com.dmg.gameconfigserver.model.bean.GameInfoBean;
import com.dmg.gameconfigserver.service.api.GameInfoApiService;
import com.dmg.gameconfigserverapi.dto.GameInfoDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/10/17 11:27
 * @Version V1.0
 **/
@Service("gameInfoApiService")
public class GameInfoApiServiceImpl implements GameInfoApiService {

    @Autowired
    private GameInfoDao gameInfoDao;

    @Override
    public List<GameInfoDTO> getGameInfoList() {
        List<GameInfoDTO> gameInfoDTOS = new LinkedList<>();
        List<GameInfoBean> gameInfoBeans = gameInfoDao.selectList(new LambdaQueryWrapper<>());
        for (GameInfoBean gameInfoBean : gameInfoBeans){
            GameInfoDTO gameInfoDTO = new GameInfoDTO();
            BeanUtils.copyProperties(gameInfoBean,gameInfoDTO);
            gameInfoDTOS.add(gameInfoDTO);
        }
        return gameInfoDTOS;
    }
}
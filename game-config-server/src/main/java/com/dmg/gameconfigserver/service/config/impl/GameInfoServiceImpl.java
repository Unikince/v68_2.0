package com.dmg.gameconfigserver.service.config.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.web.BusinessException;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.dao.GameInfoDao;
import com.dmg.gameconfigserver.dao.config.FileBaseConfigDao;
import com.dmg.gameconfigserver.model.bean.GameInfoBean;
import com.dmg.gameconfigserver.model.bean.config.FileBaseConfigBean;
import com.dmg.gameconfigserver.model.dto.CommonRespDTO;
import com.dmg.gameconfigserver.model.dto.GameInfoDTO;
import com.dmg.gameconfigserver.model.dto.config.GameConfigDetailDTO;
import com.dmg.gameconfigserver.model.vo.GameFileVO;
import com.dmg.gameconfigserver.model.vo.GameInfoVO;
import com.dmg.gameconfigserver.service.config.GameInfoService;
import com.dmg.gameconfigserver.service.gatewayroutes.GatewayRoutesService;
import com.dmg.server.common.util.DmgBeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


@Service("gameInfoService")
public class GameInfoServiceImpl implements GameInfoService {

    @Autowired
    private GameInfoDao gameInfoDao;

    @Autowired
    private FileBaseConfigDao fileBaseConfigDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private GatewayRoutesService gatewayRoutesService;



    @Override
    public List<GameInfoDTO> getGameInfoList(Integer gameType) {
        List<GameInfoDTO> gameInfoDTOS = new LinkedList<>();
        LambdaQueryWrapper<GameInfoBean> query = new LambdaQueryWrapper<>();
        if (gameType != null) {
            query.eq(GameInfoBean::getGameType, gameType);
        }
        List<GameInfoBean> gameInfoBeans = gameInfoDao.selectList(query);
        for (GameInfoBean gameInfoBean : gameInfoBeans) {
            GameInfoDTO gameInfoDTO = new GameInfoDTO(gameInfoBean.getGameId(),
                    gameInfoBean.getGameName(), gameInfoBean.getGameType());
            gameInfoDTOS.add(gameInfoDTO);
        }
        return gameInfoDTOS;
    }

    @Override
    public List<GameConfigDetailDTO> getGameConfigDetailList(int gameId) {
        List<GameConfigDetailDTO> gameConfigDetailDTOS = new ArrayList<>();
        List<FileBaseConfigBean> fileBaseConfigBeans = fileBaseConfigDao.selectList(new LambdaQueryWrapper<FileBaseConfigBean>().eq(FileBaseConfigBean::getGameId, gameId));
        if (CollectionUtil.isEmpty(fileBaseConfigBeans)) {
            return gameConfigDetailDTOS;
        }
        for (FileBaseConfigBean fileBaseConfigBean : fileBaseConfigBeans) {
            GameConfigDetailDTO gameConfigDetailDTO = new GameConfigDetailDTO();
            BeanUtils.copyProperties(fileBaseConfigBean, gameConfigDetailDTO);
            gameConfigDetailDTO.setCurPlayerNumber(0);
            gameConfigDetailDTO.setCurRobotNumber(0);
            gameConfigDetailDTO.setFileBaseConfigId(fileBaseConfigBean.getId());
            String winGold = stringRedisTemplate.opsForValue().get(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + gameId + "_" + fileBaseConfigBean.getFileId());
            if (StringUtils.isEmpty(winGold)) {
                gameConfigDetailDTO.setWinGold(BigDecimal.ZERO);
            } else {
                gameConfigDetailDTO.setWinGold(new BigDecimal(winGold));
            }
            gameConfigDetailDTOS.add(gameConfigDetailDTO);
        }
        return gameConfigDetailDTOS;
    }

    @Override
    public CommonRespDTO<GameInfoBean> changeGameStatus(Long id, Integer status) {
        GameInfoBean gameInfoBean  = gameInfoDao.selectById(id);
        if (gameInfoBean == null){
            throw new BusinessException(ResultEnum.PARAM_ERROR.getCodeStr(),ResultEnum.PARAM_ERROR.getMsg());
        }
        GameInfoBean target = new GameInfoBean();
        DmgBeanUtils.copyProperties(gameInfoBean, target);
        if (status > 0 && gameInfoBean.getGameStatus() == 0){
            // 开启路由
            gatewayRoutesService.openRoutesByRouteIdPrefix(gameInfoBean.getRouteIdPrefix());
            gatewayRoutesService.updateVersion();
        }else if(status == 0){
            // 关闭路由
            gatewayRoutesService.closeRoutesByRouteIdPrefix(gameInfoBean.getRouteIdPrefix());
            gatewayRoutesService.updateVersion();
        }
        gameInfoBean.setGameStatus(status);
        gameInfoDao.updateById(gameInfoBean);
        return CommonRespDTO.getSuccess(target);
    }

    @Override
    public List<GameInfoVO> getGameStatus() {
        List<GameInfoBean> gameInfoBeans = gameInfoDao.selectList(new LambdaQueryWrapper<>());
        return DmgBeanUtils.copyList(gameInfoBeans, GameInfoVO.class);
    }

    @Override
    public GameInfoVO getGameInfo(int gameId) {
        List<GameInfoBean> gameInfoBeans = gameInfoDao.selectList(new LambdaQueryWrapper<GameInfoBean>().eq(GameInfoBean::getGameId, gameId));
        if (gameInfoBeans != null && gameInfoBeans.size() > 0) {
            GameInfoVO gameInfoVO = new GameInfoVO();
            DmgBeanUtils.copyProperties(gameInfoBeans.get(0), gameInfoVO);
            return gameInfoVO;
        }
        return null;
    }

    @Override
    public void update(GameInfoVO gameInfoVO) {
        GameInfoBean gameInfoBean = new GameInfoBean();
        DmgBeanUtils.copyProperties(gameInfoVO, gameInfoBean);
        gameInfoDao.updateById(gameInfoBean);
    }

    @Override
    public List<GameInfoVO> getGameOpen() {
        List<GameInfoBean> gameInfoBeans = gameInfoDao.selectList(new LambdaQueryWrapper<GameInfoBean>().eq(GameInfoBean::getOpenStatus, 1));
        if (gameInfoBeans == null || gameInfoBeans.size() < 1) {
            return null;
        }
        return DmgBeanUtils.copyList(gameInfoBeans, GameInfoVO.class);
    }

    @Override
    public List<GameFileVO> getGameFile() {
        return gameInfoDao.getGameFile();
    }
}

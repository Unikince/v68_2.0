package com.dmg.gameconfigserver.service.home.impl;

import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.gameconfigserver.dao.home.GamePumpRecordDao;
import com.dmg.gameconfigserver.model.bean.config.FileBaseConfigBean;
import com.dmg.gameconfigserver.model.dto.GameInfoDTO;
import com.dmg.gameconfigserver.model.dto.home.GamePumpRecordDTO;
import com.dmg.gameconfigserver.model.vo.home.GameFilePumpDetailVO;
import com.dmg.gameconfigserver.model.vo.home.GamePumpDetailVO;
import com.dmg.gameconfigserver.model.vo.home.GamePumpRecordVO;
import com.dmg.gameconfigserver.service.config.GameFileConfigService;
import com.dmg.gameconfigserver.service.config.GameInfoService;
import com.dmg.gameconfigserver.service.home.GamePumpRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:39 2020/3/13
 */
@Slf4j
@Service
public class GamePumpRecordServiceImpl implements GamePumpRecordService {

    @Autowired
    private GamePumpRecordDao gamePumpRecordDao;

    @Autowired
    private GameInfoService gameInfoService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GameFileConfigService gameFileConfigService;

    @Override
    public List<GamePumpRecordVO> getGamePumpRecordList(GamePumpRecordDTO gamePumpRecordDTO) {
        return gamePumpRecordDao.getGamePumpRecordList(gamePumpRecordDTO.getGameId(), gamePumpRecordDTO.getFileId());
    }

    @Override
    public GamePumpDetailVO getGamePumpDetail() {
        List<GameInfoDTO> gameInfoDTOList = gameInfoService.getGameInfoList(null);
        GamePumpDetailVO gamePumpDetailVO = new GamePumpDetailVO();
        if (gameInfoDTOList == null || gameInfoDTOList.size() < 1) {
            return gamePumpDetailVO;
        }
        List<String> keys = new ArrayList<>(redisUtil.keys(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + "*"));
        List<String> data = redisUtil.multiGet(keys);
        gameInfoDTOList.forEach(gameInfoDTO -> {
            BigDecimal pump = BigDecimal.ZERO;
            for (int i = 0; i < keys.size() && i < data.size(); i++) {
                String[] key = keys.get(i).substring((RedisRegionConfig.FILE_WIN_GOLD_KEY + ":").length()).split("_");
                if (key == null || key.length < 2) {
                    break;
                }
                if (key[0].equals(gameInfoDTO.getGameId().toString())) {
                    pump = pump.add(new BigDecimal(data.get(i))).setScale(2, BigDecimal.ROUND_DOWN);
                }
            }
            gamePumpDetailVO.getDetails().add(
                    GamePumpDetailVO.Detail.builder()
                            .gameId(gameInfoDTO.getGameId())
                            .gameName(gameInfoDTO.getGameName())
                            .pump(pump).build());
            gamePumpDetailVO.setCountPump(gamePumpDetailVO.getCountPump().add(pump));
        });
        return gamePumpDetailVO;
    }

    @Override
    public GameFilePumpDetailVO getGameFilePumpDetail(Integer gameId) {
        List<FileBaseConfigBean> fileBaseConfigBeanList = gameFileConfigService.getFileDetailByGameId(gameId);
        GameFilePumpDetailVO gameFilePumpDetailVO = new GameFilePumpDetailVO();
        if (fileBaseConfigBeanList == null || fileBaseConfigBeanList.size() < 1) {
            return gameFilePumpDetailVO;
        }
        List<String> keys = new ArrayList<>(redisUtil.keys(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + gameId + "_*"));
        List<String> data = redisUtil.multiGet(keys);

        fileBaseConfigBeanList.forEach(fileBaseConfigBean -> {
            BigDecimal pump = BigDecimal.ZERO;
            for (int i = 0; i < keys.size() && i < data.size(); i++) {
                String[] key = keys.get(i).substring((RedisRegionConfig.FILE_WIN_GOLD_KEY + ":").length()).split("_");
                if (key == null || key.length < 2) {
                    break;
                }
                if (key[1].equals(fileBaseConfigBean.getFileId().toString())) {
                    pump = new BigDecimal(data.get(i)).setScale(2, BigDecimal.ROUND_DOWN);
                    break;
                }
            }
            gameFilePumpDetailVO.getDetails().add(
                    GameFilePumpDetailVO.Detail.builder()
                            .fileId(fileBaseConfigBean.getFileId())
                            .fileName(fileBaseConfigBean.getFileName())
                            .pump(pump).build());
            gameFilePumpDetailVO.setCountPump(gameFilePumpDetailVO.getCountPump().add(pump));
        });
        return gameFilePumpDetailVO;
    }
}

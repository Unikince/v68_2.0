package com.dmg.gameconfigserver.service.home.impl;

import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.gameconfigserver.dao.home.GameOnlineRecordDao;
import com.dmg.gameconfigserver.model.bean.config.FileBaseConfigBean;
import com.dmg.gameconfigserver.model.dto.GameInfoDTO;
import com.dmg.gameconfigserver.model.dto.home.GameOnlineRecordDTO;
import com.dmg.gameconfigserver.model.vo.home.GameFileOnlineDetailVO;
import com.dmg.gameconfigserver.model.vo.home.GameOnlineDetailVO;
import com.dmg.gameconfigserver.model.vo.home.GameOnlineRecordVO;
import com.dmg.gameconfigserver.service.config.GameFileConfigService;
import com.dmg.gameconfigserver.service.config.GameInfoService;
import com.dmg.gameconfigserver.service.home.GameOnlineRecordService;
import com.dmg.server.common.enums.FilePlaceEnum;
import com.dmg.server.common.enums.PlaceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:27 2020/3/16
 */
@Service
public class GameOnlineRecordServiceImpl implements GameOnlineRecordService {

    @Autowired
    private GameOnlineRecordDao gameOnlineRecordDao;

    @Autowired
    private GameInfoService gameInfoService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GameFileConfigService gameFileConfigService;

    @Override
    public List<GameOnlineRecordVO> getGameOnlineRecordList(GameOnlineRecordDTO gameOnlineRecordDTO) {
        return gameOnlineRecordDao.getGameOnlineRecordList(gameOnlineRecordDTO.getGameId(), gameOnlineRecordDTO.getFileId());
    }

    @Override
    public GameOnlineDetailVO getGameOnlineDetail() {
        List<GameInfoDTO> gameInfoDTOList = gameInfoService.getGameInfoList(null);
        GameOnlineDetailVO gameOnlineDetailVO = new GameOnlineDetailVO();
        if (gameInfoDTOList == null || gameInfoDTOList.size() < 1) {
            return gameOnlineDetailVO;
        }
        List<String> keys = new ArrayList<>(redisUtil.keys(RedisRegionConfig.ONLINE_NUM_KEY + ":" + "*"));
        List<String> data = redisUtil.multiGet(keys);
        Integer countOnlineNum = 0;
        for (GameInfoDTO gameInfoDTO : gameInfoDTOList) {
            Integer onlineNum = 0;
            for (int i = 0; i < keys.size() && i < data.size(); i++) {
                String[] key = keys.get(i).substring((RedisRegionConfig.ONLINE_NUM_KEY + ":").length()).split("_");
                if (key == null || key.length < 1) {
                    break;
                }
                if (key[0].equals(gameInfoDTO.getGameId().toString())) {
                    onlineNum += Integer.parseInt(data.get(i));
                }
            }
            countOnlineNum += onlineNum;
            gameOnlineDetailVO.getDetails().add(
                    GameOnlineDetailVO.Detail.builder()
                            .gameId(gameInfoDTO.getGameId())
                            .gameName(gameInfoDTO.getGameName())
                            .onlineNum(onlineNum).build());
        }
        //大厅位置
        Integer lobbyOnlineNum = 0;
        for (int i = 0; i < keys.size() && i < data.size(); i++) {
            String[] key = keys.get(i).substring((RedisRegionConfig.ONLINE_NUM_KEY + ":").length()).split("_");
            if (key == null || key.length < 1) {
                break;
            }
            if (key[0].equals(PlaceEnum.CODE_LOBBY.getCode().toString())) {
                lobbyOnlineNum = Integer.parseInt(data.get(i));
            }
        }
        gameOnlineDetailVO.getDetails().add(
                GameOnlineDetailVO.Detail.builder()
                        .gameId(PlaceEnum.CODE_LOBBY.getCode())
                        .gameName(PlaceEnum.CODE_LOBBY.getMsg())
                        .onlineNum(lobbyOnlineNum - countOnlineNum).build());
        gameOnlineDetailVO.setCountOnlineNum(lobbyOnlineNum);
        return gameOnlineDetailVO;
    }

    @Override
    public GameFileOnlineDetailVO getGameFileOnlineDetail(Integer gameId) {
        List<FileBaseConfigBean> fileBaseConfigBeanList = gameFileConfigService.getFileDetailByGameId(gameId);
        GameFileOnlineDetailVO gameFileOnlineDetailVO = new GameFileOnlineDetailVO();
        if (fileBaseConfigBeanList == null || fileBaseConfigBeanList.size() < 1) {
            return gameFileOnlineDetailVO;
        }
        List<String> keys = new ArrayList<>(redisUtil.keys(RedisRegionConfig.ONLINE_NUM_KEY + ":" + gameId + "_*"));
        List<String> data = redisUtil.multiGet(keys);

        fileBaseConfigBeanList.forEach(fileBaseConfigBean -> {
            Integer onlineNum = 0;
            for (int i = 0; i < keys.size() && i < data.size(); i++) {
                String[] key = keys.get(i).substring((RedisRegionConfig.ONLINE_NUM_KEY + ":").length()).split("_");
                if (key == null || key.length < 2) {
                    break;
                }
                if (key[0].equals(gameId.toString()) && key[1].equals(fileBaseConfigBean.getFileId().toString())) {
                    onlineNum = Integer.parseInt(data.get(i));
                    break;
                }
            }
            gameFileOnlineDetailVO.getDetails().add(
                    GameFileOnlineDetailVO.Detail.builder()
                            .fileId(fileBaseConfigBean.getFileId())
                            .fileName(fileBaseConfigBean.getFileName())
                            .onlineNum(onlineNum).build());
            gameFileOnlineDetailVO.setCountOnlineNum(gameFileOnlineDetailVO.getCountOnlineNum() + onlineNum);
        });
        return gameFileOnlineDetailVO;
    }
}

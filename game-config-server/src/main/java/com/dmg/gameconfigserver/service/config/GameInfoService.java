package com.dmg.gameconfigserver.service.config;


import com.dmg.gameconfigserver.model.bean.GameInfoBean;
import com.dmg.gameconfigserver.model.dto.CommonRespDTO;
import com.dmg.gameconfigserver.model.dto.config.GameConfigDetailDTO;
import com.dmg.gameconfigserver.model.dto.GameInfoDTO;
import com.dmg.gameconfigserver.model.vo.GameFileVO;
import com.dmg.gameconfigserver.model.vo.GameInfoVO;

import java.util.List;

/**
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
public interface GameInfoService {

    /**
     * @param
     * @return java.util.List<com.dmg.gameconfigserver.model.dto.GameInfoDTO>
     * @description: 获取游戏列表
     * @author mice
     * @date 2019/10/8
     */
    List<GameInfoDTO> getGameInfoList(Integer gameType);


    /**
     * @param gameId
     * @return java.util.List<com.dmg.gameconfigserver.model.dto.config.GameConfigDetailDTO>
     * @description: 获取游戏配置详情列表
     * @author mice
     * @date 2019/10/8
     */
    List<GameConfigDetailDTO> getGameConfigDetailList(int gameId);

    /**
     * @param id
     * @param status
     * @return int
     * @description: 修改游戏状态
     * @author mice
     * @date 2020/1/7
     */
    CommonRespDTO<GameInfoBean> changeGameStatus(Long id, Integer status);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:09 2019/11/21
     **/
    List<GameInfoVO> getGameStatus();

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 9:44 2019/11/21
     **/
    GameInfoVO getGameInfo(int gameId);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 9:59 2019/11/21
     **/
    void update(GameInfoVO gameInfoVO);

    /**
     * @Author liubo
     * @Description //TODO 查詢所有開放游戲
     * @Date 16:09 2019/11/21
     **/
    List<GameInfoVO> getGameOpen();

    /**
     * @Author liubo
     * @Description 获取游戏场次信息//TODO
     * @Date 17:41 2020/3/13
     **/
    List<GameFileVO> getGameFile();

}


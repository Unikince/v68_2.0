package com.dmg.gameconfigserver.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.gameconfigserver.dao.config.notice.NoticeDao;
import com.dmg.gameconfigserver.model.bean.config.notice.NoticeBean;
import com.dmg.gameconfigserver.service.config.GameInfoService;
import com.dmg.gameconfigserver.service.sys.SysMarqueeConfigService;
import com.dmg.gameconfigserverapi.dto.SysMarqueeConfigDTO;
import com.dmg.server.common.enums.PlaceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/25 10:36
 * @Version V1.0
 **/
@Service
public class SysMarqueeConfigServiceImpl implements SysMarqueeConfigService {

    @Autowired
    private NoticeDao noticeDao;

    @Autowired
    private GameInfoService gameInfoService;

    @Override
    public List<SysMarqueeConfigDTO> getEnableExecuteSysMarqueeList(Integer marqueeType) {
        List<SysMarqueeConfigDTO> sysMarqueeConfigDTOList = new ArrayList<>();
        List<NoticeBean> noticeBeanList = noticeDao.selectList(new LambdaQueryWrapper<NoticeBean>()
                .eq(NoticeBean::getStatus, true)
                .eq(NoticeBean::getType, marqueeType)
                .lt(NoticeBean::getStartDate, new Date())
                .ge(NoticeBean::getEndDate, new Date()));
        if (CollectionUtils.isEmpty(noticeBeanList)) {
            return sysMarqueeConfigDTOList;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (PlaceEnum position : PlaceEnum.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", position.getCode());
            list.add(map);
        }
        gameInfoService.getGameOpen().forEach(gameInfoVO -> {
            Map<String, Object> mapGame = new HashMap<>();
            mapGame.put("id", gameInfoVO.getGameId());
            list.add(mapGame);
        });
        for (Map<String, Object> map : list) {
            SysMarqueeConfigDTO sysMarqueeConfigDTO = new SysMarqueeConfigDTO();
            List<String> notifyContent = new ArrayList<>();
            for (NoticeBean noticeBean : noticeBeanList) {
                if (!String.valueOf(map.get("id")).equals(String.valueOf(noticeBean.getPosition()))) {
                    continue;
                }
                notifyContent.add(noticeBean.getNotifyContent());
            }
            if (CollectionUtils.isEmpty(notifyContent)) {
                continue;
            }
            sysMarqueeConfigDTO.setMarqueeType(marqueeType);
            sysMarqueeConfigDTO.setNotifyContent(notifyContent);
            sysMarqueeConfigDTO.setNotifyGameServerIds(String.valueOf(map.get("id")));
            sysMarqueeConfigDTOList.add(sysMarqueeConfigDTO);
        }
        return sysMarqueeConfigDTOList;

    }
}
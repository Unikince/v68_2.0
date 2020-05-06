package com.dmg.gameconfigserver.service.user.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.dao.user.AccountChangeLogDao;
import com.dmg.gameconfigserver.model.dto.user.AccountChangeLogDTO;
import com.dmg.gameconfigserver.model.vo.GameInfoVO;
import com.dmg.gameconfigserver.model.vo.user.AccountChangeLogVO;
import com.dmg.gameconfigserver.service.config.GameInfoService;
import com.dmg.gameconfigserver.service.user.AccountChangeLogService;
import com.dmg.server.common.enums.AccountChangeTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:02 2019/12/30
 */
@Service
public class AccountChangeLogServiceImpl implements AccountChangeLogService {

    @Autowired
    private AccountChangeLogDao accountChangeLogDao;

    @Autowired
    private GameInfoService gameInfoService;

    @Override
    public IPage<AccountChangeLogVO> getAccountChangeLog(AccountChangeLogDTO accountChangeLogDTO) {
        Page pageParam = new Page<>(accountChangeLogDTO.getCurrent(), accountChangeLogDTO.getSize()); // 当前页码，每页条数
        IPage<AccountChangeLogVO> pageResult = accountChangeLogDao.getAccountLogPage(pageParam, accountChangeLogDTO.getUserId(),
                accountChangeLogDTO.getType(), accountChangeLogDTO.getStartDate(), accountChangeLogDTO.getEndDate());
        List<GameInfoVO> gameInfoVOList = gameInfoService.getGameOpen();
        pageResult.getRecords().forEach(accountChangeLogVO -> {
            if (StringUtils.isEmpty(accountChangeLogVO.getTypeName())) {
                for (GameInfoVO gameInfoVO : gameInfoVOList) {
                    if (accountChangeLogVO.getType().intValue() == gameInfoVO.getGameId().intValue()) {
                        accountChangeLogVO.setTypeName(gameInfoVO.getGameName());
                        return;
                    }
                }
                for (AccountChangeTypeEnum type : AccountChangeTypeEnum.values()) {
                    if (type.getCode().intValue() == accountChangeLogVO.getType().intValue()) {
                        accountChangeLogVO.setTypeName(type.getMsg());
                        return;
                    }
                }
            }
        });
        return pageResult;
    }
}

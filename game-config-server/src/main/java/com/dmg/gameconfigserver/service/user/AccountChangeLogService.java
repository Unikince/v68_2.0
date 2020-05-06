package com.dmg.gameconfigserver.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.dto.user.AccountChangeLogDTO;
import com.dmg.gameconfigserver.model.vo.user.AccountChangeLogVO;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:02 2019/12/30
 */
public interface AccountChangeLogService {

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 11:02 2019/12/30
     **/
    IPage<AccountChangeLogVO> getAccountChangeLog(AccountChangeLogDTO accountChangeLogDTO);
}

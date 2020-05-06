package com.dmg.gameconfigserver.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.dto.user.UserWhiteDTO;
import com.dmg.gameconfigserver.model.vo.user.UserWhiteVO;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:26 2020/3/16
 */
public interface UserWhiteService {

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 15:29 2020/3/16
     **/
    IPage<UserWhiteVO> getUserWhitePage(UserWhiteDTO userWhiteDTO);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 15:49 2020/3/16
     **/
    String insert(String[] userId, Long sysUserId);

    /**
     * @Author liubo
     * @Description //TODO 
     * @Date 15:55 2020/3/16
     **/
    void deleteById(Long id);
}

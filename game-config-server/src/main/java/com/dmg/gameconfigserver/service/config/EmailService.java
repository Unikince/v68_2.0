package com.dmg.gameconfigserver.service.config;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.bean.config.email.EmailBean;
import com.dmg.gameconfigserver.model.bean.user.UserEmailBean;
import com.dmg.gameconfigserver.model.dto.config.email.EmailDTO;
import com.dmg.gameconfigserver.model.dto.config.email.EmailPageDTO;
import com.dmg.gameconfigserver.model.vo.config.email.EmailVO;
import com.dmg.gameconfigserverapi.dto.UserEmailDTO;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:29 2020/3/19
 */
public interface EmailService {

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 11:36 2020/3/19
     **/
    IPage<EmailVO> getEmailList(EmailPageDTO emailPageDTO);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 11:51 2020/3/19
     **/
    void insert(EmailDTO emailDTO, Long sysUserId);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    Boolean update(EmailDTO emailDTO, Long sysUserId);

    /**
     * @Author liubo
     * @Description //TODO 根据id删除
     * @Date 16:37 2019/9/27
     **/
    void deleteById(Long id);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 15:28 2020/3/23
     **/
    EmailBean getEmailById(Long id);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 9:44 2020/4/2
     **/
    Boolean insertUserEmail(UserEmailDTO userEmailDTO);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 10:29 2020/4/2
     **/
    UserEmailBean getUserEmailByTransferAccountId(Long id);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 10:34 2020/4/2
     **/
    void deleteUserEmailById(Long id);
}

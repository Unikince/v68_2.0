package com.dmg.gameconfigserver.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.dao.config.email.EmailDao;
import com.dmg.gameconfigserver.dao.user.UserDao;
import com.dmg.gameconfigserver.dao.user.UserEmailDao;
import com.dmg.gameconfigserver.model.bean.config.email.EmailBean;
import com.dmg.gameconfigserver.model.bean.sys.SysUserBean;
import com.dmg.gameconfigserver.model.bean.user.UserBean;
import com.dmg.gameconfigserver.model.bean.user.UserEmailBean;
import com.dmg.gameconfigserver.model.dto.config.email.EmailDTO;
import com.dmg.gameconfigserver.model.dto.config.email.EmailPageDTO;
import com.dmg.gameconfigserver.model.vo.config.email.EmailVO;
import com.dmg.gameconfigserver.service.config.EmailService;
import com.dmg.gameconfigserver.service.sys.SysUserService;
import com.dmg.gameconfigserverapi.dto.UserEmailDTO;
import com.dmg.server.common.util.DmgBeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:29 2020/3/19
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailDao emailDao;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserEmailDao userEmailDao;

    @Autowired
    private UserDao userDao;

    @Override
    public IPage<EmailVO> getEmailList(EmailPageDTO emailPageDTO) {
        Page page = new Page(emailPageDTO.getCurrent(), emailPageDTO.getSize());
        return emailDao.geEmailList(page, emailPageDTO.getUserId(), emailPageDTO.getSysUserId());
    }

    @Override
    public void insert(EmailDTO emailDTO, Long sysUserId) {
        EmailBean email = new EmailBean();
        DmgBeanUtils.copyProperties(emailDTO, email);
        email.setCreateDate(new Date());
        email.setCreateUser(sysUserId);
        email.setModifyUser(sysUserId);
        email.setModifyDate(new Date());
        SysUserBean sysUserBean = sysUserService.getUserInfoById(sysUserId);
        if (sysUserBean != null) {
            email.setModifyUserName(sysUserBean.getNickName());
        }
        emailDao.insert(email);
        this.insertUserEmail(email);
    }

    @Override
    public Boolean update(EmailDTO emailDTO, Long sysUserId) {
        EmailBean email = emailDao.selectById(emailDTO.getId());
        if (email == null) {
            return false;
        }
        DmgBeanUtils.copyProperties(emailDTO, email);
        email.setModifyUser(sysUserId);
        email.setModifyDate(new Date());
        SysUserBean sysUserBean = sysUserService.getUserInfoById(sysUserId);
        if (sysUserBean != null) {
            email.setModifyUserName(sysUserBean.getNickName());
        }
        emailDao.updateById(email);
        this.insertUserEmail(email);
        return true;
    }

    @Override
    public void deleteById(Long id) {
        emailDao.deleteById(id);
        userEmailDao.delete(new LambdaQueryWrapper<UserEmailBean>().eq(UserEmailBean::getEmailId, id));
    }

    @Override
    public EmailBean getEmailById(Long id) {
        return emailDao.selectById(id);
    }

    @Override
    public Boolean insertUserEmail(UserEmailDTO userEmailDTO) {
        UserEmailBean userEmailBean = new UserEmailBean();
        DmgBeanUtils.copyProperties(userEmailDTO, userEmailBean);
        userEmailBean.setItemNum(String.valueOf(userEmailDTO.getItemNum()));
        return userEmailDao.insert(userEmailBean) == 1 ? true : false;
    }

    @Override
    public UserEmailBean getUserEmailByTransferAccountId(Long id) {
        return userEmailDao.selectById(id);
    }

    @Override
    public void deleteUserEmailById(Long id) {
        userEmailDao.deleteById(id);
    }

    private void insertUserEmail(EmailBean email) {
        userEmailDao.delete(new LambdaQueryWrapper<UserEmailBean>().eq(UserEmailBean::getEmailId, email.getId()));
        List<UserEmailBean> userEmailBeanList = new ArrayList<>();
        if (StringUtils.isEmpty(email.getUserIds())) {
            List<UserBean> userBeanList = userDao.selectList(new LambdaQueryWrapper<>());
            if (userBeanList == null || userBeanList.size() < 0) {
                return;
            }
            userBeanList.forEach(userBean -> {
                userEmailBeanList.add(UserEmailBean.builder()
                        .emailContent(email.getEmailContent())
                        .emailName(email.getEmailName())
                        .expireDate(email.getExpireDate())
                        .itemNum(email.getItemNum())
                        .itemType(email.getItemType())
                        .sendDate(email.getSendDate())
                        .emailId(email.getId())
                        .userId(userBean.getId()).build());
            });
        } else {
            String[] userIds = email.getUserIds().split(",");
            if (userIds == null || userIds.length < 1) {
                return;
            }
            for (String userId : userIds) {
                userEmailBeanList.add(UserEmailBean.builder()
                        .emailContent(email.getEmailContent())
                        .emailName(email.getEmailName())
                        .expireDate(email.getExpireDate())
                        .itemNum(email.getItemNum())
                        .itemType(email.getItemType())
                        .sendDate(email.getSendDate())
                        .emailId(email.getId())
                        .userId(Long.parseLong(userId)).build());
            }
        }
        userEmailDao.insertBatch(userEmailBeanList);
    }
}

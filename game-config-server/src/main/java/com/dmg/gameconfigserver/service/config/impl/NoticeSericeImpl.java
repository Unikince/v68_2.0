package com.dmg.gameconfigserver.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.dao.config.notice.NoticeDao;
import com.dmg.gameconfigserver.model.bean.config.notice.NoticeBean;
import com.dmg.gameconfigserver.model.dto.PageReqDTO;
import com.dmg.gameconfigserver.model.dto.config.notice.NoticeDTO;
import com.dmg.gameconfigserver.model.vo.config.notice.NoticePageVO;
import com.dmg.gameconfigserver.service.config.NoticeSerice;
import com.dmg.server.common.util.DmgBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 19:00 2020/1/7
 */
@Service
public class NoticeSericeImpl implements NoticeSerice {

    @Autowired
    private NoticeDao noticeDao;

    @Override
    public IPage<NoticePageVO> getNoticeList(PageReqDTO pageReqDTO) {
        Page page = new Page(pageReqDTO.getCurrent(), pageReqDTO.getSize());
        return noticeDao.getNoticeList(page);
    }

    @Override
    public Boolean insert(NoticeDTO noticeDTO, Long sysUserId) {
        NoticeBean noticeBean = new NoticeBean();
        DmgBeanUtils.copyProperties(noticeDTO, noticeBean);
        noticeBean.setCreateDate(new Date());
        noticeBean.setCreateUser(sysUserId);
        noticeBean.setModifyUser(sysUserId);
        noticeBean.setModifyDate(new Date());
        noticeDao.insert(noticeBean);
        return true;
    }

    @Override
    public Boolean update(NoticeDTO noticeDTO, Long sysUserId) {
        NoticeBean noticeBean = new NoticeBean();
        DmgBeanUtils.copyProperties(noticeDTO, noticeBean);
        noticeBean.setModifyUser(sysUserId);
        noticeBean.setModifyDate(new Date());
        noticeDao.updateById(noticeBean);
        return true;
    }

    @Override
    public void deleteById(Long id) {
        noticeDao.deleteById(id);
    }
}

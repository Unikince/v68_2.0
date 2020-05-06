package com.dmg.gameconfigserver.dao.config.notice;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.model.bean.config.notice.NoticeBean;
import com.dmg.gameconfigserver.model.vo.config.notice.NoticePageVO;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 18:58 2020/1/7
 */
public interface NoticeDao extends BaseMapper<NoticeBean> {

    IPage<NoticePageVO> getNoticeList(Page page);
}

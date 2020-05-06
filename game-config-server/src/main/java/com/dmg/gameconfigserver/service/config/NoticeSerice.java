package com.dmg.gameconfigserver.service.config;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.dto.PageReqDTO;
import com.dmg.gameconfigserver.model.dto.config.notice.NoticeDTO;
import com.dmg.gameconfigserver.model.vo.config.notice.NoticePageVO;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 19:00 2020/1/7
 */
public interface NoticeSerice {

    /**
     * @Author liubo
     * @Description //TODO 分页查询
     * @Date 19:12 2020/1/7
     **/
    IPage<NoticePageVO> getNoticeList(PageReqDTO pageReqDTO);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    Boolean insert(NoticeDTO noticeDTO, Long sysUserId);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    Boolean update(NoticeDTO noticeDTO, Long sysUserId);

    /**
     * @Author liubo
     * @Description //TODO 根据id删除
     * @Date 16:37 2019/9/27
     **/
    void deleteById(Long id);
}

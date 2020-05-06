package com.dmg.gameconfigserver.dao.config.email;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.model.bean.config.email.EmailBean;
import com.dmg.gameconfigserver.model.vo.config.email.EmailVO;
import org.apache.ibatis.annotations.Param;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:02 2020/3/19
 */
@DS("v68")
public interface EmailDao extends BaseMapper<EmailBean> {

    IPage<EmailVO> geEmailList(Page page,
                                 @Param("userId") Long userId,
                                 @Param("sysUserId") Long sysUserId);
}

package com.dmg.gameconfigserver.dao.user;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.model.bean.user.AccountChangeLogBean;
import com.dmg.gameconfigserver.model.vo.user.AccountChangeLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:00 2019/12/30
 */
@DS("v68")
public interface AccountChangeLogDao extends BaseMapper<AccountChangeLogBean> {

    IPage<AccountChangeLogVO> getAccountLogPage(Page page, @Param("userId") Long userId,
                                                @Param("type") Integer type,
                                                @Param("startDate") Date startDate,
                                                @Param("endDate") Date endDate);
}

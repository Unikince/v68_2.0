package com.dmg.gameconfigserver.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.model.bean.sys.SysActionLogBean;
import com.dmg.gameconfigserver.model.vo.sys.SysActionLogVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:51 2019/11/6
 */
public interface SysActionLogDao extends BaseMapper<SysActionLogBean> {

    IPage<SysActionLogVO> getSysActionLogPage(Page page, @Param("userName") String userName,
                                              @Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate);
}

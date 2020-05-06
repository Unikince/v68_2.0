package com.dmg.gameconfigserver.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.model.bean.sys.SysWhiteBean;
import com.dmg.gameconfigserver.model.vo.sys.SysWhiteVO;
import org.apache.ibatis.annotations.Param;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:51 2019/11/6
 */
public interface SysWhiteDao extends BaseMapper<SysWhiteBean> {

    IPage<SysWhiteVO> getSysWhitePage(Page page, @Param("ip") String ip);
}

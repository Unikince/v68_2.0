package com.dmg.gameconfigserver.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.model.bean.sys.SysUserBean;
import com.dmg.gameconfigserver.model.vo.sys.SysUserVO;
import org.apache.ibatis.annotations.Param;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:51 2019/11/6
 */
public interface SysUserDao extends BaseMapper<SysUserBean> {

    IPage<SysUserVO> getSysUserPage(Page page, @Param("nickName") String nickName);
}

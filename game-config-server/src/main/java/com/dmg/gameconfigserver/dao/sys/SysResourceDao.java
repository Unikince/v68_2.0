package com.dmg.gameconfigserver.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.sys.SysResourceBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:51 2019/11/6
 */
public interface SysResourceDao extends BaseMapper<SysResourceBean> {

    List<SysResourceBean> getInfoByUserId(@Param("userId") Long userId);

    Integer getCountByUserId(@Param("userId") Long userId,
                             @Param("url") String url);
}

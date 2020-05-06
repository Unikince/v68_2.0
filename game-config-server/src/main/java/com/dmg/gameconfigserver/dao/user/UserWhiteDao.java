package com.dmg.gameconfigserver.dao.user;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.model.bean.user.UserWhiteBean;
import com.dmg.gameconfigserver.model.vo.user.UserWhiteVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:25 2020/3/16
 */
@DS("v68")
public interface UserWhiteDao extends BaseMapper<UserWhiteBean> {

    IPage<UserWhiteVO> getUserWhitePage(Page page, @Param("userId") Long userId,
                                   @Param("userName") String userName);

    List<String> getUserWhiteDeviceCode();
}

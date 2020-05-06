package com.dmg.gameconfigserver.service.sys;

import com.dmg.gameconfigserver.model.bean.sys.SysResourceBean;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:52 2019/11/6
 */
public interface SysResourceService {

    /**
     * @Author liubo
     * @Description //TODO 查询所有资源
     * @Date 13:54 2019/11/6
     **/
    List<SysResourceBean> getAllList();

    /**
     * @Author liubo
     * @Description //TODO 权限查询
     * @Date 15:33 2019/12/24
     **/
    List<SysResourceBean> getInfoByUserId(Long userId);

    /**
     * @Author liubo
     * @Description //TODO 查询url是否放行
     * @Date 16:48 2019/12/24
     **/
    Boolean getURLIsRelease(Long userId, String url);

}

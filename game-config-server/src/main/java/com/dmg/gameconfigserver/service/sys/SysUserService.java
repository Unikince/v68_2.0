package com.dmg.gameconfigserver.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.bean.sys.SysUserBean;
import com.dmg.gameconfigserver.model.dto.sys.SysUserDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysUserLoginLogDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysUserPageDTO;
import com.dmg.gameconfigserver.model.vo.sys.SysUserVO;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:52 2019/11/6
 */
public interface SysUserService {
    /**
     * @Author liubo
     * @Description //TODO 查询所有用户信息
     * @Date 13:54 2019/11/6
     **/
    List<SysUserBean> getAllList();

    /**
     * @Author liubo
     * @Description //TODO 根据用户名及密码查询用户信息
     * @Date 13:55 2019/11/6
     **/
    SysUserVO getUser(String userName, String password);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    Boolean insert(SysUserDTO sysUserDTO, Long sysUserId);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    Boolean update(SysUserDTO sysUserDTO, String loginIp, Long sysUserId);

    /**
     * @Author liubo
     * @Description //TODO 根据id删除
     * @Date 16:37 2019/9/27
     **/
    void deleteById(Long id);

    /**
     * @Author liubo
     * @Description //TODO 分页查询
     * @Date 11:31 2019/12/24
     **/
    IPage<SysUserVO> getUserList(SysUserPageDTO sysUserPageDTO);

    /**
     * @Author liubo
     * @Description //TODO 登陆记录事件推送
     * @Date 15:04 2019/12/26
     **/
    void pushLogin(SysUserLoginLogDTO sysUserLoginLogDTO);

    /**
     * @Author liubo
     * @Description //TODO 
     * @Date 14:27 2020/3/20
     **/
    SysUserBean getUserInfoById(Long id);

}

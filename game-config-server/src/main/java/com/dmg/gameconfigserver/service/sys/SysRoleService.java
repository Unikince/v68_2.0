package com.dmg.gameconfigserver.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.bean.sys.SysRoleBean;
import com.dmg.gameconfigserver.model.dto.CommonRespDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysRoleDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysRolePageDTO;
import com.dmg.gameconfigserver.model.vo.sys.SysRoleVO;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:52 2019/11/6
 */
public interface SysRoleService {

    /**
     * @Author liubo
     * @Description //TODO 分页查询
     * @Date 11:31 2019/12/24
     **/
    IPage<SysRoleBean> getRoleList(SysRolePageDTO sysRolePageDTO);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 11:30 2019/12/30
     **/
    List<SysRoleBean> getAllList();

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    Boolean insert(SysRoleDTO sysRoleDTO, Long sysUserId);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    CommonRespDTO update(SysRoleDTO sysRoleDTO, Long sysUserId);

    /**
     * @Author liubo
     * @Description //TODO 根据id删除
     * @Date 16:37 2019/9/27
     **/
    void deleteById(Long id);
    /**
     * @Author liubo
     * @Description //TODO 
     * @Date 15:31 2019/12/30
     **/
    SysRoleVO getInfoById(Long id);


}

package com.dmg.gameconfigserver.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.dto.CommonRespDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysWhiteDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysWhitePageDTO;
import com.dmg.gameconfigserver.model.vo.sys.SysWhiteVO;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:52 2019/11/6
 */
public interface SysWhiteService {

    /**
     * @Author liubo
     * @Description //TODO 分页查询
     * @Date 11:31 2019/12/24
     **/
    IPage<SysWhiteVO> getWhiteList(SysWhitePageDTO sysWhitePageDTO);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    Boolean insert(SysWhiteDTO sysWhiteDTO, Long sysUserId);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 16:37 2019/9/27
     **/
    CommonRespDTO update(SysWhiteDTO sysWhiteDTO, String loginIp, Long sysUserId);

    /**
     * @Author liubo
     * @Description //TODO 根据id删除
     * @Date 16:37 2019/9/27
     **/
    void deleteById(Long id);
    /**
     * @Author liubo
     * @Description //TODO 查询ip是否放行
     * @Date 18:25 2019/12/24
     **/
    Boolean getIPIsRelease(String ip);

}

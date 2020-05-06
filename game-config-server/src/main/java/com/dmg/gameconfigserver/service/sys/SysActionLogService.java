package com.dmg.gameconfigserver.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.dto.sys.SysActionLogDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysActionLogPageDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysUserLoginLogDTO;
import com.dmg.gameconfigserver.model.vo.sys.SysActionLogVO;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:52 2019/11/6
 */
public interface SysActionLogService {

    /**
     * @Author liubo
     * @Description //TODO 分页查询
     * @Date 11:31 2019/12/24
     **/
    IPage<SysActionLogVO> getActionLogList(SysActionLogPageDTO sysActionLogPageDTO);

    /**
     * @Author liubo
     * @Description //TODO 操作日志推送
     * @Date 16:04 2019/12/25
     **/
    void pushActionLog(SysActionLogDTO sysActionLogDTO);

    /**
     * @Author liubo
     * @Description //TODO 操作日志推送 记录变更数据
     * @Date 11:48 2020/1/14
     **/
    <T> void pushActionLog(T source, T target, String loginIp, Long userId) throws Exception;

}

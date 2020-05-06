package com.dmg.gameconfigserver.service.sys;

import com.dmg.gameconfigserverapi.dto.SysMarqueeConfigDTO;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/25 10:36
 * @Version V1.0
 **/
public interface SysMarqueeConfigService {

    List<SysMarqueeConfigDTO> getEnableExecuteSysMarqueeList(Integer marqueeType);

}
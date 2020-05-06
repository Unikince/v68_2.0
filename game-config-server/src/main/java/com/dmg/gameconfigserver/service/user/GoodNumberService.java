package com.dmg.gameconfigserver.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.dto.user.GoodNumberPageDTO;
import com.dmg.gameconfigserver.model.vo.user.GoodNumberPageVO;

/**
 * 
 *
 * @author mice
 * @email .com
 * @date 2020-03-25 11:28:56
 */
public interface GoodNumberService {

    /**
     * @description: 获取分页列表
     * @param goodNumberPageVO
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.dmg.gameconfigserver.model.dto.user.GoodNumberPageDTO>
     * @author mice
     * @date 2020/3/26
    */
    IPage<GoodNumberPageDTO> getPage(GoodNumberPageVO goodNumberPageVO);

    /**
     * @description: 分配靓号
     * @param goodNumber
     * @param userId
     * @param operatorId
     * @return void
     * @author mice
     * @date 2020/3/26
    */
    void distribute(Long goodNumber,Long userId,Long operatorId);

    /**
     * @description: 取消靓号
     * @param userId
     * @param operatorId
     * @return void
     * @author mice
     * @date 2020/3/26
    */
    void untie(Long userId,Long operatorId);
}


package com.dmg.gameconfigserver.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.dao.sys.SysWhiteDao;
import com.dmg.gameconfigserver.model.bean.sys.SysWhiteBean;
import com.dmg.gameconfigserver.model.dto.CommonRespDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysWhiteDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysWhitePageDTO;
import com.dmg.gameconfigserver.model.vo.sys.SysWhiteVO;
import com.dmg.gameconfigserver.service.sys.SysActionLogService;
import com.dmg.gameconfigserver.service.sys.SysWhiteService;
import com.dmg.server.common.util.DmgBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 17:51 2019/12/24
 */
@Slf4j
@Service
public class SysWhiteServiceImpl implements SysWhiteService {

    @Autowired
    private SysWhiteDao sysWhiteDao;

    @Autowired
    private SysActionLogService sysActionLogService;

    @Override
    public IPage<SysWhiteVO> getWhiteList(SysWhitePageDTO sysWhitePageDTO) {
        Page page = new Page(sysWhitePageDTO.getCurrent(), sysWhitePageDTO.getSize());
        return sysWhiteDao.getSysWhitePage(page, sysWhitePageDTO.getIp());
    }

    @Override
    public Boolean insert(SysWhiteDTO sysWhiteDTO, Long sysUserId) {
        if (sysWhiteDao.selectOne(new LambdaQueryWrapper<SysWhiteBean>()
                .eq(SysWhiteBean::getIp, sysWhiteDTO.getIp())) != null) {
            return false;
        }
        SysWhiteBean sysWhiteBean = new SysWhiteBean();
        DmgBeanUtils.copyProperties(sysWhiteDTO, sysWhiteBean);
        sysWhiteBean.setCreateDate(new Date());
        sysWhiteBean.setCreateUser(sysUserId);
        sysWhiteBean.setModifyDate(new Date());
        sysWhiteBean.setSort(sysWhiteDao.selectList(new LambdaQueryWrapper<SysWhiteBean>().orderByDesc(SysWhiteBean::getSort)).get(0).getSort() + 1);
        sysWhiteDao.insert(sysWhiteBean);
        return true;
    }

    @Override
    public CommonRespDTO update(SysWhiteDTO sysWhiteDTO, String loginIp, Long sysUserId) {
        SysWhiteBean sysWhiteBean = sysWhiteDao.selectOne(new LambdaQueryWrapper<SysWhiteBean>()
                .eq(SysWhiteBean::getId, sysWhiteDTO.getId()));
        if (sysWhiteBean == null) {
            return CommonRespDTO.getError(ResultEnum.HAS_NOT_EXIT);
        }
        if (StringUtils.isNotEmpty(sysWhiteDTO.getIp()) && !sysWhiteDTO.getIp().equals(sysWhiteBean.getIp())
                && sysWhiteDao.selectOne(new LambdaQueryWrapper<SysWhiteBean>()
                .eq(SysWhiteBean::getIp, sysWhiteDTO.getIp())) != null) {
            return CommonRespDTO.getError();
        }
        SysWhiteBean target = new SysWhiteBean();
        DmgBeanUtils.copyProperties(sysWhiteBean, target);
        DmgBeanUtils.copyProperties(sysWhiteDTO, sysWhiteBean);
        sysWhiteBean.setModifyDate(new Date());
        sysWhiteBean.setModifyUser(sysUserId);
        sysWhiteDao.updateById(sysWhiteBean);
        try {
            sysActionLogService.pushActionLog(sysWhiteDTO, target, loginIp, sysUserId);
        } catch (Exception e) {
            log.error("发送操作记录失败:{}", e);
        }
        return CommonRespDTO.getSuccess();
    }

    @Override
    public void deleteById(Long id) {
        sysWhiteDao.deleteById(id);
    }

    @Override
    public Boolean getIPIsRelease(String ip) {
        return sysWhiteDao.selectCount(new LambdaQueryWrapper<SysWhiteBean>()
                .eq(SysWhiteBean::getIp, ip)
                .eq(SysWhiteBean::getStatus, true)) > 0;
    }
}

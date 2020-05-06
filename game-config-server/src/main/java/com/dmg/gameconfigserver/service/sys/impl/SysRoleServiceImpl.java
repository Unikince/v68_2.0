package com.dmg.gameconfigserver.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.dao.sys.SysRoleDao;
import com.dmg.gameconfigserver.dao.sys.SysRoleResourceDao;
import com.dmg.gameconfigserver.model.bean.sys.SysRoleBean;
import com.dmg.gameconfigserver.model.bean.sys.SysRoleResourceBean;
import com.dmg.gameconfigserver.model.dto.CommonRespDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysRoleDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysRolePageDTO;
import com.dmg.gameconfigserver.model.vo.sys.SysRoleVO;
import com.dmg.gameconfigserver.service.sys.SysRoleService;
import com.dmg.server.common.util.DmgBeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:34 2019/12/24
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private SysRoleResourceDao sysRoleResourceDao;

    @Override
    public IPage<SysRoleBean> getRoleList(SysRolePageDTO sysRolePageDTO) {
        Page page = new Page(sysRolePageDTO.getCurrent(), sysRolePageDTO.getSize());
        LambdaQueryWrapper<SysRoleBean> query = new LambdaQueryWrapper<SysRoleBean>()
                .orderByDesc(SysRoleBean::getCreateDate);
        if (StringUtils.isNotEmpty(sysRolePageDTO.getName())) {
            query.like(SysRoleBean::getName, sysRolePageDTO.getName());
        }
        return sysRoleDao.selectPage(page, query);
    }

    @Override
    public List<SysRoleBean> getAllList() {
        return sysRoleDao.selectList(new LambdaQueryWrapper<SysRoleBean>().eq(SysRoleBean::getStatus, true));
    }

    @Override
    public Boolean insert(SysRoleDTO sysRoleDTO, Long sysUserId) {
        if (sysRoleDao.selectOne(new LambdaQueryWrapper<SysRoleBean>()
                .eq(SysRoleBean::getName, sysRoleDTO.getName())) != null) {
            return false;
        }
        SysRoleBean sysRoleBean = new SysRoleBean();
        DmgBeanUtils.copyProperties(sysRoleDTO, sysRoleBean);
        sysRoleBean.setCreateDate(new Date());
        sysRoleBean.setCreateUser(sysUserId);
        sysRoleBean.setModifyDate(new Date());
        sysRoleBean.setSort(sysRoleDao.selectList(new LambdaQueryWrapper<SysRoleBean>().orderByDesc(SysRoleBean::getSort)).get(0).getSort() + 1);
        sysRoleDao.insert(sysRoleBean);
        if (sysRoleDTO.getResourceId() != null && sysRoleDTO.getResourceId().size() > 0) {
            sysRoleDTO.getResourceId().forEach(resourceId -> {
                SysRoleResourceBean sysRoleResourceBean = new SysRoleResourceBean();
                sysRoleResourceBean.setCreateDate(new Date());
                sysRoleResourceBean.setCreateUser(sysUserId);
                sysRoleResourceBean.setModifyDate(new Date());
                sysRoleResourceBean.setSort(sysRoleResourceDao.selectList(new LambdaQueryWrapper<SysRoleResourceBean>().orderByDesc(SysRoleResourceBean::getSort)).get(0).getSort() + 1);
                sysRoleResourceBean.setResourceId(resourceId);
                sysRoleResourceBean.setRoleId(sysRoleBean.getId());
                sysRoleResourceDao.insert(sysRoleResourceBean);
            });
        }
        return true;
    }

    @Override
    public synchronized CommonRespDTO update(SysRoleDTO sysRoleDTO, Long sysUserId) {
        SysRoleBean sysRoleBean = sysRoleDao.selectOne(new LambdaQueryWrapper<SysRoleBean>()
                .eq(SysRoleBean::getId, sysRoleDTO.getId()));
        if (sysRoleBean == null) {
            return CommonRespDTO.getError(ResultEnum.ROLE_NOT_EXIT);
        }
        if (StringUtils.isNotEmpty(sysRoleDTO.getName()) && !sysRoleDTO.getName().equals(sysRoleBean.getName())
                && sysRoleDao.selectOne(new LambdaQueryWrapper<SysRoleBean>()
                .eq(SysRoleBean::getName, sysRoleDTO.getName())) != null) {
            return CommonRespDTO.getError(ResultEnum.ROLE_EXIT);
        }
        DmgBeanUtils.copyProperties(sysRoleDTO, sysRoleBean);
        sysRoleBean.setModifyDate(new Date());
        sysRoleBean.setModifyUser(sysUserId);
        sysRoleDao.updateById(sysRoleBean);
        if (sysRoleDTO.getResourceId() != null && sysRoleDTO.getResourceId().size() > 0) {
            sysRoleResourceDao.delete(new LambdaQueryWrapper<SysRoleResourceBean>().eq(SysRoleResourceBean::getRoleId, sysRoleBean.getId()));
            sysRoleDTO.getResourceId().forEach(resourceId -> {
                SysRoleResourceBean sysRoleResourceBean = new SysRoleResourceBean();
                sysRoleResourceBean.setCreateDate(new Date());
                sysRoleResourceBean.setCreateUser(sysUserId);
                sysRoleResourceBean.setModifyDate(new Date());
                sysRoleResourceBean.setSort(sysRoleResourceDao.selectList(new LambdaQueryWrapper<SysRoleResourceBean>().orderByDesc(SysRoleResourceBean::getSort)).get(0).getSort() + 1);
                sysRoleResourceBean.setResourceId(resourceId);
                sysRoleResourceBean.setRoleId(sysRoleBean.getId());
                sysRoleResourceDao.insert(sysRoleResourceBean);
            });
        }
        return CommonRespDTO.getSuccess();
    }

    @Override
    public void deleteById(Long id) {
        sysRoleDao.deleteById(id);
        sysRoleResourceDao.delete(new LambdaQueryWrapper<SysRoleResourceBean>().eq(SysRoleResourceBean::getRoleId, id));
    }

    @Override
    public SysRoleVO getInfoById(Long id) {
        SysRoleBean sysRoleBean = sysRoleDao.selectById(id);
        if (sysRoleBean == null) {
            return null;
        }
        List<SysRoleResourceBean> sysRoleResourceBeanList = sysRoleResourceDao.selectList(new LambdaQueryWrapper<SysRoleResourceBean>().eq(SysRoleResourceBean::getRoleId, sysRoleBean.getId()));
        SysRoleVO sysRoleVO = new SysRoleVO();
        DmgBeanUtils.copyProperties(sysRoleBean, sysRoleVO);
        if (sysRoleResourceBeanList != null && sysRoleResourceBeanList.size() > 0) {
            List<Long> resourceId = new ArrayList<>();
            sysRoleResourceBeanList.forEach(sysRoleResourceBean -> {
                resourceId.add(sysRoleResourceBean.getResourceId());
            });
            sysRoleVO.setResourceId(resourceId);
        }
        return sysRoleVO;
    }
}

package com.dmg.gameconfigserver.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.dao.sys.SysRoleDao;
import com.dmg.gameconfigserver.dao.sys.SysUserDao;
import com.dmg.gameconfigserver.model.bean.sys.SysRoleBean;
import com.dmg.gameconfigserver.model.bean.sys.SysUserBean;
import com.dmg.gameconfigserver.model.dto.sys.SysUserDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysUserLoginLogDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysUserPageDTO;
import com.dmg.gameconfigserver.model.vo.sys.SysUserVO;
import com.dmg.gameconfigserver.service.sys.SysActionLogService;
import com.dmg.gameconfigserver.service.sys.SysUserService;
import com.dmg.gameconfigserver.service.sys.event.SysLoginLogEvent;
import com.dmg.server.common.util.DmgBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:53 2019/11/6
 */
@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SysActionLogService sysActionLogService;


    @Override
    public List<SysUserBean> getAllList() {
        return sysUserDao.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public SysUserVO getUser(String userName, String password) {
        SysUserBean sysUserBean = sysUserDao.selectOne(new LambdaQueryWrapper<SysUserBean>()
                .eq(SysUserBean::getUserName, userName)
                .eq(SysUserBean::getPassword, password));
        if (sysUserBean == null) {
            return null;
        }
        SysUserVO sysUserVO = new SysUserVO();
        DmgBeanUtils.copyProperties(sysUserBean, sysUserVO);
        SysRoleBean sysRoleBean = sysRoleDao.selectById(sysUserVO.getRoleId());
        sysUserVO.setRoleName(sysRoleBean.getName());
        sysUserVO.setRole(sysRoleBean.getRole());
        return sysUserVO;
    }

    @Override
    public Boolean insert(SysUserDTO sysUserDTO, Long sysUserId) {
        if (sysUserDao.selectOne(new LambdaQueryWrapper<SysUserBean>()
                .eq(SysUserBean::getUserName, sysUserDTO.getUserName())) != null) {
            return false;
        }
        SysUserBean sysUserBean = new SysUserBean();
        DmgBeanUtils.copyProperties(sysUserDTO, sysUserBean);
        sysUserBean.setCreateDate(new Date());
        sysUserBean.setCreateUser(sysUserId);
        sysUserBean.setModifyDate(new Date());
        sysUserBean.setSort(sysUserDao.selectList(new LambdaQueryWrapper<SysUserBean>().orderByDesc(SysUserBean::getSort)).get(0).getSort() + 1);
        sysUserDao.insert(sysUserBean);
        return true;
    }

    @Override
    public Boolean update(SysUserDTO sysUserDTO, String loginIp, Long sysUserId) {
        SysUserBean sysUserBean = sysUserDao.selectOne(new LambdaQueryWrapper<SysUserBean>()
                .eq(SysUserBean::getId, sysUserDTO.getId()));
        if (sysUserBean == null) {
            return false;
        }
        SysUserBean target = new SysUserBean();
        DmgBeanUtils.copyProperties(sysUserBean, target);
        DmgBeanUtils.copyProperties(sysUserDTO, sysUserBean);
        sysUserBean.setModifyDate(new Date());
        sysUserBean.setModifyUser(sysUserId);
        sysUserDao.updateById(sysUserBean);
        try {
            sysActionLogService.pushActionLog(sysUserDTO, target, loginIp, sysUserId);
        } catch (Exception e) {
            log.error("发送操作记录失败:{}", e);
        }
        return true;
    }

    @Override
    public void deleteById(Long id) {
        sysUserDao.deleteById(id);
    }

    @Override
    public IPage<SysUserVO> getUserList(SysUserPageDTO sysUserPageDTO) {
        Page page = new Page(sysUserPageDTO.getCurrent(), sysUserPageDTO.getSize());
        return sysUserDao.getSysUserPage(page, sysUserPageDTO.getNickName());
    }

    @Override
    public void pushLogin(SysUserLoginLogDTO sysUserLoginLogDTO) {
        applicationContext.publishEvent(new SysLoginLogEvent(sysUserLoginLogDTO));
    }

    @Override
    public SysUserBean getUserInfoById(Long id) {
        return sysUserDao.selectById(id);
    }
}

package com.dmg.gameconfigserver.service.financing.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.common.core.web.BusinessException;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.dao.finance.VipConfigDao;
import com.dmg.gameconfigserver.dao.sys.SysUserDao;
import com.dmg.gameconfigserver.model.bean.finance.VipConfigBean;
import com.dmg.gameconfigserver.model.bean.sys.SysUserBean;
import com.dmg.gameconfigserver.model.vo.finance.VipListRecv;
import com.dmg.gameconfigserver.service.financing.VipConfigService;

/**
 * VIP配置
 */
@Service
public class VipConfigServiceImpl implements VipConfigService {
    @Autowired
    private VipConfigDao dao;
    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public synchronized void saveOne(VipConfigBean bean, Long loginId) {
        if (bean == null) {
            throw new BusinessException("" + ResultEnum.PARAM_ERROR.getCode(), "参数为空");
        }
        int count = this.dao.selectCount(null);
        bean.setId(count + 1L);
        SysUserBean user = this.sysUserDao.selectById(loginId);
        bean.setModifyUser(user.getUserName());
        bean.setModifyDate(new Date());
        this.dao.insert(bean);

    }

    @Override
    public synchronized void delete(Long id) {
        if (id == null) {
            throw new BusinessException("" + ResultEnum.PARAM_ERROR.getCode(), "参数为空");
        }
        this.dao.deleteById(id);

        LambdaQueryWrapper<VipConfigBean> listWrapper = new LambdaQueryWrapper<>();
        listWrapper.orderByAsc(VipConfigBean::getId);
        List<VipConfigBean> beans = this.dao.selectList(listWrapper);

        for (int i = 0; i < beans.size(); i++) {
            VipConfigBean bean = beans.get(i);
            this.dao.sortId(bean.getId(), i + 1L);
        }
    }

    @Override
    public void updateOne(VipConfigBean bean, Long loginId) {
        if (bean == null) {
            throw new BusinessException("" + ResultEnum.PARAM_ERROR.getCode(), "参数为空");
        }
        SysUserBean user = this.sysUserDao.selectById(loginId);
        bean.setModifyUser(user.getUserName());
        bean.setModifyDate(new Date());
        this.dao.updateById(bean);
    }

    @Override
    public VipConfigBean get(Long id) {
        if (id == null) {
            throw new BusinessException("" + ResultEnum.PARAM_ERROR.getCode(), "参数为空");
        }
        return this.dao.selectById(id);
    }

    @Override
    public IPage<VipConfigBean> getList(VipListRecv reqVo) {
        IPage<VipConfigBean> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        return this.dao.selectPage(page, null);
    }

    @Override
    public int vipSum() {
        return this.dao.selectCount(null);
    }

}

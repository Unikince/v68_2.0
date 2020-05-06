package com.dmg.gameconfigserver.service.financing.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.common.core.util.OrderIdUtil;
import com.dmg.common.core.web.BusinessException;
import com.dmg.data.client.NettySend;
import com.dmg.data.common.dto.GoldPaySendDto;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.dao.finance.PersionRechargeLogDao;
import com.dmg.gameconfigserver.dao.sys.SysUserDao;
import com.dmg.gameconfigserver.model.bean.finance.PersionRechargeLogBean;
import com.dmg.gameconfigserver.model.bean.sys.SysUserBean;
import com.dmg.gameconfigserver.model.vo.finance.PersionRechargeLogAuditListRecv;
import com.dmg.gameconfigserver.model.vo.finance.PersionRechargeLogAuditOneSend;
import com.dmg.gameconfigserver.model.vo.finance.PersionRechargeLogAuditUpdateRecv;
import com.dmg.gameconfigserver.model.vo.finance.PersionRechargeLogCreateRecv;
import com.dmg.gameconfigserver.model.vo.finance.PersionRechargeLogOrderListRecv;
import com.dmg.gameconfigserver.model.vo.user.UserDetailVO;
import com.dmg.gameconfigserver.service.financing.PersionRechargeLogService;
import com.dmg.gameconfigserver.service.user.UserService;
import com.dmg.server.common.enums.AccountChangeTypeEnum;

import cn.hutool.core.date.DateUtil;

/**
 * 人工充值
 */
@Service
public class PersionRechargeLogServiceImpl implements PersionRechargeLogService {
    @Autowired
    private PersionRechargeLogDao dao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private UserService userService;
    @Autowired
    private NettySend nettySend;

    @Override
    public synchronized void create(PersionRechargeLogCreateRecv reqVo, Long loginId) {
        if (reqVo == null) {
            throw new BusinessException("" + ResultEnum.PARAM_ERROR.getCode(), "参数为空");
        }
        if (reqVo.getRechargeAmount().equals(BigDecimal.ZERO)) {
            return;
        }
        UserDetailVO u = this.userService.getUserInfo((int) (long) reqVo.getUserId());

        PersionRechargeLogBean bean = new PersionRechargeLogBean();
        bean.setOrderId(OrderIdUtil.personOrderId(reqVo.getType()));
        bean.setUserId(reqVo.getUserId());
        bean.setNickname(u.getUser().getUserName());
        bean.setType(reqVo.getType());
        bean.setRechargeAmount(reqVo.getRechargeAmount());
        bean.setGiveAmount(BigDecimal.ZERO);
        bean.setAccountAmount(reqVo.getRechargeAmount());
        bean.setStatus(1);
        SysUserBean user = this.sysUserDao.selectById(loginId);
        bean.setCreateUser(user.getUserName());
        bean.setCreateDate(new Date());
        bean.setCreateRemark(reqVo.getCreateRemark());
        this.dao.insert(bean);
    }

    @Override
    public IPage<PersionRechargeLogBean> persionRechargeOrderList(PersionRechargeLogOrderListRecv reqVo) {
        IPage<PersionRechargeLogBean> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        LambdaQueryWrapper<PersionRechargeLogBean> listWrapper = new LambdaQueryWrapper<>();
        if (reqVo.getUserId() != null && reqVo.getUserId() > 0) {
            listWrapper.eq(PersionRechargeLogBean::getUserId, reqVo.getUserId());
        }
        if (StringUtils.isNotBlank(reqVo.getOrderId())) {
            listWrapper.eq(PersionRechargeLogBean::getOrderId, reqVo.getOrderId());
        }
        if (reqVo.getType() != null && reqVo.getType() > 0) {
            listWrapper.eq(PersionRechargeLogBean::getType, reqVo.getType());
        }
        if (reqVo.getStatus() != null && reqVo.getStatus() > 0) {
            listWrapper.eq(PersionRechargeLogBean::getStatus, reqVo.getStatus());
        }
        if (StringUtils.isNotBlank(reqVo.getDealUser())) {
            listWrapper.eq(PersionRechargeLogBean::getDealUser, reqVo.getDealUser());
        }
        if (reqVo.getStartDate() != null) {
            listWrapper.ge(PersionRechargeLogBean::getCreateDate, reqVo.getStartDate());
        }
        if (reqVo.getEndDate() != null) {
            listWrapper.lt(PersionRechargeLogBean::getCreateDate, reqVo.getEndDate());
        }
        listWrapper.orderByAsc(PersionRechargeLogBean::getId);
        return this.dao.selectPage(page, listWrapper);
    }

    @Override
    public List<String> persionRechargeAuditUserList() {
        return this.dao.allDealUser();
    }

    @Override
    public IPage<PersionRechargeLogBean> persionRechargeAuditList(PersionRechargeLogAuditListRecv reqVo) {
        IPage<PersionRechargeLogBean> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        LambdaQueryWrapper<PersionRechargeLogBean> listWrapper = new LambdaQueryWrapper<>();
        if (reqVo.getUserId() != null && reqVo.getUserId() > 0) {
            listWrapper.eq(PersionRechargeLogBean::getUserId, reqVo.getUserId());
        }
        if (StringUtils.isNotBlank(reqVo.getOrderId())) {
            listWrapper.eq(PersionRechargeLogBean::getOrderId, reqVo.getOrderId());
        }
        if (reqVo.getStartDate() != null) {
            listWrapper.ge(PersionRechargeLogBean::getCreateDate, reqVo.getStartDate());
        }
        if (reqVo.getEndDate() != null) {
            listWrapper.lt(PersionRechargeLogBean::getCreateDate, reqVo.getEndDate());
        }
        listWrapper.eq(PersionRechargeLogBean::getStatus, 1);
        listWrapper.orderByAsc(PersionRechargeLogBean::getId);
        return this.dao.selectPage(page, listWrapper);
    }

    @Override
    public PersionRechargeLogAuditOneSend persionRechargeAuditOne(Long id) {
        PersionRechargeLogAuditOneSend resVo = new PersionRechargeLogAuditOneSend();
        PersionRechargeLogBean bean = this.dao.selectById(id);
        resVo.setPersionRechargeLog(bean);
        long userId = bean.getUserId();
        resVo.setUserDetail(this.userService.getUserInfo((int) userId));
        return resVo;
    }

    @Override
    public synchronized void persionRechargeAuditUpdate(PersionRechargeLogAuditUpdateRecv vo, Long loginId) {
        PersionRechargeLogBean bean = this.dao.selectById(vo.getId());
        if (bean == null) {
            return;
        }
        if (bean.getStatus() != 1) {
            return;
        }
        bean.setStatus(vo.getStatus());
        SysUserBean user = this.sysUserDao.selectById(loginId);
        bean.setDealUser(user.getUserName());
        bean.setDealDate(new Date());
        bean.setDealRemark(vo.getDealRemark());
        this.dao.updateById(bean);

        if (vo.getStatus() == 2) {// 充值成功，转账
            GoldPaySendDto dto = new GoldPaySendDto();
            dto.setUserId(bean.getUserId());
            dto.setPayGold(bean.getAccountAmount());
            dto.setType(AccountChangeTypeEnum.CODE_ARTIFICIAL_RECHARGE.getCode());
            this.nettySend.goldPayAsync(dto);
        }
    }

    /**
     * 自动拒绝
     */
    @Override
    public void autoPersionRechargeAuditDeny() {
        Date lastDay = DateUtil.offsetDay(new Date(), -1);
        this.dao.autoPersionRechargeAuditDeny(lastDay);
    }

}

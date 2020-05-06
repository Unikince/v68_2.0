package com.dmg.agentserver.business.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.agentserver.business.constant.TransferAccountErrorEnum;
import com.dmg.agentserver.business.dao.TransferAccountRecordDao;
import com.dmg.agentserver.business.model.po.TransferAccountRecordPo;
import com.dmg.agentserver.core.exception.BusinessException;
import com.dmg.agentserver.core.exception.CommErrorEnum;
import com.dmg.agentserviceapi.business.agentconfig.model.pojo.AgentConfig;
import com.dmg.agentserviceapi.business.transferaccountrecord.model.dto.TransferAccountRecordGetListRecv;
import com.dmg.agentserviceapi.business.transferaccountrecord.model.pojo.TransferAccountRecord;
import com.dmg.agentserviceapi.core.pack.PagePackageRes;
import com.dmg.agentserviceapi.core.page.PageReq;
import com.dmg.agentserviceapi.core.page.PageRes;
import com.dmg.common.core.util.OrderIdUtil;
import com.dmg.data.client.NettySend;
import com.dmg.data.common.dto.GoldPaySendDto;
import com.dmg.gameconfigserverapi.dto.UserEmailDTO;
import com.dmg.gameconfigserverapi.feign.UserEmailService;
import com.dmg.server.common.enums.AccountChangeTypeEnum;
import com.dmg.server.common.enums.ItemTypeEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

/**
 * 转账纪录
 */
@Service
public class TransferAccountRecordService {
    @Autowired
    private TransferAccountRecordDao dao;
    @Autowired
    private AgentConfigService agentConfigService;
    @Autowired
    private AgentRelationService agentRelationService;
    @Autowired
    private UserEmailService userEmailService;
    @Autowired
    private NettySend nettySend;
    /** 发送邮件过期时间 */
    private static Date emailExpireDate = DateUtil.parse("2099-12-31", DatePattern.NORM_DATE_PATTERN);

    /**
     * 创建
     */
    public void create(@RequestBody TransferAccountRecord obj) {
        AgentConfig agentConfig = this.agentConfigService.get();

        // begin:转账开启状态
        if (!agentConfig.isTransferAccountsStautus()) {
            throw BusinessException.create(TransferAccountErrorEnum.FORBID, agentConfig.getForbidRemake());
        }
        // end:转账开启状态

        // begin:转账开启时间
        Date nowTime = DateUtil.parse(DateUtil.format(new Date(), DatePattern.NORM_TIME_PATTERN), DatePattern.NORM_TIME_PATTERN);
        Date payBeginTime = DateUtil.parse(agentConfig.getPayBeginTime(), DatePattern.NORM_TIME_PATTERN);
        Date payEndTime = DateUtil.parse(agentConfig.getPayEndTime(), DatePattern.NORM_TIME_PATTERN);
        if (nowTime.compareTo(payBeginTime) < 0 || nowTime.compareTo(payEndTime) > 0) {
            Map<String, Object> details = new HashMap<>();
            details.put("payBeginTime", agentConfig.getPayBeginTime());
            details.put("payEndTime", agentConfig.getPayEndTime());
            details.put("current", DateUtil.now());
            throw BusinessException.create(TransferAccountErrorEnum.FORBID_TIME, JSON.toJSONString(details));
        }
        // end:转账开启时间

        // begin:验证玩家是否存在-转账玩家id
        long sourceId = obj.getSourceId();
        String sourceName = this.dao.getUserNickByUserId(sourceId);
        if (StringUtils.isBlank(sourceName)) {
            throw BusinessException.create(TransferAccountErrorEnum.PLAYER_NOT_EMPTY, "" + sourceId);
        }
        // end:验证玩家是否存在-转账玩家id

        // begin:验证玩家是否存在-接收玩家id
        long targetId = obj.getTargetId();
        String targetName = this.dao.getUserNickByUserId(targetId);
        if (StringUtils.isBlank(targetName)) {
            throw BusinessException.create(TransferAccountErrorEnum.PLAYER_NOT_EMPTY, "" + targetId);
        }
        // end:验证玩家是否存在-接收玩家id

        // begin:检查是否是上下级关系
        boolean isChild = this.agentRelationService.isChild(sourceId, targetId);
        if (isChild == false) {
            throw BusinessException.create(TransferAccountErrorEnum.NOT_CHILD, "" + targetId);
        }
        // end:检查是否是上下级关系

        // begin:总充值检查
        BigDecimal totalAgentRecharge = this.dao.getTotalAgentRecharge(sourceId);
        if (totalAgentRecharge == null) {
            totalAgentRecharge = BigDecimal.ZERO;
        }
        if (totalAgentRecharge.compareTo(agentConfig.getMinRecharge()) < 0) {
            Map<String, Object> details = new HashMap<>();
            details.put("allow", agentConfig.getMinRecharge());
            details.put("current", totalAgentRecharge);
            throw BusinessException.create(TransferAccountErrorEnum.TOTAL_RECHARGE_NOT_ENOUGH, JSON.toJSONString(details));
        }
        // end:总充值检查

        // begin:总流水检查
        BigDecimal totalAgentWater = this.dao.getTotalAgentWater(sourceId);
        if (totalAgentWater == null) {
            totalAgentWater = BigDecimal.ZERO;
        }
        if (totalAgentWater.compareTo(agentConfig.getMinWater()) < 0) {
            Map<String, Object> details = new HashMap<>();
            details.put("allow", agentConfig.getMinWater());
            details.put("current", totalAgentWater);
            throw BusinessException.create(TransferAccountErrorEnum.TOTAL_WATER_NOT_ENOUGH, JSON.toJSONString(details));
        }
        // end:总流水检查

        // begin:转账单笔金额检查
        if (obj.getSendAmount().compareTo(agentConfig.getMinOncePay()) < 0
                || obj.getSendAmount().compareTo(agentConfig.getMaxOncePay()) > 0) {
            Map<String, Object> details = new HashMap<>();
            details.put("min", agentConfig.getMinOncePay());
            details.put("max", agentConfig.getMaxOncePay());
            details.put("current", obj.getSendAmount());
            throw BusinessException.create(TransferAccountErrorEnum.ONCE_AMOUNT_ENOUGH, JSON.toJSONString(details));
        }
        // end:转账单笔金额检查

        // begin:日转账次数上限
        int payTimesOfDay = this.dao.getCountOfTimesToday(sourceId);
        if (payTimesOfDay > agentConfig.getPayTimesOfDay()) {
            throw BusinessException.create(TransferAccountErrorEnum.TIMES_TODAY_OVER, "" + agentConfig.getPayTimesOfDay());
        }
        // end:日转账次数上限

        // begin:日转账金额上限
        BigDecimal payMaxOfDay = this.dao.getAmoutOfPayToday(sourceId);
        if (payMaxOfDay == null) {
            payMaxOfDay = BigDecimal.ZERO;
        }
        if (payMaxOfDay.compareTo(agentConfig.getPayMaxOfDay()) > 0) {
            throw BusinessException.create(TransferAccountErrorEnum.TIMES_TODAY_OVER, agentConfig.getPayMaxOfDay().toString());
        }
        // end:日转账金额上限

        BigDecimal pump = agentConfig.getPumpRatio().multiply(obj.getSendAmount()).divide(new BigDecimal(100.0)).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal sendAmount = obj.getSendAmount().subtract(pump);

        TransferAccountRecordPo po = new TransferAccountRecordPo();
        po.setOrderId(OrderIdUtil.transferAccountOrderId());
        po.setSourceId(sourceId);
        po.setSourceName(sourceName);
        po.setTargetId(targetId);
        po.setTargetName(targetName);
        po.setSendAmount(obj.getSendAmount());
        po.setRecvAmount(sendAmount);
        po.setPump(pump);
        po.setStatus(1);
        po.setCreateDate(new Date());
        po.setTotalAgentRecharge(totalAgentRecharge);
        po.setTotalAgentWater(totalAgentWater);
        this.dao.insert(po);

        GoldPaySendDto dto = new GoldPaySendDto();
        dto.setUserId(sourceId);
        dto.setPayGold(obj.getSendAmount());
        dto.setType(AccountChangeTypeEnum.TRANSFER_ACCOUNT_DEDUCT.getCode());
        this.nettySend.goldPayAsync(dto);

        UserEmailDTO userEmailDTO = new UserEmailDTO();
        userEmailDTO.setTransferAccountId(po.getId());
        userEmailDTO.setUserId(po.getTargetId());
        userEmailDTO.setEmailName("代理转账");

        StringBuilder content = new StringBuilder();
        content.append("代理[id:" + po.getSourceId() + ",昵称:" + po.getTargetName() + "]");
        content.append("给你转入 " + po.getSendAmount() + " 金币,");
        content.append("扣除手续费 " + po.getPump() + " 金币,");
        content.append("实际到账 " + po.getRecvAmount() + " 金币,请接收。");
        userEmailDTO.setEmailContent(content.toString());
        userEmailDTO.setItemType(ItemTypeEnum.CODE_GOLD.getCode());
        userEmailDTO.setItemNum(null);// TODO
        userEmailDTO.setSendDate(new Date());
        userEmailDTO.setExpireDate(emailExpireDate);
        this.userEmailService.saveUserEmail(userEmailDTO);
    }

    /**
     * 完成转账
     */
    public void finish(long id) {
        TransferAccountRecordPo po = this.dao.selectById(id);
        if (po == null) {
            throw BusinessException.create(CommErrorEnum.OBJ_NOT_EXIST, "" + id);
        }
        if (po.getStatus() != 1) {
            throw BusinessException.create(TransferAccountErrorEnum.ORDER_FINISH, "" + id);
        }
        po.setStatus(2);
        po.setFinishDate(new Date());
        this.dao.updateById(po);
    }

    /**
     * 撤销转账
     */
    public void cancel(long id) {
        TransferAccountRecordPo po = this.dao.selectById(id);
        if (po == null) {
            throw BusinessException.create(CommErrorEnum.OBJ_NOT_EXIST, "" + id);
        }
        if (po.getStatus() != 1) {
            throw BusinessException.create(TransferAccountErrorEnum.ORDER_FINISH, "" + id);
        }
        po.setStatus(3);
        po.setFinishDate(new Date());
        this.dao.updateById(po);

        GoldPaySendDto dto = new GoldPaySendDto();
        dto.setUserId(po.getSourceId());
        dto.setPayGold(po.getSendAmount().negate());
        dto.setType(AccountChangeTypeEnum.TRANSFER_ACCOUNT_ROLLBACK.getCode());
        this.nettySend.goldPayAsync(dto);
        this.userEmailService.deleteUserEmail(id);
    }

    /**
     * 获取所有对象
     */
    public PagePackageRes<List<TransferAccountRecord>> getList(PageReq pageReq, TransferAccountRecordGetListRecv reqVo) {
        IPage<TransferAccountRecordPo> page = new Page<>(pageReq.getCurrent(), pageReq.getSize());
        LambdaQueryWrapper<TransferAccountRecordPo> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(reqVo.getOrderId())) {
            wrapper.eq(TransferAccountRecordPo::getOrderId, reqVo.getOrderId());
        }

        if (reqVo.getSourceId() > 0) {
            wrapper.eq(TransferAccountRecordPo::getSourceId, reqVo.getSourceId());
        }

        if (reqVo.getTargetId() > 0) {
            wrapper.eq(TransferAccountRecordPo::getTargetId, reqVo.getTargetId());
        }

        if (reqVo.getStatus() > 0) {
            wrapper.eq(TransferAccountRecordPo::getStatus, reqVo.getStatus());
        }

        if (reqVo.getStartDate() != null) {
            wrapper.ge(TransferAccountRecordPo::getCreateDate, reqVo.getStartDate());
        }
        if (reqVo.getEndDate() != null) {
            wrapper.lt(TransferAccountRecordPo::getFinishDate, reqVo.getEndDate());
        }

        wrapper.orderByDesc(TransferAccountRecordPo::getId);
        IPage<TransferAccountRecordPo> pagePos = this.dao.selectPage(page, wrapper);

        List<TransferAccountRecord> data = new ArrayList<>();
        for (TransferAccountRecordPo po : pagePos.getRecords()) {
            data.add(po);
        }

        PageRes pageRes = new PageRes();
        BeanUtil.copyProperties(pagePos, pageRes);
        PagePackageRes<List<TransferAccountRecord>> result = new PagePackageRes<>();
        result.setData(data);
        result.setPage(pageRes);
        return result;
    }
}
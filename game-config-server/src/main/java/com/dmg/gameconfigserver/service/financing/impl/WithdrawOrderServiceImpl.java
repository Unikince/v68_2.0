package com.dmg.gameconfigserver.service.financing.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.BusinessException;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.dao.finance.WithdrawOrderDao;
import com.dmg.gameconfigserver.dao.sys.SysUserDao;
import com.dmg.gameconfigserver.dao.user.UserDao;
import com.dmg.gameconfigserver.model.bean.finance.WithdrawOrderBean;
import com.dmg.gameconfigserver.model.bean.sys.SysUserBean;
import com.dmg.gameconfigserver.model.bean.user.UserBean;
import com.dmg.gameconfigserver.model.dto.finance.QueryAbnormalOrderDTO;
import com.dmg.gameconfigserver.model.dto.finance.QueryWithdrawOrderDTO;
import com.dmg.gameconfigserver.model.dto.finance.ReviewWithdrawOrderDTO;
import com.dmg.gameconfigserver.model.vo.user.UserDetailVO;
import com.dmg.gameconfigserver.service.financing.WithdrawOrderService;
import com.dmg.gameconfigserver.service.user.UserService;
import com.dmg.server.common.enums.WithdrawOrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import static com.dmg.common.core.util.MD5Util.stringToMD5;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/27 10:28
 * @Version V1.0
 **/
@Service
@Slf4j
public class WithdrawOrderServiceImpl implements WithdrawOrderService {
    @Autowired
    private WithdrawOrderDao withdrawOrderDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private SysUserDao sysUserDao;
    private static final String WITHDRAW_URL = "http://gmapi.bigfun.io/api/order/trans";
    private static final String SECRETKEY = "4LWNGV4BTUB4YCA7";

    @Override
    public IPage<WithdrawOrderBean> queryWithdrawOrderPage(QueryWithdrawOrderDTO queryWithdrawOrderDTO) {
        IPage<WithdrawOrderBean> withdrawOrderBeanIPage = withdrawOrderDao.selectPage(queryWithdrawOrderDTO.getPageCondition(), new LambdaQueryWrapper<WithdrawOrderBean>()
                .eq(queryWithdrawOrderDTO.getUserId() != null, WithdrawOrderBean::getUserId, queryWithdrawOrderDTO.getUserId())
                .eq(StringUtils.isNotEmpty(queryWithdrawOrderDTO.getOrderId()), WithdrawOrderBean::getOrderId, queryWithdrawOrderDTO.getOrderId())
                .eq(queryWithdrawOrderDTO.getOrderStatus() != null, WithdrawOrderBean::getOrderStatus, queryWithdrawOrderDTO.getOrderStatus())
                .eq(queryWithdrawOrderDTO.getReviewerId() != null, WithdrawOrderBean::getReviewerId, queryWithdrawOrderDTO.getReviewerId())
                .gt(queryWithdrawOrderDTO.getStartDate() != null, WithdrawOrderBean::getApplyDate, queryWithdrawOrderDTO.getStartDate())
                .lt(queryWithdrawOrderDTO.getEndDate() != null, WithdrawOrderBean::getApplyDate, queryWithdrawOrderDTO.getEndDate())
                .orderByDesc(WithdrawOrderBean::getApplyDate)
        );
        return withdrawOrderBeanIPage;
    }

    @Override
    public IPage<WithdrawOrderBean> queryReviewWithdrawOrderPage(QueryWithdrawOrderDTO queryWithdrawOrderDTO) {
        IPage<WithdrawOrderBean> withdrawOrderBeanIPage = withdrawOrderDao.selectPage(queryWithdrawOrderDTO.getPageCondition(), new LambdaQueryWrapper<WithdrawOrderBean>()
                .eq(queryWithdrawOrderDTO.getUserId() != null, WithdrawOrderBean::getUserId, queryWithdrawOrderDTO.getUserId())
                .eq(StringUtils.isNotEmpty(queryWithdrawOrderDTO.getOrderId()), WithdrawOrderBean::getOrderId, queryWithdrawOrderDTO.getOrderId())
                .eq(queryWithdrawOrderDTO.getOrderStatus() != null, WithdrawOrderBean::getOrderStatus, queryWithdrawOrderDTO.getOrderStatus())
                .gt(queryWithdrawOrderDTO.getStartDate() != null, WithdrawOrderBean::getApplyDate, queryWithdrawOrderDTO.getStartDate())
                .lt(queryWithdrawOrderDTO.getEndDate() != null, WithdrawOrderBean::getApplyDate, queryWithdrawOrderDTO.getEndDate())
                .apply("(lock_user_id = {0} or lock_user_id is null)", queryWithdrawOrderDTO.getReviewerId())
                .orderByDesc(WithdrawOrderBean::getApplyDate)
        );
        return withdrawOrderBeanIPage;
    }

    @Override
    public synchronized ReviewWithdrawOrderDTO applyReviewOrder(int reviewerId, long id) {
        WithdrawOrderBean withdrawOrderBean = withdrawOrderDao.selectById(id);
        if (withdrawOrderBean == null) {
            throw new BusinessException(ResultEnum.PARAM_ERROR.getCodeStr(), ResultEnum.PARAM_ERROR.getMsg());
        }
        if (withdrawOrderBean.getLockUserId() != null && withdrawOrderBean.getLockUserId() != reviewerId) {
            throw new BusinessException(ResultEnum.OTHER_DEALING.getCodeStr(), ResultEnum.OTHER_DEALING.getMsg());
        }
        if (withdrawOrderBean.getLockUserId() == null) {
            withdrawOrderBean.setLockUserId((long) reviewerId);
            withdrawOrderDao.updateById(withdrawOrderBean);
        }
        ReviewWithdrawOrderDTO reviewWithdrawOrderDTO = new ReviewWithdrawOrderDTO();
        UserDetailVO userDetailVO = userService.getUserInfo(Math.toIntExact(withdrawOrderBean.getUserId()));
        ReviewWithdrawOrderDTO.WithdrawOrderOrderInfo orderInfo = new ReviewWithdrawOrderDTO.WithdrawOrderOrderInfo();
        BeanUtils.copyProperties(withdrawOrderBean, orderInfo);
        reviewWithdrawOrderDTO.setUserInfo(userDetailVO);
        reviewWithdrawOrderDTO.setOrderInfo(orderInfo);
        return reviewWithdrawOrderDTO;
    }

    @Override
    public String reviewOrder(int reviewerId, Long id, int reviewStatus, String reviewRemark) {
        WithdrawOrderBean withdrawOrderBean = withdrawOrderDao.selectById(id);
        if (withdrawOrderBean.getLockUserId() != null && reviewerId != withdrawOrderBean.getLockUserId()) {
            throw new BusinessException(ResultEnum.OTHER_DEALING.getCodeStr(), ResultEnum.OTHER_DEALING.getMsg());
        }
        if (reviewStatus == 0) {
            withdrawOrderDao.clearLockUser(id);
            return withdrawOrderBean.getOrderId();
        }
        if (reviewStatus == WithdrawOrderStatusEnum.NO_ARRIVE.getCode()) {
            UserBean userBean = userDao.selectById(withdrawOrderBean.getUserId());
            Map<String, Object> createOrderSignTreeMap = new TreeMap<>();
            createOrderSignTreeMap.put("outOrderNo", withdrawOrderBean.getOrderId());
            createOrderSignTreeMap.put("outUserId", String.valueOf(withdrawOrderBean.getUserId()));
            createOrderSignTreeMap.put("payType", String.valueOf(withdrawOrderBean.getBankType()));
            createOrderSignTreeMap.put("payAccount", withdrawOrderBean.getBankCardNum());
            createOrderSignTreeMap.put("payAmount", withdrawOrderBean.getWithdrawAmount().toString());
            createOrderSignTreeMap.put("channelCode", userBean.getChannelCode());
            String sign = sign(createOrderSignTreeMap);
            createOrderSignTreeMap.put("sign", sign);
            //{
            //    "code": "0",
            //    "msg": "success",
            //    "data": {
            //        "transOrderId": "xxx",
            //        "outOrderNo": "xxxx",
            //        "payAccount": "xxxxxx",
            //        "payAmount": "xxxxxx"
            //    }
            //}
            String result = HttpUtil.post(WITHDRAW_URL, createOrderSignTreeMap);
            log.info("==>JQ创建代付订单返回信息:{}", result);
            JSONObject resultJSON = JSONObject.parseObject(result);
            if (!"0".equals(resultJSON.getString("code"))) {
                throw new BusinessException(ResultEnum.JQ_WITHDRAW_ORDER_ERROR.getCodeStr(), ResultEnum.JQ_WITHDRAW_ORDER_ERROR.getMsg());
            }
        }

        SysUserBean sysUserBean = sysUserDao.selectById(reviewerId);
        withdrawOrderBean.setReviewDate(new Date());
        withdrawOrderBean.setReviewerId((long) reviewerId);
        withdrawOrderBean.setReviewerName(sysUserBean.getUserName());
        withdrawOrderBean.setReviewRemark(reviewRemark);
        withdrawOrderBean.setOrderStatus(reviewStatus);
        withdrawOrderBean.setLockUserId(null);
        withdrawOrderDao.update(withdrawOrderBean, new LambdaQueryWrapper<WithdrawOrderBean>().eq(WithdrawOrderBean::getId, id));
        return withdrawOrderBean.getOrderId();
    }

    private String sign(Map<String, Object> paramMap) {
        StringBuilder sb = new StringBuilder();
        for (String key : paramMap.keySet()) {
            String value = (String) paramMap.get(key);
            sb.append(key + "=" + value + "&");
        }
        sb.append("key=");
        sb.append(SECRETKEY);
        log.info("签名字符串==>{}", sb.toString());
        String oursign = stringToMD5(sb.toString()).toLowerCase();
        log.info("签名==>{}", oursign);
        return oursign;
    }

    @Override
    public IPage<WithdrawOrderBean> queryAbnormalOrder(QueryAbnormalOrderDTO queryAbnormalOrderDTO) {
        IPage<WithdrawOrderBean> abnormalOrderIPage = withdrawOrderDao.selectPage(queryAbnormalOrderDTO.getPageCondition(), new LambdaQueryWrapper<WithdrawOrderBean>()
                .eq(StringUtils.isNotEmpty(queryAbnormalOrderDTO.getOrderId()), WithdrawOrderBean::getOrderId, queryAbnormalOrderDTO.getOrderId())
                .in(WithdrawOrderBean::getOrderStatus, Arrays.asList(WithdrawOrderStatusEnum.NO_ARRIVE.getCode(), WithdrawOrderStatusEnum.PAYING.getCode()))
        );
        return abnormalOrderIPage;
    }

    @Override
    public String setFinish(Long id, Long dealUserId) {
        WithdrawOrderBean withdrawOrderBean = withdrawOrderDao.selectById(id);
        if (withdrawOrderBean == null) {
            throw new BusinessException(ResultEnum.PARAM_ERROR.getCodeStr(), ResultEnum.PARAM_ERROR.getMsg());
        }
        if (withdrawOrderBean.getOrderStatus() != WithdrawOrderStatusEnum.PAYING.getCode()
                && withdrawOrderBean.getOrderStatus() != WithdrawOrderStatusEnum.NO_ARRIVE.getCode()) {
            throw new BusinessException(ResultEnum.ORDER_STATUS_ERROR.getCodeStr(), ResultEnum.ORDER_STATUS_ERROR.getMsg());
        }
        withdrawOrderBean.setOrderStatus(WithdrawOrderStatusEnum.FINISH.getCode());
        withdrawOrderDao.updateById(withdrawOrderBean);
        return withdrawOrderBean.getOrderId();
    }

    @Override
    public void appropriateAgain(Long id, Long dealUserId) {
        WithdrawOrderBean withdrawOrderBean = withdrawOrderDao.selectById(id);
        if (withdrawOrderBean == null) {
            throw new BusinessException(ResultEnum.PARAM_ERROR.getCodeStr(), ResultEnum.PARAM_ERROR.getMsg());
        }
        if (withdrawOrderBean.getOrderStatus() != WithdrawOrderStatusEnum.NO_ARRIVE.getCode()) {
            throw new BusinessException(ResultEnum.ORDER_STATUS_ERROR.getCodeStr(), ResultEnum.ORDER_STATUS_ERROR.getMsg());
        }
        UserBean userBean = userDao.selectById(withdrawOrderBean.getUserId());
        Map<String, Object> createOrderSignTreeMap = new TreeMap<>();
        createOrderSignTreeMap.put("outOrderNo", withdrawOrderBean.getOrderId());
        createOrderSignTreeMap.put("outUserId", String.valueOf(withdrawOrderBean.getUserId()));
        createOrderSignTreeMap.put("payType", String.valueOf(withdrawOrderBean.getBankType()));
        createOrderSignTreeMap.put("payAccount", withdrawOrderBean.getBankCardNum());
        createOrderSignTreeMap.put("payAmount", withdrawOrderBean.getWithdrawAmount().toString());
        createOrderSignTreeMap.put("channelCode", userBean.getChannelCode());
        String sign = sign(createOrderSignTreeMap);
        createOrderSignTreeMap.put("sign", sign);
        //{
        //    "code": "0",
        //    "msg": "success",
        //    "data": {
        //        "transOrderId": "xxx",
        //        "outOrderNo": "xxxx",
        //        "payAccount": "xxxxxx",
        //        "payAmount": "xxxxxx"
        //    }
        //}
        String result = HttpUtil.post(WITHDRAW_URL, createOrderSignTreeMap);
        log.info("==>JQ创建代付订单返回信息:{}", result);
        JSONObject resultJSON = JSONObject.parseObject(result);
        if (!"0".equals(resultJSON.getString("code"))) {
            throw new BusinessException(ResultEnum.JQ_WITHDRAW_ORDER_ERROR.getCodeStr(), ResultEnum.JQ_WITHDRAW_ORDER_ERROR.getMsg());
        }

    }

    @Override
    public String refuse(Long id, Long dealUserId, String remark) {
        WithdrawOrderBean withdrawOrderBean = withdrawOrderDao.selectById(id);
        if (withdrawOrderBean == null) {
            throw new BusinessException(ResultEnum.PARAM_ERROR.getCodeStr(), ResultEnum.PARAM_ERROR.getMsg());
        }
        if (withdrawOrderBean.getOrderStatus() != WithdrawOrderStatusEnum.WAITE_REVIEW.getCode()) {
            throw new BusinessException(ResultEnum.ORDER_STATUS_ERROR.getCodeStr(), ResultEnum.ORDER_STATUS_ERROR.getMsg());
        }
        SysUserBean sysUserBean = sysUserDao.selectById(dealUserId);
        withdrawOrderBean.setAppropriateRemark(remark);
        withdrawOrderBean.setAppropriatorId(dealUserId);
        withdrawOrderBean.setAppropriatorName(sysUserBean.getUserName());
        withdrawOrderBean.setOrderStatus(WithdrawOrderStatusEnum.REFUSE_WITHDRAW.getCode());
        withdrawOrderDao.updateById(withdrawOrderBean);
        return withdrawOrderBean.getOrderId();
    }

    @Override
    public BigDecimal countTodayWithdraw() {
        BigDecimal data = withdrawOrderDao.countTodayWithdraw(WithdrawOrderStatusEnum.FINISH.getCode());
        return data == null ? BigDecimal.ZERO : data;
    }


}
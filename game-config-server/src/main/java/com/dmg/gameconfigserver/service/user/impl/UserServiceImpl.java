package com.dmg.gameconfigserver.service.user.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.agentserviceapi.business.agentrelation.feign.AgentRelationFeign;
import com.dmg.agentserviceapi.business.agentrelation.model.dto.AgentRelationGifaudRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.feign.AgentSettleFeign;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.AgentSettleRecordGifaudRes;
import com.dmg.agentserviceapi.core.comm.IdReq;
import com.dmg.agentserviceapi.core.error.ResultAgent;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.gameconfigserver.dao.user.BindingCardDao;
import com.dmg.gameconfigserver.dao.user.UserDao;
import com.dmg.gameconfigserver.model.bean.user.BindingCardBean;
import com.dmg.gameconfigserver.model.bean.user.UserBean;
import com.dmg.gameconfigserver.model.dto.user.UserDTO;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerChargeAndWithdrawInfo;
import com.dmg.gameconfigserver.model.vo.user.UserBindingCardVO;
import com.dmg.gameconfigserver.model.vo.user.UserDetailVO;
import com.dmg.gameconfigserver.model.vo.user.UserDetailVO.UserDetailVOBuilder;
import com.dmg.gameconfigserver.model.vo.user.UserListVO;
import com.dmg.gameconfigserver.model.vo.user.UserVO;
import com.dmg.gameconfigserver.service.statement.PlayerStatementService;
import com.dmg.gameconfigserver.service.user.UserService;
import com.dmg.server.common.util.DmgBeanUtils;

import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/20 16:37
 * @Version V1.0
 **/
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BindingCardDao bindingCardDao;
    @Autowired
    private PlayerStatementService playerStatementService;
    @Autowired
    private AgentRelationFeign agentRelationFeign;
    @Autowired
    private AgentSettleFeign agentSettleRecordFeign;

    @Value("${md5.salt}")
    private String salt;

    @Autowired
    private RedisUtil redisUtil;

    private static String sessionKey = "session:";

    @Override
    public IPage<UserListVO> getUserPage(UserDTO userDTO) {
        Page page = new Page(userDTO.getCurrent(), userDTO.getSize());
        IPage<UserListVO> result = this.userDao.getUserPage(page, userDTO.getUserId(), userDTO.getUserName(), userDTO.getPhone());
        if (CollectionUtils.isEmpty(result.getRecords())) {
            return result;
        }
        result.getRecords().forEach(userListVO -> {
            if (this.redisUtil.hasKey(sessionKey.concat(String.valueOf(userListVO.getId())))) {
                userListVO.setIsOnline(true);
            }
        });
        return result;
    }

    @Override
    public UserDetailVO getUserInfo(Integer id) {
        UserDetailVOBuilder result = UserDetailVO.builder();

        // begin:玩家基本信息
        UserVO userVO = this.userDao.getUserInfoById(id);
        if (this.redisUtil.hasKey(sessionKey.concat(String.valueOf(id)))) {
            userVO.setIsOnline(true);
        }
        result.user(userVO);
        // end:玩家基本信息

        // begin:绑定银行卡信息
        BindingCardBean bindingCardBean = this.bindingCardDao.selectOne(new LambdaQueryWrapper<BindingCardBean>().eq(BindingCardBean::getUserId, id));
        if (bindingCardBean != null) {
            UserBindingCardVO userBindingCardVO = new UserBindingCardVO();
            DmgBeanUtils.copyProperties(bindingCardBean, userBindingCardVO);
            result.bindingCard(userBindingCardVO);
        }
        // end:绑定银行卡信息

        // begin:充值提款信息
        PlayerChargeAndWithdrawInfo chargeAndWithdrawInfo = this.playerStatementService.getPlayerChargeAndWithdrawInfo(id);
        result.chargeAndWithdrawInfo(chargeAndWithdrawInfo);
        // end:充值提款信息

        IdReq idReq = new IdReq();
        idReq.setId(id);

        // begin:代理信息
        ResultAgent<AgentRelationGifaudRes> agentRelationResult = this.agentRelationFeign.getInfoForAdminUserDetails(idReq);
        if (agentRelationResult.isSuccess()) {
            result.agentRelation(agentRelationResult.getData());
        }
        // end:代理信息

        // begin:业绩信息
        ResultAgent<AgentSettleRecordGifaudRes> agentPerformance = this.agentSettleRecordFeign.getInfoForAdminUserDetails(idReq);
        if (agentPerformance.isSuccess()) {
            result.agentPerformance(agentPerformance.getData());
        }
        // end:业绩信息
        return result.build();
    }

    @Override
    public void update(UserDetailVO userDetailVO) {
        if (userDetailVO.getUser() != null) {
            UserBean userBean = new UserBean();
            DmgBeanUtils.copyProperties(userDetailVO.getUser(), userBean);
            if (StringUtils.isNotEmpty(userDetailVO.getUser().getPassword())) {
                userBean.setPassword(DigestUtil.md5Hex(userDetailVO.getUser().getPassword() + this.salt));
            }
            this.userDao.updateById(userBean);
            if (StringUtils.isNotEmpty(userDetailVO.getUser().getUserName())) {
                this.cacheRemove(userDetailVO.getUser().getId());
            }
        }
        if (userDetailVO.getBindingCard() != null) {
            BindingCardBean bindingCardBean = new BindingCardBean();
            DmgBeanUtils.copyProperties(userDetailVO.getBindingCard(), bindingCardBean);
            this.bindingCardDao.updateById(bindingCardBean);
        }
    }

    @Override
    public Long getTodayLoginCount() {
        Long num = this.userDao.countTodayLoginUser();
        return num == null ? 0 : num;
    }

    @Override
    public Long getTodayRegisterCount() {
        Long num = this.userDao.countTodayRegister();
        return num == null ? 0 : num;
    }

    @CacheEvict(cacheNames = RedisRegionConfig.USER, key = "#id")
    public void cacheRemove(Long id) {
    }
}
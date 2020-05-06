package com.dmg.clubserver.process.clubinvitation;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.config.ClubLogType;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.ClubInvitationDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.dao.bean.ClubInvitationBean;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.vo.ReviewInvitationVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.service.ClubLogService;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.dmg.clubserver.config.MessageConfig.*;

/**
 * @Description 审核俱乐部邀请
 * @Author mice
 * @Date 2019/5/31 18:10
 * @Version V1.0
 **/
@Service
@Transactional
public class ReviewInvitationProcess implements AbstractMessageHandler {
    @Autowired
    private ClubInvitationDao clubInvitationDao;
    @Autowired
    private ClubDao clubDao;
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    @Autowired
    private ClubLogService clubLogService;
    private static LocationManager locationManager;

    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        ReviewInvitationProcess.locationManager = locationManager;
    }

    @Override
    public String getMessageId() {
        return CLUB_REVIEW_INVITATION;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        ReviewInvitationVO vo = params.toJavaObject(ReviewInvitationVO.class);
        // 统一处理
        if (vo.getAllProcess() != 0) {
            allProcess(vo.getRoleId(), vo.getAllProcess(), result);
        } else {
            ClubInvitationBean clubInvitationBean = clubInvitationDao.selectOne(new LambdaQueryWrapper<ClubInvitationBean>()
                    .eq(ClubInvitationBean::getClubId, vo.getClubId())
                    .eq(ClubInvitationBean::getBeInvitorId, vo.getRoleId()));
            if (clubInvitationBean == null) {
                result.setRes(ResultEnum.PARAM_ERROR.getCode());
                return;
            }
            if (vo.getReviewStatus() == 0) {
                clubInvitationDao.deleteById(clubInvitationBean.getId());
            } else {
                ClubBean clubBean = clubDao.selectOne(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getClubId, vo.getClubId()));
                if (clubBean.getCurrentMemberNum() == clubBean.getMemberNumLimit()) {
                    result.setRes(ResultEnum.CLUB_MEMBER_LIMIT.getCode());
                    clubInvitationDao.deleteById(clubInvitationBean.getId());
                    return;
                }
                int hasJoinClubCount = clubPlayerDao.selectCount(new LambdaQueryWrapper<RClubPlayerBean>().eq(RClubPlayerBean::getRoleId,vo.getRoleId()));
                if (hasJoinClubCount>=10){
                    result.setRes(ResultEnum.JOINED_CLUB_LIMIT.getCode());
                    return;
                }
                RClubPlayerBean clubPlayerBean = new RClubPlayerBean();
                clubPlayerBean.setRoleId(clubInvitationBean.getBeInvitorId());
                clubPlayerBean.setPosition(3);
                clubPlayerBean.setJoinDate(new Date());
                clubPlayerBean.setClubId(clubInvitationBean.getClubId());
                clubPlayerDao.insert(clubPlayerBean);
                clubInvitationDao.deleteById(clubInvitationBean.getId());
                clubBean.setCurrentMemberNum(clubBean.getCurrentMemberNum()+1);
                clubDao.updateById(clubBean);
                clubLogService.saveLog(clubInvitationBean.getBeInvitorId(),null,
                        ClubLogType.JOIN_CLUB.getKey(),clubInvitationBean.getClubId(),null);
            }
            MyWebSocket myWebSocket = locationManager.getWebSocket(clubInvitationBean.getInvitorId());
            if (myWebSocket != null) {
                MessageResult messageResult = new MessageResult(1, vo.getReviewStatus(), INVITATION_RESULT_NTC);
                myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
            }
        }
    }

    /**
     * @param roleId
     * @param processType
     * @return void
     * @description: 全部处理
     * @author mice
     * @date 2019/5/28
     */
    private void allProcess(Integer roleId, Integer processType, MessageResult result) {
        int reviewStatus = processType == 1 ? 1 : 0;
        List<ClubInvitationBean> clubInvitationBeanList = clubInvitationDao.selectList(new LambdaQueryWrapper<ClubInvitationBean>()
                .eq(ClubInvitationBean::getBeInvitorId, roleId));
        if (CollectionUtil.isEmpty(clubInvitationBeanList)) {
            return;
        }
        if (reviewStatus == 0){
            clubInvitationBeanList.forEach(clubInvitationBean  -> {
                clubInvitationDao.deleteById(clubInvitationBean.getId());
                MyWebSocket myWebSocket = locationManager.getWebSocket(clubInvitationBean.getInvitorId());
                if (myWebSocket!=null){
                    MessageResult messageResult = new MessageResult(1,0,INVITATION_RESULT_NTC);
                    myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
                }
            });
        }else {
            int hasJoinClubCount = clubPlayerDao.selectCount(new LambdaQueryWrapper<RClubPlayerBean>().eq(RClubPlayerBean::getRoleId,roleId));
            if (hasJoinClubCount+clubInvitationBeanList.size()>10){
                result.setRes(ResultEnum.JOINED_CLUB_LIMIT.getCode());
                return;
            }
            for (ClubInvitationBean clubInvitationBean : clubInvitationBeanList){
                ClubBean clubBean = clubDao.selectOne(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getClubId, clubInvitationBean.getClubId()));
                if (clubBean.getCurrentMemberNum() >= clubBean.getMemberNumLimit()) {
                    continue;
                }
                RClubPlayerBean clubPlayerBean = new RClubPlayerBean();
                clubPlayerBean.setRoleId(clubInvitationBean.getBeInvitorId());
                clubPlayerBean.setPosition(3);
                clubPlayerBean.setJoinDate(new Date());
                clubPlayerBean.setClubId(clubInvitationBean.getClubId());
                clubPlayerDao.insert(clubPlayerBean);
                clubInvitationDao.deleteById(clubInvitationBean.getId());
                clubBean.setCurrentMemberNum(clubBean.getCurrentMemberNum()+1);
                clubDao.updateById(clubBean);
                clubLogService.saveLog(clubInvitationBean.getBeInvitorId(),null,
                        ClubLogType.JOIN_CLUB.getKey(),clubInvitationBean.getClubId(),null);
                MyWebSocket myWebSocket = locationManager.getWebSocket(clubInvitationBean.getInvitorId());
                if (myWebSocket != null) {
                    MessageResult messageResult = new MessageResult(1, reviewStatus, INVITATION_RESULT_NTC);
                    myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));

                }
            }
        }

    }
}
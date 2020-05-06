package com.dmg.clubserver.process.clubmanage;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.config.ClubLogType;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.ClubJoinRequestDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.dao.bean.ClubJoinRequestBean;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.vo.ReviewClubLobbyRequestVO;
import com.dmg.clubserver.model.vo.ReviewClubRequestVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.process.clubrequest.ReviewClubLobbyRequestProcess;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.service.ClubLogService;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.dmg.clubserver.config.MessageConfig.REVIEW_CLUB_LOBBY_REQUES_NTC;
import static com.dmg.clubserver.config.MessageConfig.REVIEW_CLUB_REQUES;
import static java.util.function.Function.identity;

/**
 * @Description 审核俱乐部申请
 * @Author mice
 * @Date 2019/5/28 14:54
 * @Version V1.0
 **/
@Service
@Transactional
public class ReviewClubRequestProcess implements AbstractMessageHandler {
    @Autowired
    private ClubJoinRequestDao clubJoinRequestDao;
    @Autowired
    private ClubDao clubDao;
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    @Autowired
    private ClubLogService clubLogService;

    private static LocationManager locationManager;
    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        ReviewClubRequestProcess.locationManager = locationManager;
    }

    @Override
    public String getMessageId() {
        return REVIEW_CLUB_REQUES;
    }

    @Override
    @Transactional
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        ReviewClubRequestVO vo = params.toJavaObject(ReviewClubRequestVO.class);
        // 权限检查
        RClubPlayerBean manager = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId, vo.getClubId())
                .eq(RClubPlayerBean::getRoleId, vo.getRoleId()));
        if (manager.getPosition() == 3) {
            result.setRes(ResultEnum.NO_AUTH.getCode());
            return;
        }

        // 统一处理
        if (vo.getAllProcess() != 0) {
            if (!allProcess(vo)){
                result.setRes(ResultEnum.CLUB_MEMBER_LIMIT.getCode());
                return;
            }
        } else {
            ClubJoinRequestBean clubJoinRequestBean = clubJoinRequestDao.selectOne(new LambdaQueryWrapper<ClubJoinRequestBean>().eq(ClubJoinRequestBean::getClubId, vo.getClubId()).eq(ClubJoinRequestBean::getRequestorId, vo.getRequestorId()));
            if (clubJoinRequestBean == null){
                result.setRes(ResultEnum.PARAM_ERROR.getCode());
                return;
            }
            if (vo.getReviewStatus() == 0) {
                clubJoinRequestBean.setReviewDate(new Date());
                clubJoinRequestBean.setReviewerId(vo.getRoleId());
                clubJoinRequestDao.updateById(clubJoinRequestBean);
                // 操作日志
                clubLogService.saveLog(vo.getRoleId(),vo.getRequestorId(),
                        ClubLogType.REFUSE_JOIN.getKey(),vo.getClubId(),null);
            } else {
                ClubBean clubBean = clubDao.selectOne(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getClubId, vo.getClubId()));
                if (clubBean.getCurrentMemberNum() == clubBean.getMemberNumLimit()) {
                    result.setRes(ResultEnum.CLUB_MEMBER_LIMIT.getCode());
                    return;
                }
                RClubPlayerBean clubPlayerBean = new RClubPlayerBean();
                clubPlayerBean.setRoleId(clubJoinRequestBean.getRequestorId());
                clubPlayerBean.setPosition(3);
                clubPlayerBean.setJoinDate(new Date());
                clubPlayerBean.setClubId(clubJoinRequestBean.getClubId());
                clubPlayerDao.insert(clubPlayerBean);
                clubBean.setCurrentMemberNum(clubBean.getCurrentMemberNum()+1);
                clubDao.updateById(clubBean);
                clubJoinRequestDao.deleteById(clubJoinRequestBean.getId());
                clubLogService.saveLog(vo.getRoleId(),vo.getRequestorId(),
                        ClubLogType.AGREE_JOIN.getKey(),vo.getClubId(),null);
            }
            MyWebSocket myWebSocket = locationManager.getWebSocket(clubJoinRequestBean.getRequestorId());
            if (myWebSocket!=null){
                MessageResult messageResult = new MessageResult(1,vo.getReviewStatus(),REVIEW_CLUB_LOBBY_REQUES_NTC);
                myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
            }
        }
    }

    /**
     * @param vo
     * @return boolean
     * @description: 全部处理
     * @author mice
     * @date 2019/5/28
     */
    private boolean allProcess(ReviewClubRequestVO vo) {
        List<ClubJoinRequestBean> requestBeans = clubJoinRequestDao.selectList(new LambdaQueryWrapper<ClubJoinRequestBean>().eq(ClubJoinRequestBean::getClubId, vo.getClubId()).isNull(ClubJoinRequestBean::getReviewerId));
        if (CollectionUtil.isEmpty(requestBeans)) {
            return true;
        }
        if (vo.getAllProcess() == 1) {
            ClubBean clubBean = clubDao.selectOne(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getClubId, vo.getClubId()));
            if (requestBeans.size() + clubBean.getCurrentMemberNum() > clubBean.getMemberNumLimit()) {
               return false;
            }

            List<Integer> requestIds = new ArrayList<>();
            requestBeans.forEach(requestBean -> {
                RClubPlayerBean clubPlayerBean = new RClubPlayerBean();
                clubPlayerBean.setRoleId(requestBean.getRequestorId());
                clubPlayerBean.setPosition(3);
                clubPlayerBean.setJoinDate(new Date());
                clubPlayerBean.setClubId(requestBean.getClubId());
                clubPlayerDao.insert(clubPlayerBean);
                requestIds.contains(requestBean.getId());
                clubLogService.saveLog(vo.getRoleId(),requestBean.getRequestorId(),
                        ClubLogType.AGREE_JOIN.getKey(),vo.getClubId(),null);
                MyWebSocket myWebSocket = locationManager.getWebSocket(requestBean.getRequestorId());
                if (myWebSocket!=null){
                    MessageResult messageResult = new MessageResult(1,1,REVIEW_CLUB_LOBBY_REQUES_NTC);
                    myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
                }
            });
            clubJoinRequestDao.deleteBatchIds(requestIds);
            clubBean.setCurrentMemberNum(clubBean.getCurrentMemberNum()+requestIds.size());
            clubDao.updateById(clubBean);
        } else {
            requestBeans.forEach(requestBean -> {
                requestBean.setReviewDate(new Date());
                requestBean.setReviewerId(vo.getRoleId());
                clubJoinRequestDao.updateById(requestBean);
                clubLogService.saveLog(vo.getRoleId(),requestBean.getRequestorId(),
                        ClubLogType.REFUSE_JOIN.getKey(),vo.getClubId(),null);
                MyWebSocket myWebSocket = locationManager.getWebSocket(requestBean.getRequestorId());
                if (myWebSocket!=null){
                    MessageResult messageResult = new MessageResult(1,0,REVIEW_CLUB_LOBBY_REQUES_NTC);
                    myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
                }
            });
        }
        return true;
    }
}
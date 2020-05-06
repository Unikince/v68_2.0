package com.dmg.clubserver.process.clubrequest;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.ClubJoinRequestDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.dao.bean.ClubJoinRequestBean;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.vo.ReviewClubLobbyRequestVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.process.ClubLoginProcess;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.dmg.clubserver.config.MessageConfig.REVIEW_CLUB_LOBBY_REQUES;
import static com.dmg.clubserver.config.MessageConfig.REVIEW_CLUB_LOBBY_REQUES_NTC;
import static java.util.function.Function.identity;

/**
 * @Description 审核俱乐部申请(大厅)
 * @Author mice
 * @Date 2019/5/28 14:54
 * @Version V1.0
 **/
@Service
public class ReviewClubLobbyRequestProcess implements AbstractMessageHandler {
    @Autowired
    private ClubJoinRequestDao clubJoinRequestDao;
    @Autowired
    private ClubDao clubDao;
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    private static LocationManager locationManager;
    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        ReviewClubLobbyRequestProcess.locationManager = locationManager;
    }
    @Override
    public String getMessageId() {
        return REVIEW_CLUB_LOBBY_REQUES;
    }

    @Override
    @Transactional
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        ReviewClubLobbyRequestVO vo = params.toJavaObject(ReviewClubLobbyRequestVO.class);
        if (vo.getAllProcess() != 0){
            // 统一处理
            allProcess(vo.getRoleId(),vo.getAllProcess(),result);
        }else {
            ClubJoinRequestBean clubJoinRequestBean = clubJoinRequestDao.selectOne(new LambdaQueryWrapper<ClubJoinRequestBean>().eq(ClubJoinRequestBean::getClubId,vo.getClubId()).eq(ClubJoinRequestBean::getRequestorId,vo.getRequestorId()));
            if (clubJoinRequestBean ==  null){
                result.setRes(ResultEnum.PARAM_ERROR.getCode());
                return;
            }
            if (vo.getReviewStatus()==0){
                clubJoinRequestBean.setRequestDate(new Date());
                clubJoinRequestBean.setReviewerId(vo.getRoleId());
                clubJoinRequestDao.updateById(clubJoinRequestBean);
            }else {
                ClubBean clubBean = clubDao.selectOne(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getClubId,vo.getClubId()));
                if (clubBean.getCurrentMemberNum()==clubBean.getMemberNumLimit()){
                    result.setRes(ResultEnum.CLUB_MEMBER_LIMIT.getCode());
                    return;
                }
                int hasJoinClubCount = clubPlayerDao.selectCount(new LambdaQueryWrapper<RClubPlayerBean>().eq(RClubPlayerBean::getRoleId,vo.getRoleId()));
                if (hasJoinClubCount>=10){
                    result.setRes(ResultEnum.JOINED_CLUB_LIMIT.getCode());
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
            }
            MyWebSocket myWebSocket = locationManager.getWebSocket(clubJoinRequestBean.getRequestorId());
            if (myWebSocket!=null){
                MessageResult messageResult = new MessageResult(1,vo.getReviewStatus(),REVIEW_CLUB_LOBBY_REQUES_NTC);
                myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
            }
        }
    }

    /**
     * @description: 全部处理
     * @param roleId
     * @param processType
     * @return void
     * @author mice
     * @date 2019/5/28
    */
    private void allProcess(Integer roleId,Integer processType,MessageResult result){
        int reviewStatus = processType==1? 1:0;
        List<RClubPlayerBean> clubPlayerBeanList = clubPlayerDao.selectList(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getRoleId,roleId)
                .in(RClubPlayerBean::getPosition, Arrays.asList(1,2)));
        List<Integer> clubIds = clubPlayerBeanList.stream().map(RClubPlayerBean::getClubId).collect(Collectors.toList());
        List<ClubJoinRequestBean> requestBeans = clubJoinRequestDao.selectList(new LambdaQueryWrapper<ClubJoinRequestBean>().isNull(ClubJoinRequestBean::getReviewerId).in(ClubJoinRequestBean::getClubId,clubIds));
        if (CollectionUtil.isEmpty(requestBeans)){
            return;
        }
        if (processType == 1){
            Map<Integer,List<ClubJoinRequestBean>> joinRequestBeanMap = requestBeans.stream().collect(Collectors.groupingBy(ClubJoinRequestBean::getClubId));
            List<ClubBean> clubBeanList = clubDao.selectList(new LambdaQueryWrapper<ClubBean>().in(ClubBean::getClubId,clubIds));
            Map<Integer,ClubBean> clubBeanMap = clubBeanList.stream().collect(Collectors.toMap(ClubBean::getClubId,identity()));
            List<Integer> outLimitClubs = new ArrayList<>();
            joinRequestBeanMap.forEach((clubId, clubJoinRequestBeans) -> {
                if (clubJoinRequestBeans.size()+clubBeanMap.get(clubId).getCurrentMemberNum()>clubBeanMap.get(clubId).getMemberNumLimit()){
                    outLimitClubs.add(clubId);
                }
            });
            if (CollectionUtil.isNotEmpty(outLimitClubs)){
                result.setRes(ResultEnum.CLUB_MEMBER_LIMIT.getCode());
            }
            Set<Integer> requestIds = new HashSet<>();
            for (Integer clubId : joinRequestBeanMap.keySet()){
                if (!outLimitClubs.contains(clubId)){
                    joinRequestBeanMap.get(clubId).forEach(requestBean -> {
                        requestIds.add(requestBean.getId());
                        int hasJoinClubCount = clubPlayerDao.selectCount(new LambdaQueryWrapper<RClubPlayerBean>().eq(RClubPlayerBean::getRoleId,requestBean.getRequestorId()));
                        if (hasJoinClubCount>=10){
                            return;
                        }
                        RClubPlayerBean clubPlayerBean = new RClubPlayerBean();
                        clubPlayerBean.setRoleId(requestBean.getRequestorId());
                        clubPlayerBean.setPosition(3);
                        clubPlayerBean.setJoinDate(new Date());
                        clubPlayerBean.setClubId(requestBean.getClubId());
                        clubPlayerDao.insert(clubPlayerBean);
                        ClubBean clubBean = clubBeanMap.get(requestBean.getClubId());
                        clubBean.setCurrentMemberNum(clubBean.getCurrentMemberNum()+1);
                        clubDao.updateById(clubBean);
                        MyWebSocket myWebSocket = locationManager.getWebSocket(requestBean.getRequestorId());
                        if (myWebSocket!=null){
                            JSONObject msg = new JSONObject();
                            msg.put("clubId",clubId);
                            msg.put("clubName",clubBeanMap.get(clubId).getName());
                            msg.put("reviewStatus",reviewStatus);
                            MessageResult messageResult = new MessageResult(1,reviewStatus,REVIEW_CLUB_LOBBY_REQUES_NTC);
                            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
                        }

                    });
                }
            }

            clubJoinRequestDao.deleteBatchIds(requestIds);
        }else {
            requestBeans.forEach(requestBean -> {
                requestBean.setRequestDate(new Date());
                requestBean.setReviewerId(roleId);
                clubJoinRequestDao.updateById(requestBean);
                MyWebSocket myWebSocket = locationManager.getWebSocket(requestBean.getRequestorId());
                if (myWebSocket!=null){
                    MessageResult messageResult = new MessageResult(1,reviewStatus,REVIEW_CLUB_LOBBY_REQUES_NTC);
                    myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
                }
            });
        }


    }
}
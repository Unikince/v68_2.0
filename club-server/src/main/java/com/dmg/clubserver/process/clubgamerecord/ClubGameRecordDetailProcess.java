package com.dmg.clubserver.process.clubgamerecord;

import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.dao.ClubGameRecordDao;
import com.dmg.clubserver.dao.bean.ClubGameRecordBean;
import com.dmg.clubserver.model.RoleInfoBean;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.service.SynchronousPlayerDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dmg.clubserver.config.MessageConfig.CLUB_GAME_RECORD_DETAIL;

/**
 * @Description 俱乐部战绩列表
 * @Author mice
 * @Date 2019/5/31 14:48
 * @Version V1.0
 **/
@Service
public class ClubGameRecordDetailProcess implements AbstractMessageHandler {
    @Autowired
    private ClubGameRecordDao clubGameRecordDao;
    @Autowired
    private SynchronousPlayerDataService synchronousPlayerDataService;
    @Override
    public String getMessageId() {
        return CLUB_GAME_RECORD_DETAIL;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        Integer id = params.getInteger("id");
        Map<String,Object> objectMap = getDetail(id);
        result.setMsg(JSONObject.toJSON(objectMap));
    }

    private Map<String,Object> getDetail(int id) {
        Map<String,Object> all = new HashMap<>();
        ClubGameRecordBean sysGameRecordBean = clubGameRecordDao.selectById(id);
        List<ClubGameRecordBean> details = clubGameRecordDao.selectDetailByGameId(sysGameRecordBean.getGameId());
        Map<Integer,List<ClubGameRecordBean>> groupMap = details.stream().collect(Collectors.groupingBy(ClubGameRecordBean::getRound));
        Set<Integer> roleIds = details.stream().map(ClubGameRecordBean::getRoleId).collect(Collectors.toSet());
        List<RoleInfoBean> roleInfoBeanList = synchronousPlayerDataService.synchronousPlayerData(StringUtils.join(roleIds,","));
        Map<Integer,RoleInfoBean> roleInfoBeanMap = roleInfoBeanList.stream().collect(Collectors.toMap(RoleInfoBean::getRoleId, Function.identity()));

        LinkedList<Map<String,Object>>  result = new LinkedList<>();
        for (Integer round : groupMap.keySet()){
            Map<String,Object> objectMap = new HashMap<>();
            objectMap.put("round",round);
            List<Map<String,String>> scores = new ArrayList<>();
            for (ClubGameRecordBean bean : groupMap.get(round)){
                Map<String,String> score = new HashMap<>();
                score.put("roleId", bean.getRoleId()+"");
                score.put("nickName", roleInfoBeanMap.get(bean.getRoleId())==null ? "玩家已注销":roleInfoBeanMap.get(bean.getRoleId()).getNickName());
                score.put("score", bean.getScore()+"");
                scores.add(score);
            }
            objectMap.put("scores",scores);
            result.add(objectMap);
        }
        all.put("roomId",sysGameRecordBean.getRoomId());
        all.put("endDate",sysGameRecordBean.getEndDate());
        all.put("detail",result);
        return all;
    }
}
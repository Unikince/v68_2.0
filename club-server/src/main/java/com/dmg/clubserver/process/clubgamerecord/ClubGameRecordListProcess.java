package com.dmg.clubserver.process.clubgamerecord;

import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.dao.ClubGameRecordDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubGameRecordBean;
import com.dmg.clubserver.model.RoleInfoBean;
import com.dmg.clubserver.model.vo.ClubGameRecordListVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.service.SynchronousPlayerDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dmg.clubserver.config.MessageConfig.CLUB_GAME_RECORD;

/**
 * @Description 俱乐部战绩列表
 * @Author mice
 * @Date 2019/5/31 14:48
 * @Version V1.0
 **/
@Service
public class ClubGameRecordListProcess implements AbstractMessageHandler {
    @Autowired
    private ClubGameRecordDao clubGameRecordDao;
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    @Autowired
    private SynchronousPlayerDataService synchronousPlayerDataService;
    @Override
    public String getMessageId() {
        return CLUB_GAME_RECORD;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        ClubGameRecordListVO vo = params.toJavaObject(ClubGameRecordListVO.class);
        String clubIds;
        if (vo.getRoleId()!=null){
            List<Integer> clubIdList= clubPlayerDao.selectHasJionClubIdByRoleId(vo.getRoleId());
            clubIds = StringUtils.join(clubIdList,",");
        }else {
            clubIds = vo.getClubId()+"";
        }
        Map<String,Object> condition = new HashMap<>();
        condition.put("clubIds",clubIds);
        condition.put("date",new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 7));
        List<ClubGameRecordBean> gameRecordBeans = clubGameRecordDao.selectGameRecordByClubId(condition);
        Set<Integer> roleIds = new HashSet<>();
        List<Map<String, Object>> resultMap = new ArrayList<>();
        result.setMsg(JSONObject.toJSON(resultMap));
        if (!CollectionUtils.isEmpty(gameRecordBeans)) {
            // 游戏id  几场游戏
            Map<Long, List<ClubGameRecordBean>> map = new HashMap<>();
            for (ClubGameRecordBean recordBean : gameRecordBeans) {
                if (map.containsKey(recordBean.getGameId())) {
                    map.get(recordBean.getGameId()).add(recordBean);
                } else {
                    List<ClubGameRecordBean> gameRecordBeans1 = new ArrayList<>();
                    gameRecordBeans1.add(recordBean);
                    map.put(recordBean.getGameId(), gameRecordBeans1);
                }
                roleIds.add(recordBean.getRoleId());
            }
            List<RoleInfoBean> roleInfoBeanList = synchronousPlayerDataService.synchronousPlayerData(StringUtils.join(roleIds,","));
            Map<Integer,RoleInfoBean> roleInfoBeanMap = roleInfoBeanList.stream().collect(Collectors.toMap(RoleInfoBean::getRoleId, Function.identity()));
            for (Long t : map.keySet()) {
                Map<String, Object> objectMap = new HashMap<>();
                List<Map<String, String>> recordMap = new ArrayList<>();
                objectMap.put("id", map.get(t).get(0).getId());
                objectMap.put("time", map.get(t).get(0).getEndDate());
                objectMap.put("type", map.get(t).get(0).getGameType());

                for (ClubGameRecordBean recordBean : map.get(t)) {
                    Map<String, String> playerScoreMap = new HashMap<>();
                    playerScoreMap.put("roleId",recordBean.getRoleId()+"");
                    playerScoreMap.put("score", recordBean.getScore()+"");
                    playerScoreMap.put("nickName", roleInfoBeanMap.get(recordBean.getRoleId())==null ? "玩家已注销":roleInfoBeanMap.get(recordBean.getRoleId()).getNickName());
                    playerScoreMap.put("cost",recordBean.getRoomCardConsumeNum()+"");
                    recordMap.add(playerScoreMap);
                }
                objectMap.put("record", recordMap);
                resultMap.add(objectMap);
            }
            result.setMsg(JSONObject.toJSON(resultMap));
        }
    }
}
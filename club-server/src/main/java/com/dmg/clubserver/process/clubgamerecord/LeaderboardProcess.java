package com.dmg.clubserver.process.clubgamerecord;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.dao.ClubGameRecordDao;
import com.dmg.clubserver.model.RoleInfoBean;
import com.dmg.clubserver.model.dto.LeaderboardDTO;
import com.dmg.clubserver.model.vo.LeaderboardVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.service.SynchronousPlayerDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dmg.clubserver.config.MessageConfig.LEADER_BOARD;

/**
 * @Description 排行榜
 * @Author mice
 * @Date 2019/5/31 16:38
 * @Version V1.0
 **/
@Service
public class LeaderboardProcess implements AbstractMessageHandler {
    @Autowired
    private ClubGameRecordDao clubGameRecordDao;
    @Autowired
    private SynchronousPlayerDataService synchronousPlayerDataService;
    @Override
    public String getMessageId() {
        return LEADER_BOARD;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        LeaderboardVO vo = params.toJavaObject(LeaderboardVO.class);
        List<LeaderboardDTO> leaderboardProcessList = clubGameRecordDao.selectLeaderboard(vo.getClubId(),vo.getDate()+"%");
        if (CollectionUtil.isEmpty(leaderboardProcessList)){
            result.setMsg(JSONObject.toJSON(new ArrayList<>()));
            return;
        }
        List<Integer> roleIds = leaderboardProcessList.stream().map(LeaderboardDTO::getRoleId).collect(Collectors.toList());
        List<RoleInfoBean> roleInfoBeanList = synchronousPlayerDataService.synchronousPlayerData(StringUtils.join(roleIds,","));
        Map<Integer,RoleInfoBean> roleInfoBeanMap = roleInfoBeanList.stream().collect(Collectors.toMap(RoleInfoBean::getRoleId, Function.identity()));
        //leaderboardProcessList.remove(0);
        LeaderboardDTO leaderboardDTO;
        for (int i=1;i<=leaderboardProcessList.size();i++){
            leaderboardDTO = leaderboardProcessList.get(i-1);
                RoleInfoBean roleInfoBean = roleInfoBeanMap.get(leaderboardDTO.getRoleId());
                leaderboardDTO.setId(i);
                if (roleInfoBean!=null){
                    leaderboardDTO.setHeadImage(roleInfoBean.getHeadImage());
                    leaderboardDTO.setNickName(roleInfoBean.getNickName());
                }else {
                    leaderboardDTO.setNickName("玩家已注销");
                }
        }
        result.setMsg(JSONObject.toJSON(leaderboardProcessList));
    }
}
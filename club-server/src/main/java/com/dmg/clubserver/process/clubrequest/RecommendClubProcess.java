package com.dmg.clubserver.process.clubrequest;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.ClubJoinRequestDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.dao.bean.ClubJoinRequestBean;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.dto.RecommendClubDTO;
import com.dmg.clubserver.model.vo.RecommendClubVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.dmg.clubserver.config.MessageConfig.RECOMMEND_CLUB;

/**
 * @Description 推荐俱乐部(含查询)
 * @Author mice
 * @Date 2019/5/28 10:26
 * @Version V1.0
 **/
@Service
public class RecommendClubProcess implements AbstractMessageHandler {
    @Autowired
    private ClubDao clubDao;
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    @Autowired
    private ClubJoinRequestDao clubJoinRequestDao;


    @Override
    public String getMessageId() {
        return RECOMMEND_CLUB;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        RecommendClubVO vo = params.toJavaObject(RecommendClubVO.class);
        List<RecommendClubDTO> recommendClubDTOList = new ArrayList<>();
        // 有查询条件
        if (vo.getClubId() != null){
            ClubBean clubBean = clubDao.selectOne(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getClubId,vo.getClubId()));
            if (clubBean == null){
                result.setMsg(JSONObject.toJSON(recommendClubDTOList));
                return;
            }
            RecommendClubDTO  recommendClubDTO = new RecommendClubDTO();
            BeanUtils.copyProperties(clubBean,recommendClubDTO);
            RClubPlayerBean clubPlayerBean = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                    .eq(RClubPlayerBean::getClubId,vo.getClubId())
                    .eq(RClubPlayerBean::getRoleId,vo.getRoleId()));
            if (clubPlayerBean !=null){
                recommendClubDTO.setRequsetStatus(2);
            }else {
                ClubJoinRequestBean clubJoinRequestBean = clubJoinRequestDao.selectOne(new LambdaQueryWrapper<ClubJoinRequestBean>()
                        .eq(ClubJoinRequestBean::getClubId,vo.getClubId())
                        .eq(ClubJoinRequestBean::getRequestorId,vo.getRoleId())
                        .isNull(ClubJoinRequestBean::getReviewerId));
                if (clubJoinRequestBean != null){
                    recommendClubDTO.setRequsetStatus(1);
                }else {
                    recommendClubDTO.setRequsetStatus(0);
                }
            }
            recommendClubDTOList.add(recommendClubDTO);
            result.setMsg(JSONObject.toJSON(recommendClubDTOList));
            return;
        }
        // 无查询条件
        List<Integer> hasRequestClubIds = clubJoinRequestDao.selectHasRequestClubIdByRoleId(vo.getRoleId());
        List<Integer> hasJoinClubIds = clubPlayerDao.selectHasJionClubIdByRoleId(vo.getRoleId());
        List<Integer> outClubIds = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(hasRequestClubIds)){
            outClubIds.addAll(hasRequestClubIds);
        }
        if (CollectionUtil.isNotEmpty(hasJoinClubIds)){
            outClubIds.addAll(hasJoinClubIds);
        }
        List<ClubBean> clubBeanList = clubDao.selectList(new LambdaQueryWrapper<ClubBean>().notIn(CollectionUtil.isNotEmpty(outClubIds),ClubBean::getClubId,outClubIds));
        if (CollectionUtil.isEmpty(clubBeanList)){
            result.setMsg(JSONObject.toJSON(recommendClubDTOList));
            return;
        }
        if (clubBeanList.size()<=4){
            clubBeanList.forEach(clubBean -> {
                RecommendClubDTO  recommendClubDTO = new RecommendClubDTO();
                BeanUtils.copyProperties(clubBean,recommendClubDTO);
                recommendClubDTO.setRequsetStatus(0);
                recommendClubDTOList.add(recommendClubDTO);
            });
        }else {
            ClubBean clubBean = null;
            for (int i=0;i<4;i++){
                int index = RandomUtil.randomInt(clubBeanList.size());
                clubBean = clubBeanList.remove(index);
                RecommendClubDTO  recommendClubDTO = new RecommendClubDTO();
                BeanUtils.copyProperties(clubBean,recommendClubDTO);
                recommendClubDTO.setRequsetStatus(0);
                recommendClubDTOList.add(recommendClubDTO);
            }
        }
        result.setMsg(JSONObject.toJSON(recommendClubDTOList));
    }
}
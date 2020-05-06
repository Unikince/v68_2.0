package com.dmg.clubserver.process;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.dto.ClubInnerLoginDTO;
import com.dmg.clubserver.model.table.TableManager;
import com.dmg.clubserver.model.vo.ClubInnerLoginVO;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.service.SynchronousPlayerDataService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dmg.clubserver.config.MessageConfig.CLUB_INNER_LOGIN;

/**
 * @Description 俱乐部内部登录
 * @Author mice
 * @Date 2019/5/25 16:00
 * @Version V1.0
 **/
@Service
public class ClubInnerLoginProcess implements AbstractMessageHandler{
    @Autowired
    private SynchronousPlayerDataService synchronousPlayerDataService;
    @Autowired
    private ClubDao clubDao;
    @Autowired
    private RClubPlayerDao clubPlayerDao;

    @Override
    public String getMessageId() {
        return CLUB_INNER_LOGIN;
    }


    @Override
    public void messageHandler(String userid,JSONObject params,MessageResult result) {
        ClubInnerLoginVO vo = params.toJavaObject(ClubInnerLoginVO.class);
        ClubBean clubBean = clubDao.selectOne(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getClubId,vo.getClubId()));
        if (clubBean == null){
            result.setRes(ResultEnum.LESS_PARAMS.getCode());
            return;
        }
        RClubPlayerBean clubPlayerBean = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId,vo.getClubId())
                .eq(RClubPlayerBean::getRoleId,vo.getRoleId()));
        ClubInnerLoginDTO clubInnerLoginDTO = new ClubInnerLoginDTO();
        BeanUtils.copyProperties(clubBean,clubInnerLoginDTO);
        clubInnerLoginDTO.setCreatorId(clubBean.getCreatorId());
        clubInnerLoginDTO.setTables(TableManager.instance().getTables(vo.getClubId()));
        clubInnerLoginDTO.setPlayerStatus(clubPlayerBean.getStatus());
        List<RClubPlayerBean> clubPlayerBeanList = clubPlayerDao.selectList(new LambdaQueryWrapper<RClubPlayerBean>().eq(RClubPlayerBean::getClubId,vo.getClubId()).eq(RClubPlayerBean::getPosition,2));
        if (CollectionUtil.isNotEmpty(clubPlayerBeanList)){
            clubPlayerBeanList.forEach(rClubPlayerBean -> clubInnerLoginDTO.getManagerIds().add(rClubPlayerBean.getRoleId()));
        }
        result.setMsg(JSONObject.toJSON(clubInnerLoginDTO));
    }
}
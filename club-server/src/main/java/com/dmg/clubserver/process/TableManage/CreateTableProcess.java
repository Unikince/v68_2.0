package com.dmg.clubserver.process.TableManage;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.table.Table;
import com.dmg.clubserver.model.table.TableManager;
import com.dmg.clubserver.model.vo.CreateTableVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.dmg.clubserver.config.MessageConfig.CREATE_TABLE;

/**
 * @Description 创建俱乐部房间
 * @Author mice
 * @Date 2019/6/1 10:12
 * @Version V1.0
 **/
@Service
@Slf4j
public class CreateTableProcess implements AbstractMessageHandler {
    @Autowired
    private WebSocketClient webSocketClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    @Autowired
    private ClubDao clubDao;

    @Override
    public String getMessageId() {
        return CREATE_TABLE;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        CreateTableVO vo = params.toJavaObject(CreateTableVO.class);
        RClubPlayerBean clubPlayerBean = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId,vo.getClubId())
                .eq(RClubPlayerBean::getRoleId,vo.getRoleId()));
        if (clubPlayerBean.getStatus()==1){
            result.setRes(ResultEnum.BE_FREEZED.getCode());
            return;
        }

        Integer tableCount = TableManager.instance().tableCount(vo.getClubId());
        if (tableCount>=20){
            result.setRes(ResultEnum.CLUB_TABLE_LIMIT.getCode());
            return;
        }
        ClubBean clubBean = clubDao.selectOne(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getClubId,vo.getClubId()));
        if (clubBean == null){
            result.setMsg(ResultEnum.PARAM_ERROR.getCode());
            return;
        }
        if (clubBean.getRoomCard()-vo.getCostRoomCard()<0){
            result.setRes(ResultEnum.CLUB_ROOM_CARD_LESS.getCode());
            return;
        }
        if (vo.getTableNum()!=null && vo.getTableNum()>0){
            Table table = TableManager.instance().getTableByTableNum(vo.getClubId(),vo.getTableNum());
            if (table!=null && table.getCreatorId()!=null && table.getCreatorId()>0){
                result.setRes(ResultEnum.ROOM_INFO_TIME_OUT.getCode());
                return;
            }
        }

        params.put("roleId",userid);
        if (!webSocketClient.isOpen()){
            try {
                webSocketClient.reconnectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error("=> 重连大厅失败");
            }
        }
        webSocketClient.send(params.getJSONObject("tableMsg").toJSONString());
    }
}

package com.dmg.clubserver.process.TableManage;

import static com.dmg.clubserver.config.MessageConfig.*;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.config.ClubLogType;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.service.ClubLogService;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.model.table.Table;
import com.dmg.clubserver.model.table.TableManager;
import com.dmg.clubserver.model.table.TableRule;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class CreateTableBackProcess implements AbstractMessageHandler {
    @Autowired
    private WebSocketClient webSocketClient;
    @Autowired
	private ClubDao clubDao;
    @Autowired
	private ClubLogService clubLogService;
    @Autowired
	private RClubPlayerDao clubPlayerDao;
	private static LocationManager locationManager;
	@Autowired
	public void setLocationManager(LocationManager locationManager) {
		CreateTableBackProcess.locationManager = locationManager;
	}

    @Override
    public String getMessageId() {
        return ROOM_List;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
    	result.setM(CREATE_TABLE_SUCCESS);
    	Table table = new Table();
    	table.setCostRoomCard(params.getInteger("cost"));
    	table.setCreateDate(new Date());
    	table.setCreatorId(params.getInteger("identify"));
    	table.setCurrentPlayerNum(0);
    	table.setCurrentRound(0);
    	table.setGameType(params.getInteger("smallGameType"));
    	table.setPlayerNumLimit(params.getInteger("playernum"));
    	table.setRoomId(params.getInteger("roomId"));
    	table.setTotalRound(params.getInteger("totalround"));
    	table.setTableStatus(0);
    	TableRule rule = new TableRule(params);
    	table.setTableRule(rule);
    	if (params.get("tableNum")!=null || params.getInteger("tableNum")>0){
			table.setTableNum(params.getInteger("tableNum"));
		}
    	Integer clubId = params.getInteger("clubId");
		ClubBean clubBean = clubDao.selectOne(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getClubId,clubId));
		if (clubBean.getRoomCard()-table.getCostRoomCard()<0){
			clubBean.setRoomCard(0);
		}else {
			clubBean.setRoomCard(clubBean.getRoomCard()-table.getCostRoomCard());
		}
		// 扣减房卡
		clubDao.updateById(clubBean);
		// 开房日志
		clubLogService.saveLog(table.getCreatorId(),null,
				ClubLogType.CREATE_TABLE.getKey(),clubId,table.getGameType()+"");
    	TableManager.instance().addTable(clubId, table);
    	result.setMsg(JSONObject.toJSON(params));
		List<Integer> roleIds = clubPlayerDao.selectRoleIdsByClubId(clubId);
		MessageResult messageResult = new MessageResult(1, clubBean.getRoomCard(), UPDATE_CLUB_ROOM_CARD_NTC);
		roleIds.forEach(roleId -> {
			MyWebSocket myWebSocket = locationManager.getWebSocket(roleId);
			if (myWebSocket != null) {
				myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
			}
		});
    }
}

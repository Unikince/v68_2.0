package com.dmg.zhajinhuaserver.manager.work.delay;

import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.model.bean.ZhaJinHuaD;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import com.zyhy.common_server.work.DelayTimeWork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dmg.zhajinhuaserver.config.MessageConfig.RESULT_ALL_INFO;

/**
 * 总结算任务
 *
 * @Date 2018/5/14 0014 16:39
 */
public class SumUpCalculationWork extends DelayTimeWork {

    private int roomId;

    @Override
    public void init(Object... args) {
        this.roomId = (int) args[0];
    }

    @Override
    public void go() {
		 GameRoom room = RoomManager.instance().getRoom(roomId);
         List<Map<String, Object>> result = new ArrayList<>();
         long time = System.currentTimeMillis();
         double winGold = 0;
         List<Seat> winList = new ArrayList<>();
         List<Long> kouList = new ArrayList<>();
         for(Seat ss : room.getSeatMap().values()) {
        	 if(ss.getChipsRemain() > winGold) {
        		 winGold = ss.getChipsRemain();
        	 }	
         }
         for(Seat data : room.getSeatMap().values()) {
        	 if(data.getChipsRemain() == winGold && data.isPlayed()) {
        		 winList.add(data);
        	 }
         }
         for (Seat seat : room.getSeatMap().values()) {
        	 if(!seat.isPlayed()) {
        		 continue;
        	 }	
        	 Map<String, Object> maps = new HashMap<>();
        	 maps.put(D.PLAYER_LOGO, seat.getPlayer().getHeadImgUrl());
        	 maps.put("result_info", seat.getResultInfo());
        	 maps.put(ZhaJinHuaD.WIN, seat.getChipsRemain());
        	 maps.put(D.ROLE_ID, seat.getPlayer().getRoleId());
        	 maps.put(D.PLAYER_ROLENAME, seat.getPlayer().getNickname());
        	 switch (room.getCostType()) {
        	 case 0:
        		 if(seat.getPlayer().getRoleId() == room.getCreator()) {
        			 maps.put("cost", room.getCost());
        			 kouList.add(seat.getPlayer().getRoleId());
        		 } 
        		 break;
        	 case 1:
        		 if(winList.contains(seat)) {
        			 maps.put("cost", (int)Math.round(room.getCost() / winList.size()));
        			 kouList.add(seat.getPlayer().getRoleId());
        		 }
        		 break;
        	 case 2:
        		 maps.put("cost", (int)Math.round(room.getCost() / room.getTotalPlayer()));
        		 kouList.add(seat.getPlayer().getRoleId());
        		 break;
        	 }
            
        	 if(seat.getPlayer().getRoleId() == room.getCreator()) {
        		 maps.put("iscreator", true);
        	 } else {
        		 maps.put("iscreator", false);
        	 }	
        	 maps.put("end_time", time);
        	 result.add(maps);
         }
		PushService pushService = SpringContextUtil.getBean(PushService.class);
		MessageResult messageResult = new MessageResult(RESULT_ALL_INFO,result);
		pushService.broadcast(messageResult,room);
		PlayerService playerCacheService = SpringContextUtil.getBean(PlayerService.class);
         for (Seat data : room.getSeatMap().values()) {
        	 data.getPlayer().setRoomId(0);
        	 playerCacheService.update(data.getPlayer());
        	 data.clear(true);
         }
         /*Lobby lobby = LobbyManager.instance().getLobby();
         lobby.send(JsonUtil.create().cmd(D.REMOVE_PRIVATE_ROOM).setForward(D.HALL_NEED_FORWARD)
        		 .put("roomId", room.getRoomId()).put("kou", true)
        		 .put("number", (int)Math.round(room.getCost() / kouList.size()))
        		 .put("useRoomCard", room.isUseRoomCard())
        		 .put("players", kouList).toJsonString());*/
         RoomManager.instance().removeRoom(room.getRoomId());
    }

}

package com.dmg.bcbm.core.manager;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dmg.bcbm.core.abs.role.Role;
import com.dmg.bcbm.core.config.D;
import com.dmg.bcbm.logic.entity.bcbm.Banker;
import com.dmg.bcbm.logic.entity.bcbm.Room;
import com.dmg.gameconfigserverapi.feign.GameConfigService;
import com.zyhy.common_server.util.StringUtils;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import util.ByteHelper;
import util.json.JsonUtil;

@Slf4j
public class RoomManager {
	private static RoomManager roomManager = new RoomManager();
	private RoomManager() {
	}
	public static RoomManager intance(){
		return roomManager;
	}
	
	
	
	private GameConfigService gameConfigService;
	 /** 停服标志 */
    private boolean shutdownServer = false;

    //private int initRoomId = 10000;
	
    public void init(GameConfigService gameConfigService){
		this.gameConfigService = gameConfigService;
		createRoom(1); // 创建level为1的房间
	}
    
    /**
     * 停服标志
     * @return
     */
    public boolean getShutdownServer(){
    	return this.shutdownServer;
    }
    /**
     * 游戏id
     * @return
     */
    public String getGameId(){
    	return D.GAME_ID;
    }
    
    public void setShutdownServer(boolean state){
    	shutdownServer = state;
    }
    
    /**
     * 所有房间
     * key:房间id value:房间
    */
    @Getter
    private Map<Integer, Room> roomMap = new HashMap<>();
    
	/**
	 * 通过房间id获取房间
	 * @param roomId
	 * @return 
	 */
	public Room getRoomById(int roomId){
		return this.roomMap.get(roomId);
	}
    
	/**
	 * 创建房间
	 * @param level
	 * @return
	 */
	public Room createRoom(Integer level){
        Room room = new Room();
        //room.setRoomId(initRoomId++);
        room.setRoomId(1); // 暂时只创建一个id为1的房间
        room.setCreateTime(System.currentTimeMillis());
        room.setLevel(level);
        room.setGameConfigService(gameConfigService);
        room.updateGameconfig();
        // 设置系统庄家
        Banker banker = room.getBanker();
    	banker.setRoleid(D.SYSTEMBANKER);
		banker.setNickName(D.SYSTEMBANKER);
		banker.setCount(0);
		banker.setGold(D.SYSTEMBANKERGOLD);
 		// 添加机器人
        //room.addRobot(D.INITNUM);
        roomMap.put(room.getRoomId(),room);
        log.info("createRoom success! =====> roomId = {}",room.getRoomId());
        return room;
    }
    
	
	/**
	 * 移除掉线的连接
	 */
	public void removeChannel(ChannelHandlerContext ctx){
		// 获取所有房间
		Map<Integer, Room> roomMap = getRoomMap();
		for (Room room : roomMap.values()) {
			Map<ChannelHandlerContext, String> onlineList = room.getOnlineList();
			if (onlineList.containsKey(ctx)) {
				room.removeChannel(ctx);
			}
		}
	}
	
}

package com.dmg.zhajinhuaserver.manager;

import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhuqd
 * @Date 2017年8月30日
 * @Desc
 */
public class RoomManager {

    private Logger logger = LoggerFactory.getLogger(RoomManager.class);
    private static RoomManager instance = new RoomManager();
    private ConcurrentHashMap<Integer, GameRoom> roomMap = new ConcurrentHashMap<>();
    private List<Integer> roomIds = Collections.synchronizedList(new ArrayList<Integer>());
    public static Map<Integer, Map<Integer, Integer>> robotLveelMap = new HashMap<>();
    private ConcurrentHashMap<Long, Integer> playerRoomIdMap = new ConcurrentHashMap<>();
    /** 停服标志 */
    private boolean shutdownServer = false;
    private RoomManager() {

    }

    public static RoomManager instance() {
        return instance;
    }

    public synchronized GameRoom getGameRoomId(Integer roomLevel, double gold) {
        List<GameRoom> copyRooms = new ArrayList<>();
        for (GameRoom room : roomMap.values()) {
            boolean bool = false;

            if (roomLevel == 5) {
                if (gold >= room.getLowerLimit().doubleValue() && gold <= room.getUpperLimit().doubleValue()) {
                    bool = true;
                }
            } else {
                if (roomLevel == room.getGrade()) {
                    bool = true;
                }
            }
            if (bool) {
                int peopleUpperLimit = room.getTotalPlayer();
                if (room.getPlayerNumber() < peopleUpperLimit) {
                    copyRooms.add(room);
                }
            }
        }
        Collections.sort(copyRooms, comparator);
        GameRoom room = null;
        int maxPlayer = 0;
        for (GameRoom r : copyRooms) {
            if (r.getPlayerNumber() >= maxPlayer) {
                maxPlayer = r.getPlayerNumber();
                room = r;
            }
        }
        if (room!=null){
            room.setPlayerNumber(room.getPlayerNumber()+1);
        }
        return room;
    }
    /**
     * 获取用户座位信息
     *
     * @param roomId
     * @return
     */
    public Seat getPlayerSeatInfo(int roomId, long userId){
        GameRoom gameRoom = roomMap.get(roomId);
        if (gameRoom == null){
            return null;
        }
        for (Seat seat : gameRoom.getSeatMap().values())
            if (seat.getPlayer().getRoleId() == userId){
                return seat;
            }
        return null;
    }

    private static Comparator<GameRoom> comparator = new Comparator<GameRoom>() {
        @Override
        public int compare(GameRoom o1, GameRoom o2) {
            return o1.getPlayerNumber() - o2.getPlayerNumber();
        }
    };

    private boolean isRoomIds(int ids) {
        boolean b = false;
        for (Integer id : roomIds) {
            if (id.intValue() == ids) {
                b = true;
                break;
            }
        }
        return b;
    }

    /**
     * 获取房间数据
     *
     * @param roomId
     * @return
     */
    public GameRoom getRoom(int roomId) {
        return roomMap.get(roomId);
    }

    /**
     * 添加房间
     *
     * @param room
     */
    public boolean addQuickRooom(GameRoom room) {
        if (room == null || roomMap.contains(room.getRoomId())) {
            return false;
        }
        roomMap.put(room.getRoomId(), room);
        return true;
    }

    /**
     * 移除房间
     *
     * @param roomId
     */
    public void removeRoom(int roomId) {
        roomMap.remove(roomId);
    }

    public ConcurrentHashMap<Integer, GameRoom> getRoomMap() {
        return roomMap;
    }

    public void setRoomMap(ConcurrentHashMap<Integer, GameRoom> roomMap) {
        this.roomMap = roomMap;
    }

    public ConcurrentHashMap<Long, Integer> getPlayerRoomIdMap() {
        return playerRoomIdMap;
    }

    public void setPlayerRoomIdMap(ConcurrentHashMap<Long, Integer> playerRoomIdMap) {
        this.playerRoomIdMap = playerRoomIdMap;
    }

    public boolean isShutdownServer() {
        return shutdownServer;
    }

    public void setShutdownServer(boolean shutdownServer) {
        this.shutdownServer = shutdownServer;
    }
}

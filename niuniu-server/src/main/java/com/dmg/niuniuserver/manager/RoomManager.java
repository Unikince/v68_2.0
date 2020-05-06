package com.dmg.niuniuserver.manager;

import com.dmg.niuniuserver.config.NiouNiou;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Seat;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhuqd
 * @Date 2017年8月30日
 * @Desc
 */
@Slf4j
public class RoomManager {

    private static RoomManager instance = new RoomManager();

    private ConcurrentHashMap<Integer, GameRoom> roomMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Integer, Integer> roomRoundMap = new ConcurrentHashMap<>();

    private Map<Integer, Map> roomStatusMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Long, Integer> playerRoomIdMap = new ConcurrentHashMap<>();

    public static RoomManager instance() {
        return instance;
    }

    /**
     * 停服标志
     */
    private boolean shutdownServer = false;

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
     * 获取用户座位信息
     *
     * @param roomId
     * @return
     */
    public Seat getPlayerSeatInfo(int roomId, long userId) {
        GameRoom gameRoom = roomMap.get(roomId);
        if (gameRoom == null) {
            return null;
        }
        for (Seat seat : gameRoom.getSeatMap().values())
            if (seat.getPlayer().getUserId() == userId) {
                return seat;
            }
        return null;
    }

    /**
     * @param userId
     * @return seatIndex
     * @description:
     * @author mice
     * @date 2019/7/29
     */
    public int addPlayerGold(int roomId, long userId, BigDecimal addGoldNum) {
        GameRoom room = roomMap.get(roomId);
        synchronized (room) {
            for (Seat seat : room.getSeatMap().values()) {
                if (seat.getPlayer().getUserId() != userId) {
                    continue;
                }
                seat.getPlayer().setGold(BigDecimal.valueOf(seat.getPlayer().getGold()).add(addGoldNum).doubleValue());
                seat.setChipsRemain(BigDecimal.valueOf(seat.getChipsRemain()).add(addGoldNum).doubleValue());
                return seat.getSeatId();
            }
        }
        return 0;
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
         /*GameServerService service = ServiceManager.instance().get(GameServerService.class);
        JSONObject message = JsonUtil.create().cmd(D.ROOM_LIST).setForward(D.HALL_NEED_FORWARD).put(D.ROOM, room).toJsonObject();
        service.roleSendMessage(JsonUtil.toJSONString0(message));*/
        return true;
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
                if (roomLevel == room.getLevel()) {
                    bool = true;
                }
            }
            if (bool) {
                int peopleUpperLimit = NiouNiou.FRIED_GOLDEN_FLOWER_PEOPLE_NUMBER_LIMIT;
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
        if (room != null) {
            room.setPlayerNumber(room.getPlayerNumber() + 1);
        }
        return room;
    }

    private static Comparator<GameRoom> comparator = new Comparator<GameRoom>() {
        @Override
        public int compare(GameRoom o1, GameRoom o2) {
            return o1.getSeatMap().size() - o2.getSeatMap().size();
        }
    };

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

    public ConcurrentHashMap<Integer, Integer> getRoomRoundMap() {
        return roomRoundMap;
    }

    public void setRoomRoundMap(ConcurrentHashMap<Integer, Integer> roomRoundMap) {
        this.roomRoundMap = roomRoundMap;
    }


    public Map<Integer, Map> getRoomStatusMap() {
        return roomStatusMap;
    }

    public void setRoomStatusMap(Map<Integer, Map> roomStatusMap) {
        this.roomStatusMap = roomStatusMap;
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

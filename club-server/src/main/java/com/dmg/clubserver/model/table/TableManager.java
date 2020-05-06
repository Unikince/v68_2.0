package com.dmg.clubserver.model.table;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/30 17:56
 * @Version V1.0
 **/
@Slf4j
public class TableManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private static TableManager tableManager = new TableManager();

    public static TableManager instance(){
        return tableManager;
    }

    /**
     * 牌桌  clubId map<roomId,牌桌>
     */
    private Map<Integer,Map<Integer, Table>> clubTableMap = new ConcurrentHashMap<>();

    /**
     * @description: 获取俱乐部当前房间数
     * @param clubId
     * @return java.lang.Integer
     * @author mice
     * @date 2019/6/1
    */
    public Integer tableCount(Integer clubId){
        if (clubTableMap.get(clubId)==null){
            return 0;
        }
        return  clubTableMap.get(clubId).size();
    }

    /**
     * @description: 添加俱乐部房间
     * @param clubId
     * @param table
     * @return void
     * @author mice
     * @date 2019/6/1
     */
    public void addTable(Integer clubId,Table table){
        Map<Integer, Table> tableMap = clubTableMap.get(clubId);
        if (table.getTableNum()==null||table.getTableNum()==0){
            table.setTableNum(TableManager.instance().getTableNum(clubId));
            tableMap.put(table.getRoomId(),table);
        }else {
            Integer oldRoomId=0;
            for (Integer t : tableMap.keySet()){
                if (t.equals(table.getTableNum())){
                    oldRoomId = t;
                    break;
                }
            }
            tableMap.remove(oldRoomId);
            tableMap.put(table.getRoomId(),table);
        }
        log.info("添加俱乐部房间成功,俱乐部id:{},牌桌序号:{},房间id:{}",clubId,table.getTableNum(),table.getRoomId());
    }

    /**
     * @description: 移除俱乐部房间
     * @param clubId
     * @param roomId
     * @return void
     * @author mice
     * @date 2019/6/1
    */
    public Table removeTable(Integer clubId,Integer roomId){
        Map<Integer, Table> tableMap = clubTableMap.get(clubId);
        if (CollectionUtil.isEmpty(tableMap) || !tableMap.containsKey(roomId)) return null;
        Table table = tableMap.remove(roomId);
        log.info("移除俱乐部房间成功,俱乐部id:{},牌桌序号:{},房间id:{}",clubId,table.getTableNum(),table.getRoomId());
        return table;
    }

    /**
     * @description: 移除俱乐部所有房间
     * @param clubId
     * @return void
     * @author mice
     * @date 2019/6/1
     */
    public void removeAllTable(Integer clubId){
        clubTableMap.remove(clubId);
        log.info("移除俱乐部所有房间成功,俱乐部id:{}",clubId);
    }

    /**
     * @description: 获取俱乐部某个房间
     * @param clubId
     * @param roomId
     * @return void
     * @author mice
     * @date 2019/6/1
     */
    public Table getTable(Integer clubId,Integer roomId){
        Map<Integer, Table> tableMap = clubTableMap.get(clubId);
        if (CollectionUtil.isEmpty(tableMap) || !tableMap.containsKey(roomId)) return null;
        Table table = tableMap.get(roomId);
        return table;
    }

    /**
     * @description: 获取俱乐部某个房间
     * @param clubId
     * @param tableNum
     * @return void
     * @author mice
     * @date 2019/6/1
     */
    public Table getTableByTableNum(Integer clubId,Integer tableNum){
        Map<Integer, Table> tableMap = clubTableMap.get(clubId);
        if (CollectionUtil.isEmpty(tableMap)) return null;
        for (Table table : tableMap.values()){
            if (table.getTableNum().equals(tableNum)) return table;
        }
        return null;
    }
    /**
     * @description: 获取俱乐部房间
     * @param clubId
     * @return void
     * @author mice
     * @date 2019/6/1
     */
    public Map<Integer, Table> getTable(Integer clubId){
        Map<Integer, Table> tableMap = clubTableMap.get(clubId);
        if (CollectionUtil.isEmpty(tableMap)) return null;
        return tableMap;
    }
    
    /**
     * @description: 获取可用的俱乐部房间编号
     * @param clubId
     * @return void
     * @author mice
     * @date 2019/6/1
     */
    public Integer getTableNum(Integer clubId){
        Map<Integer, Table> tableMap = clubTableMap.get(clubId);
        if (CollectionUtil.isEmpty(tableMap)) return 1;
        for(int i=1;i<=20;i++){
        	if (tableMap.get(i)==null){
        		return i;
        	}
        }
        return 1;
    }

    /**
     * @description: 获取俱乐部所有房间
     * @param clubId
     * @return void
     * @author mice
     * @date 2019/6/1
     */
    public  Map<Integer, Table> getTables(Integer clubId){
        Map<Integer, Table> tableMap = clubTableMap.get(clubId);
        // 默认3个空桌子
        if (tableMap == null){
            tableMap = new ConcurrentHashMap<>();
            for (int i=1;i<=3;i++){
                Table table = new Table();
                table.setTableNum(i);
                tableMap.put(i,table);
            }
            clubTableMap.put(clubId,tableMap);
        }
        return tableMap;
    }


    /**
     * @description: 获取座位号
     * @param clubId
     * @return Integer
     * @author mice
     * @date 2019/6/1
     */
    public Integer getSeatNum(Integer clubId,Integer roomId){
        Map<Integer, Table> tableMap = clubTableMap.get(clubId);
        Table table = tableMap.get(roomId);
        Map<Integer, Seat> seatMap = table.getSeatMap();
        if (CollectionUtil.isEmpty(seatMap)) return 1;
        for (int i=1;i<=table.getPlayerNumLimit();i++){
            if (seatMap.get(i)==null){
                return i;
            }
        }
        return 1;
    }


}
package com.dmg.bairenzhajinhuaserver.model;

import com.dmg.bairenzhajinhuaserver.common.model.BasePlayer;
import com.dmg.bairenzhajinhuaserver.common.model.BaseRoom;
import com.dmg.bairenzhajinhuaserver.common.result.MessageResult;
import com.dmg.bairenzhajinhuaserver.common.utils.SpringContextUtil;
import com.dmg.bairenzhajinhuaserver.model.constants.D;
import com.dmg.bairenzhajinhuaserver.service.PushService;
import com.dmg.bairenzhajinhuaserver.state.RoomState;
import com.dmg.bairenzhajinhuaserver.state.impl.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/29 15:21
 * @Version V1.0
 **/
@Data
public class Room extends BaseRoom {
	private static final long serialVersionUID = 1L;
	/**
     * 庄家
     */
    private Seat banker;
    /**
     * 是否为系统庄家
     */
    private boolean systemBanker;
	/**
	 * 是否有玩家下注
	 */
	private boolean hasPlayerBet = false;
    /**
     * 内场玩家
     */
    private TreeMap<String,Seat> infieldSeatMap = new TreeMap<>(String::compareTo);
    /**
     * 游戏中所有玩家座位信息
     */
    private Map<String, Seat> seatMap = new ConcurrentHashMap<>();
    /**
     *  5个牌桌位 key:牌位 value:牌桌
     */
    private Map<String,Table> tableMap = new HashMap<>();
    /**
     *  走势图记录
     */
    private Map<String, LinkedList<Map<String,Integer>>> reportFormMap = new HashMap<>();
    /**
     * 申请上庄的玩家
     */
    private LinkedList<BasePlayer> applyToZhuangPlayerList = new LinkedList<>();
    /**
     *  当局总下注额
     */
    private BigDecimal curRoundTotalBetChip;

    private Map<Boolean, Integer> winMap = new HashMap<>();
    
    private BigDecimal taiHong;//房间台红值
    
    private BigDecimal restCanBetChip;//房间剩余可投注金额
    
    private BigDecimal pump;//抽水万分比
    
    private RoomState state;//当前阶段
    
    private boolean bankerChanged;//是否换庄
    
    private long nextcountdownTime; // 下轮开始倒计时 
    
    /**
     * 赌神第一次下注位置
     */
    private String godOfGamblersFirstBetTable =null;
    
    /** 牌型倍数配置
     * key:牌型 value:倍数
     */
    private Map<String, Integer> multipleConfigMap = new HashMap<>();
    
    private Map<String,Integer> winTableMap = new HashMap<>();//牌位输赢
    public Room () {
    	
    }
    
    public Room (int roomId,int level,BigDecimal pump) {
    	this.setRoomId(roomId);
    	this.setLevel(level);
    	this.pump = pump;
    	this.init();
    	this.initState();
    }
    /**
     * 初始属性
     */
    private void init() {
    	this.setCreateTime(System.currentTimeMillis());
    	this.setRoomStatus(RoomStatus.START);
    	this.setCountdownTime(System.currentTimeMillis() + D.BET_TIME);
    	this.setSystemBanker(true);
    	for (int i = 1; i < 6; i++) {
    		Table table = new Table();
			table.setTableIndex(i+"");
			this.tableMap.put(i+"",table);
    	}
    }
    /**
     * 初始阶段状态
     */
    private void initState() {
    	this.state = new StartState(this);
    }
    /**
     * 广播消息
     * @param message
     */
    public void broadcast(MessageResult message) {
    	PushService service = SpringContextUtil.getBean(PushService.class);
        if (!this.isSystemBanker()){
        	service.push(this.getBanker().getPlayer().getUserId(),message);
        }
        // 给房间内部玩家推送广播消息
    	for(Seat seat : seatMap.values()) {
    		service.push(seat.getPlayer().getUserId(),message);
    	}
    }
    /**
     * 排除某玩家广播消息
     * @param message
     * @param playerId
     */
    public void broadcastWithOutPlayer(MessageResult message,int playerId) {
    	PushService service = SpringContextUtil.getBean(PushService.class);
    	for(Seat seat : seatMap.values()) {
    		if(seat.getPlayer().getUserId() != playerId) {
    			service.push(seat.getPlayer().getUserId(),message);
    		}
    	}
    }
    /**
     * 房间阶段操作
     */
    public void action() {
    	state.action();
    }

	public synchronized void changeState() {
		switch (getRoomStatus()) {
		case RoomStatus.START:
			state = new BetState(this);
			break;
		case RoomStatus.BET:
			if(state instanceof BetState) {
				state = new StopBetState(this);
			} else {
				state = new DealState(this);
			}
			break;
		case RoomStatus.DEAL:
			state = new SettleState(this);
			break;
		case RoomStatus.SETTLE:
			state = new StartState(this);
			break;
		default:
			state = new StartState(this);
			break;
		}
		
	}
}
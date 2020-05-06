package com.dmg.niuniuserver.model.constants;

/**
     * 椅子当前状态
     */
    public class SeatState {
    	
    	public static final int STATE_JOIN_ROOM = 0;  //逃跑
        public static final int STATE_RUN_AWAY = 1;  //逃跑
        public static final int STATE_WAIT_START = 2;  //等待开局,准备OK;
        public static final int QIANGZHUANG = 4;       //抢庄
        public static final int QIANGZHUANG_OVER = 5;  //抢庄完成
        public static final int XIAZHU = 6;            //下注
        public static final int XIAZHU_OVER = 7;       //下注完成
        public static final int KAIPAI = 8;            //开牌
        public static final int KAIPAI_OVER = 9;       //开牌完成
        public static final int END = 9;               //结束状态
        public static final int STATE_WAIT_READY = 13; //等待下一局游戏开始,等待准备;
    }
package com.dmg.niuniuserver.model.constants;

public class RoomStatus {
        public static final int STATE_WAIT_ALL_READY = 1;             //准备阶段;
        public static final int STATE_FA_PAI = 2;                     //发牌阶段;
        public static final int STATE_QIANG_ZHUANG = 3;               //抢庄阶段;
        public static final int STATE_DING_ZHUANG = 4;                //定庄阶段;
        public static final int STATE_XIAZHU = 5;                     //下注阶段;
        public static final int STATE_KAIPAI = 6;                     //开牌阶段;
        public static final int STATE_SETTLEMENT = 7;                 //结算阶段;
        public static final int STOP_STATUS = 8;                      //停服状态


    }
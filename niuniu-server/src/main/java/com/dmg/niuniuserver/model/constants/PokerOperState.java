package com.dmg.niuniuserver.model.constants;

/**
     * 玩家下注动作
     */
    public class PokerOperState {
        public static final int FOLLOWCHIPS = 1; // 跟注
        public static final int ADDCHIPS = 2; // 加注
        public static final int COMPARECARD = 3; // 比牌
        public static final int RUSH = 4; // 血拼
        public static final int FORCECOMPARE = 5; // 强制
        public static final int SEECARDS = 6; // 看牌
        public static final int AUTOCALL = 7; // 自动跟注
        public static final int DISCARD = 8; // 弃牌
        public static final int SHOWCARDS = 9; // 亮牌
//		public static final int FOLLOWEND = 10; // 跟到底
//		public static final int Stuffycard = 11; // 闷牌
    }
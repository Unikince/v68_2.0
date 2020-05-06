package com.dmg.niuniuserver.model.constants;

/**
     * 离开房间原因
     *
     * @author Administrator
     */
    public class LeaveReason {
        public static final int LEAVE_NORMAL = 0; // 正常退出
        public static final int LEAVE_NOMONEY = 1; // 没钱
        public static final int LEAVE_OFFLINE = 2; // 离线
        public static final int LEAVE_ELSE = 3; // 其他
        public static final int LEAVE_SOLVE = 4; // 强制离开
    }
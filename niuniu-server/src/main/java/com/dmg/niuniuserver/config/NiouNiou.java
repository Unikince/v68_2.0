package com.dmg.niuniuserver.config;

public class NiouNiou {

        /**
         * 牛牛游戏人数上限
         */
        public static final int FRIED_GOLDEN_FLOWER_PEOPLE_NUMBER_LIMIT = 5;


        public static final int FREE_FILED_NUMBER = 1000;

        public static enum RoomGrade {
        	/**
             * 初级场
             */
            PRIMARY(1, 50, 99999999, 50000, 1),
            /**
             * 中级场
             */
            INITERMEDIATE(5, 250, 99999999, 200000, 2),
            /**
             * 高级场
             */
            SENIOR(10, 500, 99999999, 500000, 3),
            /**
             * 特级场
             */
            SUPER(20, 1000, 99999999, 1000000, 4);
            /**
             * 房间底分
             */
            private int baseScore;
            /**
             * 玩家携带下限
             */
            private int lowerLimit;
            /**
             * 玩家携带上限
             */
            private int upperLimit;
            /**
             * 当前场次所代表的等级
             */
            private int grade;
            /**
             * 房间最高总注
             */
            private int totalBet;

            RoomGrade(int baseScore, int lowerLimit, int upperLimit, int totalBet, int grade) {
                this.baseScore = baseScore;
                this.lowerLimit = lowerLimit;
                this.upperLimit = upperLimit;
                this.grade = grade;
                this.totalBet = totalBet;
            }

            public int getTotalBet() {
                return totalBet;
            }

            public void setTotalBet(int totalBet) {
                this.totalBet = totalBet;
            }

            public int getBaseScore() {
                return baseScore;
            }

            public void setBaseScore(int baseScore) {
                this.baseScore = baseScore;
            }

            public int getLowerLimit() {
                return lowerLimit;
            }

            public void setLowerLimit(int lowerLimit) {
                this.lowerLimit = lowerLimit;
            }

            public int getUpperLimit() {
                return upperLimit;
            }

            public void setUpperLimit(int upperLimit) {
                this.upperLimit = upperLimit;
            }

            public int getGrade() {
                return grade;
            }

            public void setGrade(int grade) {
                this.grade = grade;
            }

            public static RoomGrade getIntValue(int value) {
                switch (value) {
                    case 1:
                        return PRIMARY;
                    case 2:
                        return INITERMEDIATE;
                    case 3:
                        return SENIOR;
                    case 4:
                        return SUPER;
                    default:
                        break;
                }
                return null;
            }
        }
    }
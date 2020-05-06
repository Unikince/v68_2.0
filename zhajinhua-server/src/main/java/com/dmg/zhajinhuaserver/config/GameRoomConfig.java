//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dmg.zhajinhuaserver.config;

public class GameRoomConfig {
    public static final int UNLIMITED_MONEY = 0;

    public GameRoomConfig() {
    }

    public static int getGameTypeRoomUpperNumber(int gameType) {
        switch(gameType) {
        case 2:
            return 6;
        case 3:
            return 4;
        case 4:
            return 6;
        case 5:
            return 3;
        default:
            return 0;
        }
    }

    public static class DDZ {
        public static final int FRIDE_GOLDEN_FLOWER_GAME_TYPE = 5;
        public static final int FRIED_GOLDEN_FLOWER_PEOPLE_NUMBER_LIMIT = 3;
        public static final int FREE_FILED_NUMBER = 200;

        public DDZ() {
        }

        public static enum RoomGrade {
            PRIMARY(100, 1000, 5000, 50000, 1),
            INITERMEDIATE(500, 5000, 20000, 200000, 2),
            SENIOR(1000, 20000, 100000, 500000, 3),
            SUPER(2000, 100000, 10000000, 1000000, 4);

            private int baseScore;
            private int lowerLimit;
            private int upperLimit;
            private int grade;
            private int totalBet;

            private RoomGrade(int baseScore, int lowerLimit, int upperLimit, int totalBet, int grade) {
                this.baseScore = baseScore;
                this.lowerLimit = lowerLimit;
                this.upperLimit = upperLimit;
                this.grade = grade;
                this.totalBet = totalBet;
            }

            public int getTotalBet() {
                return this.totalBet;
            }

            public void setTotalBet(int totalBet) {
                this.totalBet = totalBet;
            }

            public int getBaseScore() {
                return this.baseScore;
            }

            public void setBaseScore(int baseScore) {
                this.baseScore = baseScore;
            }

            public int getLowerLimit() {
                return this.lowerLimit;
            }

            public void setLowerLimit(int lowerLimit) {
                this.lowerLimit = lowerLimit;
            }

            public int getUpperLimit() {
                return this.upperLimit;
            }

            public void setUpperLimit(int upperLimit) {
                this.upperLimit = upperLimit;
            }

            public int getGrade() {
                return this.grade;
            }

            public void setGrade(int grade) {
                this.grade = grade;
            }

            public static GameRoomConfig.DDZ.RoomGrade getIntValue(int value) {
                switch(value) {
                case 1:
                    return PRIMARY;
                case 2:
                    return INITERMEDIATE;
                case 3:
                    return SENIOR;
                case 4:
                    return SUPER;
                default:
                    return null;
                }
            }
        }
    }

    public static class NiouNiou {
        public static final int FRIDE_GOLDEN_FLOWER_GAME_TYPE = 4;
        public static final int FRIED_GOLDEN_FLOWER_PEOPLE_NUMBER_LIMIT = 6;
        public static final int FREE_FILED_NUMBER = 200;

        public NiouNiou() {
        }

        public static enum RoomGrade {
            PRIMARY(100, 1000, 5000, 50000, 1),
            INITERMEDIATE(500, 5000, 20000, 200000, 2),
            SENIOR(1000, 20000, 100000, 500000, 3),
            SUPER(2000, 100000, 10000000, 1000000, 4);

            private int baseScore;
            private int lowerLimit;
            private int upperLimit;
            private int grade;
            private int totalBet;

            private RoomGrade(int baseScore, int lowerLimit, int upperLimit, int totalBet, int grade) {
                this.baseScore = baseScore;
                this.lowerLimit = lowerLimit;
                this.upperLimit = upperLimit;
                this.grade = grade;
                this.totalBet = totalBet;
            }

            public int getTotalBet() {
                return this.totalBet;
            }

            public void setTotalBet(int totalBet) {
                this.totalBet = totalBet;
            }

            public int getBaseScore() {
                return this.baseScore;
            }

            public void setBaseScore(int baseScore) {
                this.baseScore = baseScore;
            }

            public int getLowerLimit() {
                return this.lowerLimit;
            }

            public void setLowerLimit(int lowerLimit) {
                this.lowerLimit = lowerLimit;
            }

            public int getUpperLimit() {
                return this.upperLimit;
            }

            public void setUpperLimit(int upperLimit) {
                this.upperLimit = upperLimit;
            }

            public int getGrade() {
                return this.grade;
            }

            public void setGrade(int grade) {
                this.grade = grade;
            }

            public static GameRoomConfig.NiouNiou.RoomGrade getIntValue(int value) {
                switch(value) {
                case 1:
                    return PRIMARY;
                case 2:
                    return INITERMEDIATE;
                case 3:
                    return SENIOR;
                case 4:
                    return SUPER;
                default:
                    return null;
                }
            }
        }
    }

    public static class MaJiong {
        public static final int FRIDE_GOLDEN_FLOWER_GAME_TYPE = 3;
        public static final int FRIED_GOLDEN_FLOWER_PEOPLE_NUMBER_LIMIT = 4;
        public static final int FREE_FILED_NUMBER = 200;

        public MaJiong() {
        }

        public static enum RoomGrade {
            PRIMARY(100, 1000, 5000, 50000, 1),
            INITERMEDIATE(500, 5000, 20000, 200000, 2),
            SENIOR(1000, 20000, 100000, 500000, 3),
            SUPER(2000, 100000, 10000000, 1000000, 4);

            private int baseScore;
            private int lowerLimit;
            private int upperLimit;
            private int grade;
            private int totalBet;

            private RoomGrade(int baseScore, int lowerLimit, int upperLimit, int totalBet, int grade) {
                this.baseScore = baseScore;
                this.lowerLimit = lowerLimit;
                this.upperLimit = upperLimit;
                this.grade = grade;
                this.totalBet = totalBet;
            }

            public int getTotalBet() {
                return this.totalBet;
            }

            public void setTotalBet(int totalBet) {
                this.totalBet = totalBet;
            }

            public int getBaseScore() {
                return this.baseScore;
            }

            public void setBaseScore(int baseScore) {
                this.baseScore = baseScore;
            }

            public int getLowerLimit() {
                return this.lowerLimit;
            }

            public void setLowerLimit(int lowerLimit) {
                this.lowerLimit = lowerLimit;
            }

            public int getUpperLimit() {
                return this.upperLimit;
            }

            public void setUpperLimit(int upperLimit) {
                this.upperLimit = upperLimit;
            }

            public int getGrade() {
                return this.grade;
            }

            public void setGrade(int grade) {
                this.grade = grade;
            }

            public static GameRoomConfig.MaJiong.RoomGrade getIntValue(int value) {
                switch(value) {
                case 1:
                    return PRIMARY;
                case 2:
                    return INITERMEDIATE;
                case 3:
                    return SENIOR;
                case 4:
                    return SUPER;
                default:
                    return null;
                }
            }
        }
    }

    public static class zhajinhua {
        public static final int FRIDE_GOLDEN_FLOWER_GAME_TYPE = 2;
        public static final int FRIED_GOLDEN_FLOWER_PEOPLE_NUMBER_LIMIT = 6;
        public static final int FREE_FILED_NUMBER = 200;

        public zhajinhua() {
        }

        public static enum RoomGrade {
            PRIMARY(100, 1000, 5000, 50000, 1),
            INITERMEDIATE(500, 5000, 20000, 200000, 2),
            SENIOR(1000, 20000, 100000, 500000, 3),
            SUPER(2000, 100000, 10000000, 1000000, 4);

            private int baseScore;
            private int lowerLimit;
            private int upperLimit;
            private int grade;
            private int totalBet;

            private RoomGrade(int baseScore, int lowerLimit, int upperLimit, int totalBet, int grade) {
                this.baseScore = baseScore;
                this.lowerLimit = lowerLimit;
                this.upperLimit = upperLimit;
                this.grade = grade;
                this.totalBet = totalBet;
            }

            public int getTotalBet() {
                return this.totalBet;
            }

            public void setTotalBet(int totalBet) {
                this.totalBet = totalBet;
            }

            public int getBaseScore() {
                return this.baseScore;
            }

            public void setBaseScore(int baseScore) {
                this.baseScore = baseScore;
            }

            public int getLowerLimit() {
                return this.lowerLimit;
            }

            public void setLowerLimit(int lowerLimit) {
                this.lowerLimit = lowerLimit;
            }

            public int getUpperLimit() {
                return this.upperLimit;
            }

            public void setUpperLimit(int upperLimit) {
                this.upperLimit = upperLimit;
            }

            public int getGrade() {
                return this.grade;
            }

            public void setGrade(int grade) {
                this.grade = grade;
            }

            public static GameRoomConfig.zhajinhua.RoomGrade getIntValue(int value) {
                switch(value) {
                case 1:
                    return PRIMARY;
                case 2:
                    return INITERMEDIATE;
                case 3:
                    return SENIOR;
                case 4:
                    return SUPER;
                default:
                    return null;
                }
            }
        }
    }
}

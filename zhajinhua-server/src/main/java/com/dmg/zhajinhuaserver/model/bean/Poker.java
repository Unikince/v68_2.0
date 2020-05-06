package com.dmg.zhajinhuaserver.model.bean;

import java.io.Serializable;

/**
 * @author zhuqd
 * @Date 2017年8月29日
 * @Desc 牌
 */
public class Poker implements Comparable<Poker>, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4522915416524941824L;
    private int value; // 牌值
    private int type; // 花色，
    //

    public Poker() {
    }

    public Poker(int value, int type) {
        this.value = value;
        this.type = type;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int compareTo(Poker o) {
        // 规则中Joker>2>A>K...这里比较大小的时候
        // 将2的值变为32 进行比较，把A变为21进行比较
        // 双Q不比较花色
        int thisVlaue = this.value;
        int otherValue = o.value;
//		if (thisVlaue == 2) {
//			thisVlaue = 32;
//		}
//		if (thisVlaue == 1) {
//			thisVlaue = 21;
//		}
//		if (otherValue == 2) {
//			otherValue = 32;
//		}
//		if (otherValue == 1) {
//			otherValue = 21;
//		}

        if (thisVlaue < otherValue) {
            return 1;
        }
        if (thisVlaue > otherValue) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        if (this.value == 61) {
            return "LX";
        }
        if (this.value == 62) {
            return "BX";
        }
        String string = "";
        if (this.type == 1) {
            string += "♠";
        } else if (this.type == 2) {
            string += "♥";
        } else if (this.type == 3) {
            string += "♣";
        } else {
            string += "♦";
        }

        if (this.value == 11) {
            string += "J";
        } else if (this.value == 12) {
            string += "Q";
        } else if (this.value == 13) {
            string += "K";
        } else if (this.value == 1) {
            string += "A";
        } else {
            string += this.value;
        }
        return string;
    }

    public int fullValue() {
        return this.type * 100 + this.value;
    }
}

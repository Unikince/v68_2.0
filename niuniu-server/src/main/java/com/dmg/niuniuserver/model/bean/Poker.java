package com.dmg.niuniuserver.model.bean;

import java.io.Serializable;

/**
 * @author zhuqd
 * @Date 2017年8月29日
 * @Desc 牌
 */
public class Poker implements Comparable<Poker>, Serializable {

    private static final long serialVersionUID = 4522915416524941824L;
    private int value; // 牌值
    private int type; // 花色
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
    public Poker clone() {
        Poker stu = new Poker();
        stu.setValue(this.getValue());
        stu.setType(this.getType());
        return stu;
    }

    @Override
    public int compareTo(Poker o) {
        int thisVlaue = this.value;
        int otherValue = o.value;
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
        return "Poker{" + "value=" + this.value + ", type=" + this.type + '}';
    }

    public int fullValue() {
        return this.type * 100 + this.value;
    }
}

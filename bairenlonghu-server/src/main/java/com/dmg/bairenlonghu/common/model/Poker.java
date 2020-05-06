package com.dmg.bairenlonghu.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 牌
 * @return
 * @author mice
 * @date 2019/7/29
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Poker implements Comparable<Poker>, Serializable {
    private int value; // 牌值
    private int type; // 花色 1:黑 2:红 3:樱 4:方

    @Override
    public Poker clone() {
        Poker stu = new Poker();
        stu.setValue(this.getValue());
        stu.setType(this.getType());
        return stu;
    }

    @Override
    public int compareTo(Poker o) {
        if (this.value < o.value) {
            return 1;
        }
        if (this.value > o.value) {
            return -1;
        }
        return 0;
    }
}

package com.dmg.doudizhuserver.business.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.dmg.doudizhuserver.business.ai.RobotAI.Entity;
import com.dmg.doudizhuserver.business.model.Card;

public class DdzGoodCardPool {

    public static List<List<Integer>> goodPool1() {
        List<List<Integer>> list0 = new ArrayList<>();
        /*-------------------三张飞机三带---------------------------*/
        List<Integer> list1 = new ArrayList<>();
        list1.add(Card.DW_NUM);
        list1.add(Card.XW_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.DW_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.XW_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.DW_NUM);
        list1.add(Card.XW_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.DW_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.XW_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.DW_NUM);
        list1.add(Card.XW_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.DW_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.XW_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.DW_NUM);
        list1.add(Card.XW_NUM);
        list1.add(Card.ER_NUM);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.DW_NUM);
        list1.add(Card.ER_NUM);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.XW_NUM);
        list1.add(Card.ER_NUM);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.DW_NUM);
        list1.add(Card.XW_NUM);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.ER_NUM);
        list1.add(Card.ER_NUM);
        list0.add(list1);
        return list0;
    }

    public static List<List<Integer>> goodPool2() {
        List<List<Integer>> list0 = new ArrayList<>();
        /*-------------------三张飞机三带---------------------------*/
        List<Integer> list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SAN.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_A.num);
        list1.add(Card.HONG_TAO_A.num);
        list1.add(Card.HONG_TAO_A.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_K.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_Q.num);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list0.add(list1);
        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SAN.num);
        list0.add(list1);

        /*-------------------顺子---------------------------*/
        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_A.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_A.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_A.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_A.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_A.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_A.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_A.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_A.num);
        list0.add(list1);

        /*-------------------连对---------------------------*/
        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_Q.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_K.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_A.num);
        list1.add(Card.HONG_TAO_A.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_Q.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_K.num);
        list1.add(Card.HONG_TAO_K.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_Q.num);
        list1.add(Card.HONG_TAO_Q.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_J.num);
        list1.add(Card.HONG_TAO_J.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list1.add(Card.HONG_TAO_SHI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SAN.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_SI.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_WU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_LIU.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list0.add(list1);

        list1 = new ArrayList<>();
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_QI.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_BA.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list1.add(Card.HONG_TAO_JIU.num);
        list0.add(list1);

        return list0;
    }

    public static int randInt(int n) {
        Random r = new Random();
        return r.nextInt(n);
    }

    public static List<Card> license(String uid, List<Card> cards, int length) {
        List<Card> result = new ArrayList<>();
        Collections.shuffle(cards);
        Map<Integer, Integer> leftMap = new HashMap<>();
        for (Card card : cards) {
            if (!leftMap.containsKey(card.num)) {
                leftMap.put(card.num, 1);
            } else {
                leftMap.put(card.num, leftMap.get(card.num) + 1);
            }
        }
        List<List<Integer>> goodPool1 = goodPool1();
        List<List<Integer>> goodPool2 = goodPool2();
        int pool1Size = goodPool1.size();
        boolean ctn = true;
        List<Integer> list = new ArrayList<>();
        int leftSize = 17;
        while (pool1Size != 0 && ctn) {
            boolean ok = true;
            int randIndex = randInt(goodPool1.size());
            List<Integer> good1 = goodPool1.remove(randIndex);
            if (good1.size() > leftSize) {
                pool1Size = goodPool1.size();
                continue;
            }
            // 检测是否满足剩余张数
            Map<Integer, Integer> countMap = calculate(good1);
            for (Entry<Integer, Integer> countEntry : countMap.entrySet()) {
                int num = countEntry.getKey();
                int count = countEntry.getValue();
                int left = leftMap.get(num) == null ? 0 : leftMap.get(num);
                if (count > left) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                // 可以派发good1
                list.addAll(good1);
                leftSize = leftSize - good1.size();
                ctn = false;
            }
            pool1Size = goodPool1.size();
        }

        for (int i = 0; i < length; i++) {
            int pool2Size = goodPool2.size();
            ctn = true;
            while (pool2Size != 0 && ctn) {
                boolean ok = true;
                int randIndex = randInt(goodPool2.size());
                List<Integer> good2 = goodPool2.remove(randIndex);
                if (good2.size() > leftSize) {
                    pool2Size = goodPool2.size();
                    continue;
                }
                // 检测是否满足剩余张数
                Map<Integer, Integer> countMap = calculate(good2);
                for (Entry<Integer, Integer> countEntry : countMap.entrySet()) {
                    int num = countEntry.getKey();
                    int count = countEntry.getValue();
                    int left = leftMap.get(num) == null ? 0 : leftMap.get(num);
                    if (count > left) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    // 可以派发good2
                    list.addAll(good2);
                    leftSize = leftSize - good2.size();
                    ctn = false;
                }
                pool2Size = goodPool2.size();
            }
        }
        for (Integer num : list) {
            Card find = null;
            for (Card card : cards) {
                if (num == card.num) {
                    find = card;
                    break;
                }
            }
            if (find != null) {
                result.add(find);
                cards.remove(find);
            }
        }
        int c = result.size();
        for (int i = 0; i < 17 - c; i++) {
            result.add(cards.remove(0));
        }
        return result;
    }

    private static Map<Integer, Integer> calculate(List<Integer> good) {
        Map<Integer, Integer> countMap = new HashMap<>();
        for (Integer num : good) {
            if (!countMap.containsKey(num)) {
                countMap.put(num, 1);
            } else {
                countMap.put(num, countMap.get(num) + 1);
            }
        }
        return countMap;
    }

    public static void main(String[] args) {
        List<String> goods = new ArrayList<>();
        goods.add("1");
        List<String> nomals = new ArrayList<>();
        nomals.add("2");
        nomals.add("3");
        Map<String, List<Card>> map = createCards(goods, nomals);
        for (Entry<String, List<Card>> entry : map.entrySet()) {
            String uid = entry.getKey();
            List<Card> list = entry.getValue();
            Entity entity = RobotAI.optimumSolution(list);
            System.out.println("uid:" + uid + ",entity:" + entity);
        }
    }

    public static Map<String, List<Card>> createCards(List<String> uids) {
        if (uids.size() > 3) {
            throw new RuntimeException("must less 3 person!");
        }
        Map<String, List<Card>> map = new HashMap<>();
        List<Card> list = new ArrayList<>();
        List<Card> list1 = Arrays.asList(Card.values());
        list.addAll(list1);
        for (int i = 0; i < uids.size(); i++) {
            List<Card> cards = license(uids.get(i), list, 2 - i);
            map.put(uids.get(i), cards);
        }
        map.put("left_cards", list);
        return map;
    }

    public static Map<String, List<Card>> createCards(List<String> goods, List<String> nomals) {
        if (goods.size() + nomals.size() > 3) {
            throw new RuntimeException("must less 3 person!");
        }
        Map<String, List<Card>> map = new HashMap<>();
        List<Card> list = new ArrayList<>();
        List<Card> list1 = Arrays.asList(Card.values());
        list.addAll(list1);
        for (int i = 0; i < goods.size(); i++) {
            List<Card> cards = license(goods.get(i), list, 2 - i);
            map.put(goods.get(i), cards);
        }
        Collections.shuffle(list);
        for (int i = 0; i < nomals.size(); i++) {
            List<Card> tmp = new ArrayList<>();
            tmp.addAll(list.subList(0, 17));
            map.put(nomals.get(i), tmp);
            list.removeAll(tmp);
        }
        map.put("left_cards", list);
        return map;
    }
}

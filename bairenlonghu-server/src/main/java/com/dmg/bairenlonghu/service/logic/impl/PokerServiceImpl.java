package com.dmg.bairenlonghu.service.logic.impl;

import com.dmg.bairenlonghu.common.model.Poker;
import com.dmg.bairenlonghu.model.constants.Combination;
import com.dmg.bairenlonghu.service.logic.PokerService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/30 16:37
 * @Version V1.0
 **/
@Service
public class PokerServiceImpl implements PokerService {

    @Override
    public int getPokerType(List<Poker> pokerList) {
        if (pokerList != null && pokerList.size() > 0) {
            return pokerList.get(0).getValue() > 9 ? 0 : pokerList.get(0).getValue();
        }
        return Combination.UNDEFINE.getValue();
    }
}
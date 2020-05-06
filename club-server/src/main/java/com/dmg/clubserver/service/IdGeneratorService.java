package com.dmg.clubserver.service;

import com.dmg.clubserver.dao.ClubDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/27 13:43
 * @Version V1.0
 **/
@Service
public class IdGeneratorService {
    private int clubId = 0;

    @Autowired
    private ClubDao clubDao;

    public int getClubId(){
        synchronized (this){
            if (clubId>0){
                clubId += 1;
                return clubId;
            }
            Integer clubid = clubDao.selectMaxClubId();
            if (clubid == null){
                return 1;
            }
            clubId = clubid + 1 ;
            return clubId;
        }
    }
}
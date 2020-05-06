package com.dmg.gameconfigserver.service.record;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.bean.record.GameRecordBean;
import com.dmg.gameconfigserver.model.dto.record.GameRecordDTO;

import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:58 2019/11/20
 */
public interface GameRecordService {


    /**
     * @Author liubo
     * @Description //TODO 分页查询战绩信息
     * @Date 15:14 2019/11/20
     **/
    IPage<GameRecordBean> getGameRecord(GameRecordDTO gameRecordDTO);

    /**
     * @Author liubo
     * @Description 查询今日服务费//TODO
     * @Date 15:56 2020/3/17
     **/
    BigDecimal getTodayServiceCharge();

    /**
     * @Author liubo
     * @Description 查询今日系统盈利//TODO
     * @Date 16:04 2020/3/17
     **/
    BigDecimal getTodayProfit();
}

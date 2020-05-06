package com.dmg.server.common.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO 牌局编号生成
 * @Date 10:22 2020/1/9
 */
public class RoundIdUtils {
    /**
     * @Author liubo
     * @Description //TODO
     * @Date 10:30 2020/1/9
     **/
    public static String getGameRecordId(String gameId, String roomId) {
        return DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS").concat(gameId).concat(roomId);
    }

}

package com.dmg.zhajinhuaserver.model.dto;

import lombok.Data;

/**
 * @Description 踢出房间DTO
 * @Author Administrator
 * @Date 10:54
 * @Version V1.0
 **/
@Data
public class OutPlayerDTO {
    //踢出房间seatId
    private int seatId;
    //离开房间原因
    private int leaveReason;
    //加入房间时间戳
    private String joinRoomTimeStamp;
}

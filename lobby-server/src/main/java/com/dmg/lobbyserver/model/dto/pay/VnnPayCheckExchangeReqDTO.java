package com.dmg.lobbyserver.model.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author liubo
 * @Description //TODO 检查回兑 req
 * @Date 10:50 2020/2/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VnnPayCheckExchangeReqDTO {

    //kal.games平台的用户uid
    private String uid;

    //1到100万
    private Integer vnd;

    //订单号,唯一的字符串
    private String order_id;

    //平台分配的app_id
    private String app_id;

}

package com.dmg.lobbyserver.model.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:52 2019/12/6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VnnPayRespContentDTO {

    /**
     * 游戏编号
     */
    private String appid;
    /**
     * 请求流水id
     */
    private String req_id;
    /**
     * 处理流水id号，对账用，非常关键，如果失败，填0
     */
    private String resp_id = "0";
    /**
     * 处理结果，0-正常，其他失败错误码
     */
    private int status = 0;
    /**
     * 处理结果，0-正常，其他失败错误码
     */
    private String err = "ok";
}

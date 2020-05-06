package com.dmg.lobbyserver.model.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO 检查回兑 resp
 * @Date 10:50 2020/2/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VnnPayCheckExchangeRespDTO {

    //错误码
    private Integer ret;

    //错误消息
    private String msg;

    private Integer app_id;

    //用户的昵称
    private String name;

    //用户头像
    private String avatar;

    //剩余的额度
    private BigDecimal quota;

}

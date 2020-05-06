package com.dmg.data.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 金币充值提款接收 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoldPayRecvDto {
    /** 状态码(0:成功,-1未知错误) */
    private int code;
}

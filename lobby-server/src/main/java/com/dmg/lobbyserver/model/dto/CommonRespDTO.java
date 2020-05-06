package com.dmg.lobbyserver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:57 2019/11/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRespDTO<T> {

    private Boolean status = false;
    private Integer code;
    private T data;
    private String msg;
}

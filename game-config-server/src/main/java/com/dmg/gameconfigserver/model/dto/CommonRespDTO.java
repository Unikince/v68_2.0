package com.dmg.gameconfigserver.model.dto;

import com.dmg.gameconfigserver.common.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author liubo
 * @Description //TODO 公共 resp DTO
 * @Date 20:19 2020/1/15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonRespDTO<T> {
    /**
     * result
     */
    private ResultEnum result;
    /**
     * data
     */
    private T data;
    /**
     * 状态
     */
    private Boolean status = false;


    public static CommonRespDTO getError(ResultEnum result) {
        return CommonRespDTO.builder().result(result).status(false).build();
    }

    public static CommonRespDTO getError() {
        return CommonRespDTO.builder().result(ResultEnum.HAS_EXIT).status(false).build();
    }

    public static <T> CommonRespDTO<T> getSuccess(T data) {
        return CommonRespDTO.<T>builder().data(data).result(ResultEnum.SUCCESS).status(true).build();
    }

    public static CommonRespDTO getSuccess() {
        return CommonRespDTO.builder().result(ResultEnum.SUCCESS).status(true).build();
    }
}

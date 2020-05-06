package com.dmg.bairenzhajinhuaserver.common.result;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/25 16:05
 * @Version V1.0
 **/
@Data
public class RequsetParam<T> {
    private String m;
    private T data;
}
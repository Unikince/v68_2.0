package com.dmg.zhajinhuaserver.model.vo;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/2 19:08
 * @Version V1.0
 **/
@Data
public class DoActionVO {
    //动作类型
    private Integer actionType;
    // 下注倍数
    private Integer multiple;
}
package com.dmg.lobbyserver.model.vo;
import lombok.Data;
import java.io.Serializable;


/**
 * @Description
 * @Author jock
 * @Date 2019/6/24 0024
 * @Version V1.0
 **/
@Data
public class TypeToshowVo  implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer code; //类型编号
    private String desc;  //类型描述

}

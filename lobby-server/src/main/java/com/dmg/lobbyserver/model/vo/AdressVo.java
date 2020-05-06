package com.dmg.lobbyserver.model.vo;

import lombok.Data;

/**
 * @Description
 * @Author jock
 * @Date 2019/6/18 0018
 * @Version V1.0
 **/
@Data
public class AdressVo {
    private String phone;//收货电话
    private String receiverName;//收货人
    private String area;//收货地区
    private String  address;//详细地址
}

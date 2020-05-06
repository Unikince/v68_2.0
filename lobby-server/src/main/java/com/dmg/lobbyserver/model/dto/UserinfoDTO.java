package com.dmg.lobbyserver.model.dto;

import com.dmg.lobbyserver.dao.bean.ReceiveAddressBean;
import lombok.Data;

/**
 * @Description
 * @Author jock
 * @Date 2019/6/21 0021
 * @Version V1.0
 **/
@Data
public class UserinfoDTO {
    private  ReceiveAddressBean ReceiveAddressBean;
    private  Integer savingsWeek;//周存款
    private  Integer consumesWeek;//周流水
    /**
     * 升级条件(存款)
     */
    private  Long  upLevelDepositNum;
    /**
     * 升级条件(流水)
     */
    private  Long  upLevelTurnoverNum;
}

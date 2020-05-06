package com.dmg.gameconfigserver.model.vo.user;

import lombok.Data;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:01 2019/11/21
 */
@Data
public class UserBindingCardVO {

    private Long id;
    //账户姓名
    private String accountName;
    //银行类型(1中国农业银行 2.中国工商银行3.中国建设银行4.交通银行5.中国银行6.中国邮政储蓄银行7.招商银行)
    private Integer bankType;
    //银行卡号
    private String bankCardNum;
    //开户省份
    private String openAccountProvince;
    //开户城市
    private String openAccountCity;
    //开户支行
    private String openAccountBranchBank;
}

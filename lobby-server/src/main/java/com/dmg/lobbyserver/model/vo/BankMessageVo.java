package com.dmg.lobbyserver.model.vo;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Description
 * @Author jock
 * @Date 2019/6/20 0020
 * @Version V1.0
 **/
@Data
public class BankMessageVo {
    @NotBlank
    private String name;//银行卡姓名
    @NotBlank
    private int type;//银行卡类型
    @NotBlank
    private String CardNo;//银行卡卡号
    @NotBlank
    private String province;//开户省份
    @NotBlank
    private String city;//开户城市
    @NotBlank
    private String subBranch;//开户支行
    @NotBlank
    private String phone;//开户手机号
    @NotBlank
    private String code;//验证码

}

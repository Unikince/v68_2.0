package com.dmg.gameconfigserver.model.bean.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:35 2019/11/21
 */
@Data
@TableName("bank_card")
public class BindingCardBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer userId;
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
    //手机号
    private String phone;
}

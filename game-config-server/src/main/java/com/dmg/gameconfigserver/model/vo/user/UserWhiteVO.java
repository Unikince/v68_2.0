package com.dmg.gameconfigserver.model.vo.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:27 2020/3/16
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserWhiteVO {

    @NotNull(message = "id不能为空")
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 账户余额
     */
    private BigDecimal accountBalance;
    /**
     * 保险箱余额
     */
    private Double strongboxBalance;
    /**
     * 是否在线
     */
    private Boolean isOnline = false;

}
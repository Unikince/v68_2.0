package com.dmg.gameconfigserver.model.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:42 2020/1/6
 */
@Data
public class RobotStatementBO extends BaseRowModel {

    @ExcelProperty(value = "机器人ID", index = 0)
    private Long userId;
    /**
     * 总盈利
     */
    @ExcelProperty(value = "总盈利", index = 1)
    private BigDecimal sumWin;
    /**
     * 总下注
     */
    @ExcelProperty(value = "总下注", index = 2)
    private BigDecimal sumBet;
    /**
     * 总赔付
     */
    @ExcelProperty(value = "总赔付", index = 3)
    private BigDecimal sumPay;
    /**
     * 返奖率
     */
    @ExcelProperty(value = "返奖率", index = 4)
    private BigDecimal returnRate;
    /**
     * 服务费
     */
    @ExcelProperty(value = "服务费", index = 5)
    private BigDecimal serviceCharge;
}

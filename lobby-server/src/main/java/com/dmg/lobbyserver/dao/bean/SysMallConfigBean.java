package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/26 15:07
 * @Version V1.0
 **/
@Data
@TableName("sys_mall_config")
public class SysMallConfigBean {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 第三方商品id
     */
    private String thirdId;
    /**
     * 系统物品配置表主键ID
     */
    private Long itemId;
    /**
     * 物品原价
     */
    private BigDecimal originalPrice;
    /**
     * 物品价格类型 1:越南盾
     */
    private Integer priceType;
    /**
     * 商品折扣
     */
    private BigDecimal itemDiscount;
    /**
     * 标签类型 1:畅销 2:热卖 3:超值
     */
    private Integer itemTagType;
    /**
     * 购买后获得的商品数量
     */
    private Long itemNum;
    /**
     * 限购类型 1:不限购 2:日限购 3:周限购 4:永久限购
     */
    private Integer purchaseLimitType;
    /**
     * 限购次数
     */
    private Long purchaseLimitNum;
    /**
     * 该商品上架时间
     */
    private Date appearTime;
    /**
     * 该商品下架时间
     */
    private Date disappearTime;
    /**
     * 商品图片
     */
    private String itemPicUrl;
}
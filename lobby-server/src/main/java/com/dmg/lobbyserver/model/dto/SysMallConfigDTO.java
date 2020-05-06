package com.dmg.lobbyserver.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/26 16:01
 * @Version V1.0
 **/
@Data
public class SysMallConfigDTO {
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
     * 物品名称
     */
    private String itemName;
    /**
     * 商品描述
     */
    private String remark;
    /**
     * 物品小图片id
     */
    private String smallPicId;
    /**
     * 物品大图片id
     */
    private String bigPicId;
    /**
     * 商品图片
     */
    private String itemPicUrl;
    /**
     * 物品原价
     */
    private BigDecimal originalPrice;
    /**
     * 物品价格类型 1金币 2 钻石 3 人民币
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
}
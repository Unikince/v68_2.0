package com.dmg.lobbyserver.dao.bean;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
/**
 * @Description 兑换比例配置
 * @Author jock
 * @Date 2019/6/25 0025
 * @Version V1.0
 **/
@Data
@TableName("Sys_convertible_config")
public class SysConvertibleConfigBean  implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 兑换比例
     */
    private Integer convertibleProportion;
    /**
     *创建时间
     */
    private Date creatTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}

package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 用户分享日志
 * @Author jock
 * @Date 2019/7/5 0005
 * @Version V1.0
 **/
@Data
@TableName("sys_share_log")
public class SysShareLogBean  implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 分享类型
     */
    private Integer shareType;
    /**
     * 创建时间
     */
    private Date createDate;
}

package com.dmg.gameconfigserver.model.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/10/25 10:25
 * @Version V1.0
 **/
@Data
@TableName("t_resource_server_config")
public class ResourceServerConfigBean {

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    //顺序
    private Integer serverOrder;
    // 服务器地址
    private String resourceServer;
    //部署环境
    private Integer deployEnvironment;
}
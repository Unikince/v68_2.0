package com.dmg.data.common.message;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户端接收消息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecvMsg implements Serializable {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 请求类型 */
    private String msgId;
    /** 唯一code,用于同步区分相应数据 */
    private String unique;
    /** 状态码 */
    private int code;
    /** 数据 */
    private Object data;

    /**
     * 构造方法
     *
     * @param data 数据
     */
    public RecvMsg(Object data) {
        this.setData(data);
    }
    
    /**
     * 获取：数据
     * @return 数据
     */
    public String getData(){
        if(data!=null){
            return data.toString();
        }else{
            return null;
        }
    }

    /**
     * 设置：数据
     *
     * @param data 数据
     */
    public void setData(Object data) {
        if(data!=null){
            this.data = JSON.toJSON(data);
        }
    }
}

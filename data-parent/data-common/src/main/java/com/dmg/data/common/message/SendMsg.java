package com.dmg.data.common.message;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户端发送消息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMsg implements Serializable {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 客户端id */
    private String clientId;
    /** 请求类型 */
    private String msgId;
    /** 唯一code,用于同步区分相应数据 */
    private String unique;
    /** 数据 */
    private String data;
}

package com.dmg.agentserviceapi.core.pack;

import lombok.Data;

/**
 * 额外数据返回封包
 */
@Data
public class ExtraPackageRes<T, E> {
    /** 数据 */
    private T data;
    /** 额外数据 */
    private E extra;
}

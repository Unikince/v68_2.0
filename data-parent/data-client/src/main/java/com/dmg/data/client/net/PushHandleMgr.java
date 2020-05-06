/**
 *
 */
package com.dmg.data.client.net;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 数据中心推送消息处理管理器
 */
@Service
public class PushHandleMgr {
    /** 使用spring注入消息处理器 */
    @Autowired(required = false)
    private List<DataPushHandler> processList;

    /** 消息号对应消息处理器 */
    private static Map<String, DataPushHandler> map = new HashMap<>();

    /**
     * 加载时自动运行<br/>
     * 扫描消息id
     */
    @PostConstruct
    private void postConstruct() {
        if (this.processList == null || this.processList.isEmpty()) {
            return;
        }
        for (DataPushHandler handler : this.processList) {
            Class<?> clz = handler.getClass();
            if (!clz.isAnnotationPresent(Component.class)) {
                continue;
            }
            Component annotation = clz.getAnnotation(Component.class);
            String value = annotation.value();
            if (StringUtils.isBlank(value)) {
                continue;
            }
            map.put(value, handler);
        }
    }

    /**
     * 根据消息id查询消息处理器
     *
     * @param msgId 消息id
     * @return 消息处理器
     */
    public static DataPushHandler getHandler(String msgId) {
        if (map.containsKey(msgId)) {
            return map.get(msgId);
        }
        return null;
    }
}

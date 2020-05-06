/**
 *
 */
package com.dmg.doudizhuserver.core.msg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 消息处理管理器
 */
@Service
public class MessageMgr {
    /** 使用spring注入消息处理器 */
    @Autowired
    private List<MessageHandler> processList;

    /** 消息号对应消息处理器 */
    private Map<String, MessageHandler> map;

    /**
     * 加载时自动运行<br/>
     * 扫描消息id
     */
    @PostConstruct
    private void postConstruct() {
        this.map = new HashMap<>();
        for (MessageHandler handler : this.processList) {
            Class<?> clz = handler.getClass();
            if (!clz.isAnnotationPresent(Component.class)) {
                continue;
            }
            Component annotation = clz.getAnnotation(Component.class);
            String value = annotation.value();
            if (StringUtils.isBlank(value)) {
                continue;
            }
            this.map.put(value, handler);
        }
    }

    /**
     * 根据消息id查询消息处理器
     *
     * @param msgId 消息id
     * @return 消息处理器
     */
    public MessageHandler getHandler(String msgId) {
        if (this.map.containsKey(msgId)) {
            return this.map.get(msgId);
        }
        return null;
    }
}

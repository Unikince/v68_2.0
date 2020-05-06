package com.dmg.dataserver.common.net.annotation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.dmg.data.common.message.SendMsg;

public class ActionMapUtil {
    private static Map<String, Action> map = new HashMap<>();

    public static Object invote(SendMsg sendMsg) throws Exception {
        Action action = map.get(sendMsg.getMsgId());
        if (action != null) {
            Method method = action.getMethod();
            try {
                return method.invoke(action.getObject(), sendMsg);
            } catch (Exception e) {
                throw e;
            }
        }
        return null;
    }

    protected static void put(String key, Action action) {
        map.put(key, action);
    }

}

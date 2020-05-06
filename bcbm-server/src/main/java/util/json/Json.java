package util.json;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dmg.bcbm.core.config.D;

/**
 * @Date: 2015年9月22日 上午11:48:45
 * @Author: zhuqd
 * @Description: JSONObject不能'.'号进行级联调用--,在这里包装一层
 */
public class Json {
    private static IntPropertyFilter filter = new IntPropertyFilter();
    private final JSONObject jsonObject = new JSONObject();
    // private final JSONObject message = new JSONObject();

    /**
     * 设置key-value
     *
     * @param key
     * @param value
     * @return
     */
    public Json put(String key, Object value) {
        // message.put(key, value);

        if (value instanceof List<?>) {
            Map<Object, Object> maps = new ConcurrentHashMap<>();
            List<?> list = (List<?>) value;
            for (int i = 1; i <= list.size(); i++) {
                maps.put(i, list.get(i - 1));
            }
            // map 转换 完成,
            jsonObject.put(key, maps);
        } else {
            jsonObject.put(key, value);
        }
        return this;
    }

    /**
     * 设置是否转发给客户端状态
     *
     * @param type 0 不需要转发给客户端
     *             1 需要转发给客户端
     * @return
     */
    public Json setForward(int type) {
        jsonObject.put(D.HALL_WHETHER_FORWARD, type);
        return this;
    }

    public Json putMap(Map<String, Object> map) {
        for (String key : map.keySet()) {
            jsonObject.put(key, map.get(key));
        }
        return this;
    }

    /**
     * value
     *
     * @param value
     * @return
     */
    public Json put(Object value) {
        if (value instanceof List<?>) {
            Map<Object, Object> maps = new ConcurrentHashMap<>();
            List<?> list = (List<?>) value;
            for (int i = 1; i <= list.size(); i++) {
                maps.put(i, list.get(i - 1));
            }
            // map 转换 完成,
            jsonObject.put("data", maps);
        } else {
            jsonObject.put("data", value);
        }
        return this;
    }


    /**
     * 设置cmd(消息)号
     *
     * @param cmd
     * @return
     */
    public Json cmd(String cmd) {
        jsonObject.put("cmd", cmd);
        return this;
    }

    /**
     * 设置json键值对
     *
     * @param cmd
     * @return
     */
    public Json setJson(String cmd, Object value) {
        jsonObject.put(cmd, value);
        return this;
    }


    /**
     * 设置errorCode
     *
     * @param errorCode
     * @return
     */
    public Json errorCode(String errorCode) {
        jsonObject.put("errorCode", errorCode);
        return this;
    }

    /**
     * 设置roleId
     *
     * @param roleId
     * @retur
     */
    public Json roleId(long roleId) {
        jsonObject.put("roleId", roleId);
        return this;
    }

    public String toJsonString() {
        return JSON.toJSONString(jsonObject, filter, SerializerFeature.DisableCircularReferenceDetect);
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public JSONObject toJsonObject() {
        return jsonObject;
    }


}

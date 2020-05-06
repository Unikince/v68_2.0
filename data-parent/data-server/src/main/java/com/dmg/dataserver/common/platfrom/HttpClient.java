package com.dmg.dataserver.common.platfrom;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.hutool.http.HttpUtil;

@Service
public class HttpClient {
    @Value("${platfrom_server.host}")
    private String serverHost;
    @Value("${platfrom_server.port}")
    private String serverPort;

    /**
     * 发送get请求
     *
     * @param url 请求路径，不带host和port
     * @param params 请求参数
     * @return 结果字符串
     */
    public String get(String url, Map<String, Object> params) {
        url = "http://" + this.serverHost + ":" + this.serverPort + url;
        return HttpUtil.get(url, params);
    }
}
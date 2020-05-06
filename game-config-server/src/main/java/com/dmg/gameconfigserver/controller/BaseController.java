package com.dmg.gameconfigserver.controller;

import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.server.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.beans.PropertyEditorSupport;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

@Slf4j
public class BaseController {

    private static final ThreadLocal<HttpServletRequest> requestContainer = new ThreadLocal<HttpServletRequest>();
    private static final ThreadLocal<HttpServletResponse> responseContainer = new ThreadLocal<HttpServletResponse>();
    private static final ThreadLocal<ModelMap> modelContainer = new ThreadLocal<ModelMap>();

    /**
     * 初始化response
     *
     * @param response
     */
    @ModelAttribute
    private final void initResponse(HttpServletResponse response) {
        responseContainer.set(response);
    }

    /**
     * 获取当前线程的response对象
     *
     * @return
     */
    protected final HttpServletResponse getResponse() {
        return responseContainer.get();
    }

    /**
     * 初始化request
     *
     * @param request
     */
    @ModelAttribute
    private final void initRequest(HttpServletRequest request) {
        requestContainer.set(request);
    }

    /**
     * 获取当前线程的request对象
     *
     * @return
     */
    protected final HttpServletRequest getRequest() {
        return requestContainer.get();
    }

    /**
     * 获取session对象
     *
     * @return
     */
    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 设置model
     *
     * @param model
     */
    @ModelAttribute
    private final void initModelMap(ModelMap model) {
        modelContainer.set(model);
    }

    /**
     * 获取当前线程的modelMap对象
     *
     * @return
     */
    protected final ModelMap getModelMap() {
        return modelContainer.get();
    }

    protected final Long getUserId(){
        return (Long) getRequest().getAttribute(Constant.ATTRIBUTE_USER_ID_KEY);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        //Date类型转换：把传递进来的日期格式的字符串转成Date类型
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    private final String ipHeaderKey = "X-FORWARDED-FOR";

    protected String getIpAddr() {
        String ipAddress = getRequest().getHeader(ipHeaderKey);
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = getRequest().getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = getRequest().getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = getRequest().getRemoteAddr();
            String localIp = "127.0.0.1";
            String localIpv6 = "0:0:0:0:0:0:0:1";
            if (ipAddress.equals(localIp) || ipAddress.equals(localIpv6)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        String ipSeparate = ",";
        int ipLength = 15;
        if (ipAddress != null && ipAddress.length() > ipLength) {
            if (ipAddress.indexOf(ipSeparate) > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(ipSeparate));
            }
        }
        return ipAddress;
    }

}
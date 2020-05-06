package com.dmg.gameconfigserver.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoAuthMapping;
import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.service.sys.SysResourceService;
import com.dmg.gameconfigserver.service.sys.SysWhiteService;
import com.dmg.gameconfigserver.service.sys.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:08 2019/11/6
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysResourceService sysResourceService;

    @Autowired
    private SysWhiteService sysWhiteService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        String accessToken = request.getHeader(Constant.LOGIN_TOKEN_HEADER_KEY);
        Long loginUserId = null;
        log.info("=========>AccessToken:{},req url:{}", accessToken, url);
        //白名单验证
        if (!sysWhiteService.getIPIsRelease(this.getIpAddr(request))) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(JSONObject.toJSONString(Result.error(ResultEnum.NOT_AUTH.getCode().toString(), ResultEnum.NOT_AUTH.getMsg())));
            log.error("无权限");
            return false;
        }
        //过滤不需要登陆就能访问的方法
        Set<String> filterPath = new HashSet<>();
        filterPath.add("**/error");
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String filter : filterPath) {
            if (antPathMatcher.match(filter, url)) {
                return true;
            }
        }
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod methodHandler = (HandlerMethod) handler;
        NoNeedLoginMapping methodAnnotation = methodHandler.getMethodAnnotation(NoNeedLoginMapping.class);
        if (StringUtils.isNotEmpty(accessToken)) {
            loginUserId = tokenService.checkLoginByToken(request.getHeader(Constant.LOGIN_TOKEN_HEADER_KEY));
        }
        // 过滤不需要登陆就能访问的方法
        if (null == methodAnnotation && null == loginUserId) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(JSONObject.toJSONString(Result.error(ResultEnum.NOT_LOGIN.getCode().toString(), ResultEnum.NOT_LOGIN.getMsg())));
            log.error("未登录");
            return false;
        }
        request.setAttribute(Constant.ATTRIBUTE_USER_ID_KEY, loginUserId);

        //权限控制
        NoAuthMapping noAuthAnnotation = methodHandler.getMethodAnnotation(NoAuthMapping.class);
        //过滤不需要权限控制就能访问的方法
        if (null != noAuthAnnotation) {
            return true;
        }
        if (loginUserId != null && noAuthAnnotation == null) {
            String[] urls = url.substring(this.getURI(URI.create(url)).toString().concat(Constant.CONTEXT_PATH).length()).split("/");
            if (!sysResourceService.getURLIsRelease(loginUserId, Constant.CONTEXT_PATH.concat(urls[0]).concat("/").concat(urls[1]))) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(JSONObject.toJSONString(Result.error(ResultEnum.NOT_AUTH.getCode().toString(), ResultEnum.NOT_AUTH.getMsg())));
                log.error("无权限");
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    private String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
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
        log.info("{}", ipAddress);
        return ipAddress;
    }


    /**
     * @Author liubo
     * @Description //TODO 获取uri
     * @Date 16:08 2019/12/24
     **/
    private URI getURI(URI uri) {
        URI effectiveURI = null;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (Throwable e) {
            log.error("获取URI出现异常:{}", e);
        }
        return effectiveURI;
    }
}
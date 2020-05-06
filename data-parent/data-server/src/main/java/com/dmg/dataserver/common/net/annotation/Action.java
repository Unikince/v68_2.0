package com.dmg.dataserver.common.net.annotation;

import java.lang.reflect.Method;

class Action {
    /** 方法对象 */
    private Method method;
    /** 类对象 */
    private Object object;

    /**
     * 获取：方法对象
     *
     * @return 方法对象
     */
    public Method getMethod() {
        return this.method;
    }

    /**
     * 设置：方法对象
     *
     * @param method 方法对象
     */
    public void setMethod(Method method) {
        this.method = method;
    }

    /**
     * 获取：类对象
     *
     * @return 类对象
     */
    public Object getObject() {
        return this.object;
    }

    /**
     * 设置：类对象
     *
     * @param object 类对象
     */
    public void setObject(Object object) {
        this.object = object;
    }

}

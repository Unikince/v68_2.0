package com.dmg.gameconfigserver.service.sys.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.annotation.ColumnName;
import com.dmg.gameconfigserver.dao.sys.SysActionLogDao;
import com.dmg.gameconfigserver.model.dto.sys.SysActionLogDTO;
import com.dmg.gameconfigserver.model.dto.sys.SysActionLogPageDTO;
import com.dmg.gameconfigserver.model.vo.sys.SysActionLogVO;
import com.dmg.gameconfigserver.service.sys.SysActionLogService;
import com.dmg.gameconfigserver.service.sys.event.SysActionLogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 18:15 2019/12/24
 */
@Service
public class SysActionLogServiceImpl implements SysActionLogService {

    @Autowired
    private SysActionLogDao sysActionLogDao;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public IPage<SysActionLogVO> getActionLogList(SysActionLogPageDTO sysActionLogPageDTO) {
        Page page = new Page(sysActionLogPageDTO.getCurrent(), sysActionLogPageDTO.getSize());
        return sysActionLogDao.getSysActionLogPage(page, sysActionLogPageDTO.getUserName(), sysActionLogPageDTO.getStartDate(), sysActionLogPageDTO.getEndDate());
    }

    @Override
    public void pushActionLog(SysActionLogDTO sysActionLogDTO) {
        sysActionLogDTO.setCreateDate(new Date());
        applicationContext.publishEvent(new SysActionLogEvent(sysActionLogDTO));
    }

    @Async
    @Override
    public <T> void pushActionLog(T source, T target, String loginIp, Long userId) throws Exception {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        // 解析字段上是否有注解
        Field[] sourceFields = source.getClass().getDeclaredFields();
        for (Field sourceField : sourceFields) {
            //设置对象的访问权限，保证对private的属性的访问
            sourceField.setAccessible(true);
            Object value = sourceField.get(source);
            if (value == null || "".equals(value)) {
                continue;
            }
            boolean fieldHasAnno = sourceField.isAnnotationPresent(ColumnName.class);
            if (!fieldHasAnno) {
                continue;
            }
            Field targetField = target.getClass().getDeclaredField(sourceField.getName());
            //设置对象的访问权限，保证对private的属性的访问
            targetField.setAccessible(true);
            Object targetValue = targetField.get(target);
            if (value == targetValue || value.equals(targetValue)) {
                continue;
            }
            ColumnName fieldAnno = sourceField.getAnnotation(ColumnName.class);
            //输出注解属性
            String name = fieldAnno.name();
            String actionDesc = "";
            if (source.getClass().isAnnotationPresent(ColumnName.class)) {
                // 获取类上的注解
                ColumnName annotation = source.getClass().getAnnotation(ColumnName.class);
                Field id = sourceFields[0];
                //设置对象的访问权限，保证对private的属性的访问
                id.setAccessible(true);
                actionDesc = actionDesc.concat("将").concat(annotation.name()).concat("：")
                        .concat(String.valueOf(id.get(source))).concat("的[").concat(name)
                        .concat("]配置由").concat(String.valueOf(targetValue)).concat("修改为").concat(String.valueOf(value));
                this.pushActionLog(SysActionLogDTO.builder()
                        .actionDesc(actionDesc)
                        .createUser(userId)
                        .loginIp(loginIp).build());
            }
        }

    }
}

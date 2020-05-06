package com.dmg.server.common.model.dto;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: 分页条件
 * @return
 * @author mice
 * @date 2019/11/20
*/
@Data
public class PageConditionDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    //页数
    private Integer current = 1;
    //条数
    private Integer size = 30;
    /**
     * 排序字段
     */
    private List<String> orderByFields;
    //查询起期
    private Date startDate;
    //查询止期
    private Date endDate;
    /**
     * 排序字段
     */
    private String orderByField;

    /**
     * 是否升序
     */
    private boolean asc = false;

    public Page getPageCondition() {
        Page page = new Page(current, size);
        if (asc){
            page.setAscs(orderByFields);
            page.setAsc(orderByField);
        }else {
            page.setDesc(orderByField);
            page.setDescs(orderByFields);
        }

        return page;
    }

}

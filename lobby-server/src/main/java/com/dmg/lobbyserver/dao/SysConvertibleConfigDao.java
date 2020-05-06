package com.dmg.lobbyserver.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.SysConvertibleConfigBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 *
 *
 * @author jock
 * @email .com
 * @date 2019-04-30 14:48:56
 */
@Mapper
public interface SysConvertibleConfigDao extends BaseMapper<SysConvertibleConfigBean> {
    /**
     *
     * 查询记录
     */
    @Select("select *from Sys_convertible_config")
    SysConvertibleConfigBean getOne();
}

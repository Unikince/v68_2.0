package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.BankCardBean;
import com.dmg.lobbyserver.dao.bean.SysMallConfigBean;
import com.dmg.lobbyserver.model.dto.SysMallConfigDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统商城配置
 * 
 * @author mice
 * @email .com
 * @date 2019-11-26
 */
@Mapper
public interface SysMallConfigDao extends BaseMapper<SysMallConfigBean> {

    /**
     * @description: 获取商城配置列表
     * @param
     * @return com.dmg.lobbyserver.model.dto.SysMallConfigDTO
     * @author mice
     * @date 2019/11/26
    */
    @Select("select m.*, i.small_pic_id, i.big_pic_id from  sys_mall_config as m left join sys_item_config as i on m.item_id=i.id order by m.id")
    List<SysMallConfigDTO> selectSysMallConfig();
	
}

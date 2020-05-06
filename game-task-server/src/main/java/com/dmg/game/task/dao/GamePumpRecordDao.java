package com.dmg.game.task.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.game.task.model.bean.GamePumpRecordBean;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:38 2020/3/13
 */
public interface GamePumpRecordDao extends BaseMapper<GamePumpRecordBean> {

    @Insert("<script> INSERT INTO t_dmg_game_pump_record "
            + "(game_id,game_name,file_id,file_name,record_date,pump,create_date,modify_date) "
            + "VALUES "
            + "<foreach collection = 'list' item='item' separator=',' > "
            + " (#{item.gameId},#{item.gameName},#{item.fileId},#{item.fileName},#{item.recordDate},#{item.pump},#{item.createDate},#{item.modifyDate}) "
            + "</foreach>"
            + "</script>")
    void insertBatch(@Param("list") List<GamePumpRecordBean> gamePumpRecordBeanList);
}

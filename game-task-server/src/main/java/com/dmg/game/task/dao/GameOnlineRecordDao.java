package com.dmg.game.task.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.game.task.model.bean.GameOnlineRecordBean;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:38 2020/3/13
 */
public interface GameOnlineRecordDao extends BaseMapper<GameOnlineRecordBean> {

    @Insert("<script> INSERT INTO t_dmg_game_online_record "
            + "(place,place_name,file_id,file_name,record_date,online_num,create_date,modify_date) "
            + "VALUES "
            + "<foreach collection = 'list' item='item' separator=',' > "
            + " (#{item.place},#{item.placeName},#{item.fileId},#{item.fileName},#{item.recordDate},#{item.onlineNum},#{item.createDate},#{item.modifyDate}) "
            + "</foreach>"
            + "</script>")
    void insertBatch(@Param("list") List<GameOnlineRecordBean> gameOnlineRecordBeanList);
}

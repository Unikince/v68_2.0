package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.AccountChangeLogBean;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:00 2019/12/30
 */
@Mapper
public interface AccountChangeLogDao extends BaseMapper<AccountChangeLogBean> {

    @Insert("<script> INSERT INTO t_dmg_account_change_log "
            + "(type,account,before_account,after_account,account_no,create_date,modify_date,user_id) "
            + "VALUES "
            + "<foreach collection = 'list' item='item' separator=',' > "
            + " (#{item.type},#{item.account},#{item.beforeAccount},#{item.afterAccount},#{item.accountNo},#{item.createDate},#{item.modifyDate},#{item.userId}) "
            + "</foreach>"
            + "</script>")
    void insertBatch(@Param("list") List<AccountChangeLogBean> accountChangeLogBeanList);

    @Select("SELECT * FROM  t_dmg_account_change_log  WHERE  YEARWEEK(date_format(create_date,'%Y-%m-%d')) = YEARWEEK(now()) and user_id=#{id}")
    List<AccountChangeLogBean> getWeekChange(@Param("id") Long id);

    @Select("select sum(account) from t_dmg_account_change_log where user_id = #{userId} and account<0")
    BigDecimal sumTurnover(Long userId);
}

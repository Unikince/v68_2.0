package com.dmg.lobbyserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.dto.LeaderboardDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户表
 *
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Mapper
//@CacheNamespace(implementation=(com.dmg.lobbyserver.config.j2cache.autoconfigure.J2CacheAdapter.class))
public interface UserDao extends BaseMapper<UserBean> {

    /**
     * @param
     * @return Long
     * @description: 查询最大userCode值
     * @author mice
     * @date 2019/6/18
     */
    @Select("select max(user_code) from t_dmg_user")
    Long selectMaxUserCode();

    /**
     * @Description 统计用户名是否唯一
     * @Author jock
     * @Date 2019/6/19 0019
     * @Version V1.0
     **/
    @Select("select count(1) from t_dmg_user where user_name=#{name}")
    Integer countName(String name);

    @Update("update t_dmg_user set new_user = 0")
    void setNewUserIsOld();

    /**
     * 查询是否有重复邮件
     */
    @Select("select count(1) from t_dmg_user where email=#{email}")
    Long getCountByEmail(String email);

    /**
     * 查询是否有重复手机号
     */
    @Select("select count(1) from t_dmg_user where phone=#{phone}")
    Long getCountByPhone(String phone);

    /**
     * @Author liubo
     * @Description //TODO 变更金币
     * @Date 11:25 2019/11/27
     **/
    @Update("update t_dmg_user set account_balance = (account_balance + #{changeAccount}) where id = #{id}")
    Integer updateAccountBalance(Long id, BigDecimal changeAccount);

    /**
     * @Author liubo
     * @Description //TODO 变更积分
     * @Date 11:25 2019/11/27
     **/
    @Update("update t_dmg_user set integral = (integral + #{integral}) where id = #{id}")
    Integer updateIntegral(Long id, Long integral);

    @Select("select count(*) from t_dmg_user where to_days(create_date) = to_days(now())")
    Long countTodayNewUser();

    @Select("select count(*) from t_dmg_user where to_days(login_date) = to_days(now())")
    Long countTodayLoginUser();

    /**
     * @description: 金币排行榜 前
     * @param
     * @return java.util.List<java.util.Map<java.lang.Object,java.lang.Object>>
     * @author mice
     * @date 2019/12/24
    */
    @Select("SELECT user_name, account_balance, @curRank := @curRank + 1 as rank from t_dmg_user t,(select @curRank := 0) r ORDER BY account_balance desc LIMIT 0,#{limit}")
    List<LeaderboardDTO> selectLeaderboard(int limit);
}

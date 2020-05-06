package com.dmg.gameconfigserver.dao.config;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.model.bean.config.UserControlListBean;
import com.dmg.gameconfigserver.model.dto.config.UserControlListDTO;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:07 2019/10/11
 */
@DS("v68")
public interface UserControlListDao extends BaseMapper<UserControlListBean> {
	@Select("<script>"
			+ "SELECT "
			+ "ucr.user_id AS userId," // 玩家ID
			+ "ucr.user_nickname AS userNickname," // 玩家昵称
			+ "IFNULL(SUM(spu.sum_bet), 0) AS totalBet," // 总下注
			+ "IFNULL(SUM(spu.sum_pay), 0) AS totalPayout," // 总赔付
			+ "IFNULL(SUM(spu.sum_win), 0) AS totalWinLose," // 总盈利
			+ "IFNULL(ucr.control_state, 10) AS controlState," // 控制状态
			+ "IFNULL(ucr.control_score, 0) AS controlScore," // 控制分数
			+ "IFNULL(ucr.control_model, 0) AS controlModel," // 控制模型
			+ "IFNULL(ucr.current_score, 0) AS currentScore," // 当前分数
			+ "IFNULL(ucr.operator, '') AS operator," // 控制人
			+ "IFNULL(ucr.operating_time, null) AS operatingTime," // 控制时间
			+ "IFNULL(ucr.operating_note, '') AS operatingNote " // 控制备注
			+ "FROM "
			+ "t_user_control_list ucr "
			+ "LEFT JOIN "
			+ "statement_player_user spu "
			+ "ON "
			+ "ucr.user_id = spu.user_id "
			+ "<where>"
	        + "<if test='userId !=null and userId != \"\"'>"
	        + "AND ucr.user_id = #{userId}\n"
	        +"</if>"
	        + "<if test='userNickname != null and userNickname != \"\"'>"
	        + "AND ucr.user_nickname=#{userNickname}\n"
	        +"</if>"
	       	+ "<if test='startNum != null and startNum != \"\"'>"
	       	+ " AND ucr.control_state between #{startNum} and #{endNum} \n"
	       	+"</if>"
	        +"</where>"
			+ "GROUP BY "
			+ "ucr.id "
			+ " </script>")
	public IPage<UserControlListDTO> getUserList(Page page,@Param("userId") Long userId, @Param("userNickname") String userNickname, @Param("startNum")int startNum , @Param("endNum")int endNum);
	//INSERT INTO t_user_control_list(user_id,user_nickname) SELECT id,user_name FROM t_dmg_user tdu WHERE NOT EXISTS(SELECT  user_id FROM t_user_control_list tucl WHERE tucl.user_id = tdu.id)
	@Insert("INSERT INTO "
			+ "t_user_control_list(user_id,user_nickname) "
			+ "SELECT "
			+ "user_code,user_name "
			+ "FROM "
			+ "t_dmg_user tdu "
			+ "WHERE "
			+ "NOT EXISTS"
			+ "(SELECT  user_id FROM t_user_control_list tucl WHERE tucl.user_id = tdu.user_code)")
	void InsertNewUser();
	@Update("UPDATE t_user_control_list SET control_state = #{state} WHERE user_id = #{userId}")
	void updateState(@Param("userId") long userId,@Param("state") int state);
}

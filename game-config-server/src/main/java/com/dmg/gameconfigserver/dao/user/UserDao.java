package com.dmg.gameconfigserver.dao.user;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.model.bean.user.UserBean;
import com.dmg.gameconfigserver.model.vo.user.UserListVO;
import com.dmg.gameconfigserver.model.vo.user.UserVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 用户表
 *
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@DS("v68")
public interface UserDao extends BaseMapper<UserBean> {

    IPage<UserListVO> getUserPage(Page page, @Param("userId") Long userId,
                                  @Param("userName") String userName,
                                  @Param("phone") String phone);

    UserVO getUserInfoById(@Param("id") Integer id);

    Long countTodayLoginUser();

    Long countTodayRegister();

    @Update("update t_dmg_user set good_number = #{goodNum} where id = #{userId}")
    void bindGoodNum(@Param("userId") Long userId,@Param("goodNum")Long goodNum);

    @Update("update t_dmg_user set good_number = null where id = #{userId}")
    void untieGoodNum(@Param("userId") Long userId);

}

package com.dmg.gameconfigserver.dao.user;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.model.bean.user.UserBean;
import com.dmg.gameconfigserver.model.bean.user.UserEmailBean;
import com.dmg.gameconfigserver.model.vo.user.UserListVO;
import com.dmg.gameconfigserver.model.vo.user.UserVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:29 2020/3/23
 **/
@DS("v68")
public interface UserEmailDao extends BaseMapper<UserEmailBean> {

    @Insert("<script> INSERT INTO t_dmg_user_email "
            + "(email_name,email_content,expire_date,send_date,item_type,item_num,user_id,email_id) "
            + "VALUES "
            + "<foreach collection = 'list' item='item' separator=',' > "
            + " (#{item.emailName},#{item.emailContent},#{item.expireDate},#{item.sendDate},#{item.itemType},#{item.itemNum},#{item.userId},#{item.emailId}) "
            + "</foreach>"
            + "</script>")
    void insertBatch(@Param("list") List<UserEmailBean> userEmailBeanList);

}

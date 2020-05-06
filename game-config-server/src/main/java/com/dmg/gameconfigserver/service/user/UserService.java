package com.dmg.gameconfigserver.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.bean.user.UserBean;
import com.dmg.gameconfigserver.model.dto.user.UserDTO;
import com.dmg.gameconfigserver.model.vo.user.UserDetailVO;
import com.dmg.gameconfigserver.model.vo.user.UserListVO;
import com.dmg.gameconfigserver.model.vo.user.UserVO;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/20 14:31
 * @Version V1.0
 **/
public interface UserService {

    /**
     * @Author liubo
     * @Description //TODO  查询用户列表
     * @Date 17:11 2019/11/20
     **/
    IPage<UserListVO> getUserPage(UserDTO userDTO);

    /**
     * @Author liubo
     * @Description //TODO 查询用户信息
     * @Date 18:40 2019/11/20
     **/
    UserDetailVO getUserInfo(Integer id);

    /**
     * @Author liubo
     * @Description //TODO 修改
     * @Date 18:49 2019/11/20
     **/
    void update(UserDetailVO userDetailVO);

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 14:47 2020/3/17
     **/
    Long getTodayLoginCount();

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 14:47 2020/3/17
     **/
    Long getTodayRegisterCount();

}
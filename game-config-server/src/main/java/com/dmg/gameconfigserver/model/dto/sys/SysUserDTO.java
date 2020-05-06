package com.dmg.gameconfigserver.model.dto.sys;

import com.dmg.gameconfigserver.annotation.ColumnName;
import com.dmg.gameconfigserver.model.vo.group.LoginValid;
import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 18:02 2019/11/6
 */
@Data
@ColumnName(name = "用户")
public class SysUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull(message = "id不能为空", groups = UpdateValid.class)
    private Long id;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * 修改时间
     */
    private Date modifyDate;
    /**
     * 修改人
     */
    private Long modifyUser;
    /**
     * 顺序
     */
    private Long sort;
    /**
     * userName
     */
    @ColumnName(name = "userName")
    @NotNull(message = "userName不能为空", groups = {SaveValid.class, LoginValid.class})
    private String userName;
    /**
     * password
     */
    @ColumnName(name = "密码")
    @NotNull(message = "密码不能为空", groups = {SaveValid.class, LoginValid.class})
    private String password;
    /**
     * 昵称
     */
    @ColumnName(name = "昵称")
    @NotNull(message = "昵称不能为空", groups = SaveValid.class)
    private String nickName;
    /**
     * 状态
     */
    @ColumnName(name = "状态")
    @NotNull(message = "状态不能为空", groups = SaveValid.class)
    private Boolean status;
    /**
     * 角色id
     */
    @ColumnName(name = "角色编号")
    @NotNull(message = "角色不能为空", groups = SaveValid.class)
    private Long roleId;
}

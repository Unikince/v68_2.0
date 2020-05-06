package com.dmg.gameconfigserver.model.dto.sys;

import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:42 2019/12/24
 */
@Data
public class SysRoleDTO {
    @NotNull(message = "id不能为空", groups = UpdateValid.class)
    private Long id;
    /**
     * 角色名称
     */
    @NotNull(message = "角色名称不能为空", groups = SaveValid.class)
    private String name;
    /**
     * 角色编码
     */
    private String role;
    /**
     * 描述
     */
    @NotNull(message = "描述不能为空", groups = SaveValid.class)
    private String description;
    /**
     * 是否启用
     */
    @NotNull(message = "是否启用不能为空", groups = SaveValid.class)
    private Boolean status;
    /**
     * 资源id
     */
    @NotNull(message = "资源不能为空", groups = SaveValid.class)
    private List<Long> resourceId;

}

package com.dmg.gameconfigserver.model.dto.sys;

import com.dmg.gameconfigserver.annotation.ColumnName;
import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:00 2019/11/6
 */
@Data
@ColumnName(name = "地址")
public class SysWhiteDTO {
    @NotNull(message = "id不能为空", groups = UpdateValid.class)
    private Long id;
    /**
     * 是否启用
     */
    @ColumnName(name = "是否启用")
    @NotNull(message = "是否启用不能为空", groups = SaveValid.class)
    private Boolean status;
    /**
     * ip
     */
    @ColumnName(name = "ip")
    @NotNull(message = "IP不能为空", groups = SaveValid.class)
    private String ip;

}

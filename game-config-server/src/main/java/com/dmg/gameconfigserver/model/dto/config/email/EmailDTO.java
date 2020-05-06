package com.dmg.gameconfigserver.model.dto.config.email;

import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:49 2020/3/19
 */
@Data
public class EmailDTO {
    @NotNull(message = "id不能为空", groups = UpdateValid.class)
    private Long id;

    /**
     * userIds
     */
    private String userIds;
    /**
     * 邮件名称
     */
    @NotNull(message = "邮件名称不能为空", groups = SaveValid.class)
    private String emailName;
    /**
     * 邮件内容信息
     */
    private String emailContent;
    /**
     * 过期时间
     */
    @NotNull(message = "过期时间不能为空", groups = SaveValid.class)
    private Date expireDate;
    /**
     * 发送时间
     */
    @NotNull(message = "发送时间不能为空", groups = SaveValid.class)
    private Date sendDate;
    /**
     * 物品类型
     */
    private Integer itemType;
    /**
     * 物品数量
     */
    private String itemNum;
}

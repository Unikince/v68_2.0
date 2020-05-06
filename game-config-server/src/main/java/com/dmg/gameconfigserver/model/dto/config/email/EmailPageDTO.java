package com.dmg.gameconfigserver.model.dto.config.email;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;
import lombok.Data;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:38 2020/3/19
 */
@Data
public class EmailPageDTO extends PageReqDTO {

    private Long userId;

    private Long sysUserId;
}

package com.dmg.lobbyserver.model.dto.pay;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:15 2020/2/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VnnPayDataDTO {

    //app_id
    @JSONField(name="app_id")
    private String app_id;

    private String content;

    private String timestamp;

    private String rand;

    private String sig;

    private String msg;

    private String ret;
}

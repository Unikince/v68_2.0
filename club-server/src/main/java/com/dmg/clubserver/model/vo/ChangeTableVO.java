package com.dmg.clubserver.model.vo;

import lombok.Data;

@Data
public class ChangeTableVO {
    private Integer roleId;
    private Integer clubId;
    private Integer roomId;
    private String tableMsg;
}

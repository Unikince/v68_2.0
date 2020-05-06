package com.dmg.clubserver.model.request;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/25 17:13
 * @Version V1.0
 **/
@Data
public class ClubLoginParam extends BaseRequestParam{
    private String roleId;
}
package com.dmg.zhajinhuaserver.model.dto;

import com.dmg.server.common.model.dto.UserResultDTO;
import com.dmg.zhajinhuaserver.model.bean.Balance;
import com.dmg.zhajinhuaserver.model.bean.Poker;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:17 2019/11/20
 */
@Data
@Builder
public class RecordDataDTO {

    private List<UserResultDTO<Poker>> userResult;
    private List<Balance> balanceList;
}

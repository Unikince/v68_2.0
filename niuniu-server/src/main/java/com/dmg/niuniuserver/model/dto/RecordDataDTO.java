package com.dmg.niuniuserver.model.dto;

import com.dmg.niuniuserver.model.bean.Balance;
import com.dmg.niuniuserver.model.bean.Poker;
import com.dmg.server.common.model.dto.UserResultDTO;
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

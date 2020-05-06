package com.dmg.gameconfigserver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 14:44
 * @Version V1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameInfoDTO {

    private Integer gameId;

    private String gameName;

    private Integer gameType;
}
package com.dmg.lobbyserver.service;

import com.dmg.lobbyserver.model.dto.GiftDataDTO;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/25 17:51
 * @Version V1.0
 **/
public interface GiftDataService {

    void sendGiftData(Long userId, List<GiftDataDTO> giftDataDTOList);
}
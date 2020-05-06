package com.dmg.lobbyserver.service.record;

import com.dmg.common.core.web.Result;
import com.dmg.lobbyserver.model.dto.UserGameRecordDTO;
import com.dmg.lobbyserver.model.vo.GameRecordVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:07 2019/11/29
 */
@FeignClient(value = "game-record-server", path = "/game-record-api/gameRecord/")
public interface GameRecordService {

    /**
     * @Author liubo
     * @Description //TODO 用户查询战绩信息
     * @Date 11:15 2019/11/29
     **/
    @PostMapping("getInfoList")
    Result<List<GameRecordVO>> getInfoList(@RequestBody UserGameRecordDTO userGameRecordDTO);
}

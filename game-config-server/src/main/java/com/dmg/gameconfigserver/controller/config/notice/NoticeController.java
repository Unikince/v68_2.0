package com.dmg.gameconfigserver.controller.config.notice;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.dto.PageReqDTO;
import com.dmg.gameconfigserver.model.dto.config.notice.NoticeDTO;
import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import com.dmg.gameconfigserver.service.config.GameInfoService;
import com.dmg.gameconfigserver.service.config.NoticeSerice;
import com.dmg.server.common.enums.PlaceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 19:15 2020/1/7
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "config/notice")
public class NoticeController extends BaseController {

    @Autowired
    private NoticeSerice noticeSerice;

    @Autowired
    private GameInfoService gameInfoService;

    @PostMapping("/getList")
    public Result getList(@RequestBody PageReqDTO pageReqDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        return Result.success(noticeSerice.getNoticeList(pageReqDTO));
    }

    @PostMapping("/saveOne")
    public Result register(@RequestBody @Validated({SaveValid.class}) NoticeDTO noticeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        Boolean result = noticeSerice.insert(noticeDTO, getUserId());
        if (!result) {
            return Result.error(ResultEnum.USER_EXIST.getCode().toString(), ResultEnum.USER_EXIST.getMsg());
        }
        return Result.success();
    }

    @PostMapping("/updateOne")
    public Result update(@RequestBody @Validated({UpdateValid.class}) NoticeDTO noticeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), bindingResult.getFieldError().getDefaultMessage());
        }
        Boolean result = noticeSerice.update(noticeDTO, getUserId());
        if (!result) {
            return Result.error(ResultEnum.USER_NO_EXIST.getCode().toString(), ResultEnum.USER_NO_EXIST.getMsg());
        }
        return Result.success();
    }

    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        if (id == null) {
            return Result.error(ResultEnum.PARAM_ERROR.getCode().toString(), "id cannot be empty");
        }
        noticeSerice.deleteById(id);
        return Result.success();
    }

    @GetMapping("/getPositionAll")
    public Result getPositionList() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PlaceEnum position : PlaceEnum.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", position.getCode());
            map.put("name", position.getMsg());
            list.add(map);
        }
        gameInfoService.getGameOpen().forEach(gameInfoVO -> {
            Map<String, Object> mapGame = new HashMap<>();
            mapGame.put("id", gameInfoVO.getGameId());
            mapGame.put("name", gameInfoVO.getGameName());
            list.add(mapGame);
        });
        return Result.success(list);
    }
}

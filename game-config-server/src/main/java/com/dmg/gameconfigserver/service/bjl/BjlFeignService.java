package com.dmg.gameconfigserver.service.bjl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.gameconfigserver.dao.bjl.BjlTableDao;
import com.dmg.gameconfigserver.model.bean.bjl.BjlTableBean;
import com.dmg.gameconfigserver.model.vo.config.rate.GameExchangeRateVO;
import com.dmg.gameconfigserver.service.config.GameExchangeRateService;
import com.dmg.gameconfigserverapi.bjl.dto.BjlTableDTO;

import cn.hutool.core.bean.BeanUtil;

/**
 * 百家乐配置获取服务
 */
@Service
public class BjlFeignService {
    @Autowired
    private BjlTableDao bjlTableDao;

    @Value("${exchange-rate-code}")
    private String exchangeRateCode;
    @Autowired
    private GameExchangeRateService gameExchangeRateService;

    /** 房间配置 */
    public List<BjlTableDTO> getBjlTable() {
        GameExchangeRateVO gameExchangeRateVo = this.gameExchangeRateService.getExchangeRateByCode(this.exchangeRateCode);
        List<BjlTableDTO> dtos = new ArrayList<>();
        List<BjlTableBean> beans = this.bjlTableDao.selectList(new LambdaQueryWrapper<>());
        for (BjlTableBean bean : beans) {
            BjlTableDTO dto = new BjlTableDTO();
            BeanUtil.copyProperties(bean, dto);
            dtos.add(dto);

            if (gameExchangeRateVo != null) {
                // 筹码
                List<Long> chipList = JSON.parseArray(bean.getChipJson(), Long.class);
                for (int i = 0; i < chipList.size(); i++) {
                    Long chip = chipList.get(i);
                    chip = gameExchangeRateVo.getExchangeRate().multiply(new BigDecimal(chip)).longValue();
                    chipList.set(i, chip);
                }
                dto.setChipJson(JSON.toJSONString(chipList));

                // 玩家最大下注
                Long playerMaxBet = bean.getPlayerMaxBet();
                playerMaxBet = gameExchangeRateVo.getExchangeRate().multiply(new BigDecimal(playerMaxBet)).longValue();
                dto.setPlayerMaxBet(playerMaxBet);

                // 桌子最大下注
                Long tableMaxBet = bean.getTableMaxBet();
                tableMaxBet = gameExchangeRateVo.getExchangeRate().multiply(new BigDecimal(tableMaxBet)).longValue();
                dto.setTableMaxBet(tableMaxBet);

                // 进场金币下限
                Long robotEnterGoldLower = bean.getRobotEnterGoldLower();
                robotEnterGoldLower = gameExchangeRateVo.getExchangeRate().multiply(new BigDecimal(robotEnterGoldLower)).longValue();
                dto.setRobotEnterGoldLower(robotEnterGoldLower);

                // 进场金币上限
                Long robotEnterGoldUpper = bean.getRobotEnterGoldUpper();
                robotEnterGoldUpper = gameExchangeRateVo.getExchangeRate().multiply(new BigDecimal(robotEnterGoldUpper)).longValue();
                dto.setRobotEnterGoldUpper(robotEnterGoldUpper);
            }
        }
        return dtos;
    }
}
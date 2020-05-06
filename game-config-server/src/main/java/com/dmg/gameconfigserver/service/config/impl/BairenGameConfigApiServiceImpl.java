package com.dmg.gameconfigserver.service.config.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.web.BusinessException;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.dao.config.FileBaseConfigDao;
import com.dmg.gameconfigserver.dao.config.bairen.BairenControlConfigDao;
import com.dmg.gameconfigserver.dao.config.bairen.BairenFileConfigDao;
import com.dmg.gameconfigserver.dao.config.bairen.BairenWaterPoolConfigDao;
import com.dmg.gameconfigserver.model.bean.config.FileBaseConfigBean;
import com.dmg.gameconfigserver.model.bean.config.bairen.BairenControlConfigBean;
import com.dmg.gameconfigserver.model.bean.config.bairen.BairenFileConfigBean;
import com.dmg.gameconfigserver.model.bean.config.bairen.BairenWaterPoolConfigBean;
import com.dmg.gameconfigserver.model.dto.config.BairenControlConfigDTO;
import com.dmg.gameconfigserver.model.dto.config.BairenFileConfigDTO;
import com.dmg.gameconfigserver.model.dto.config.BairenGameConfigDTO;
import com.dmg.gameconfigserver.model.vo.config.rate.GameExchangeRateVO;
import com.dmg.gameconfigserver.service.config.BairenGameConfigApiService;
import com.dmg.gameconfigserver.service.config.GameExchangeRateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;


@Service("bairenGameConfigApiService")
public class BairenGameConfigApiServiceImpl implements BairenGameConfigApiService {

    @Autowired
    private BairenControlConfigDao bairenControlConfigDao;

    @Autowired
    private BairenWaterPoolConfigDao bairenWaterPoolConfigDao;

    @Autowired
    private FileBaseConfigDao fileBaseConfigDao;

    @Autowired
    private BairenFileConfigDao bairenFileConfigDao;

    @Autowired
    private GameExchangeRateService gameExchangeRateService;

    @Value("${exchange-rate-code}")
    private String exchangeRateCode;


    public List<BairenGameConfigDTO> getGameConfig(int gameId) {
        List<BairenGameConfigDTO> bairenGameConfigDTOS = new LinkedList<>();
        List<FileBaseConfigBean> fileBaseConfigBeans = fileBaseConfigDao.selectList(new LambdaQueryWrapper<FileBaseConfigBean>()
                .eq(FileBaseConfigBean::getGameId, gameId)
                .eq(FileBaseConfigBean::getOpenStatus, 1));
        if (CollectionUtil.isEmpty(fileBaseConfigBeans)) {
            return bairenGameConfigDTOS;
        }
        GameExchangeRateVO gameExchangeRateVO = gameExchangeRateService.getExchangeRateByCode(exchangeRateCode);

        for (FileBaseConfigBean fileBaseConfigBean : fileBaseConfigBeans) {
            BairenGameConfigDTO bairenGameConfigDTO = new BairenGameConfigDTO();
            bairenGameConfigDTO.setFileId(fileBaseConfigBean.getFileId());

            //场次配置信息
            BairenFileConfigDTO bairenFileConfigDTO = new BairenFileConfigDTO();
            BeanUtils.copyProperties(fileBaseConfigBean, bairenFileConfigDTO);

            BairenFileConfigBean bairenFileConfigBean = bairenFileConfigDao.selectOne(new LambdaQueryWrapper<BairenFileConfigBean>()
                    .eq(BairenFileConfigBean::getFileBaseConfigId, fileBaseConfigBean.getId()));

            BeanUtils.copyProperties(bairenFileConfigBean, bairenFileConfigDTO);
            if (gameExchangeRateVO != null) {
                bairenGameConfigDTO.setExchangeRate(gameExchangeRateVO.getExchangeRate());
                if (bairenFileConfigDTO.getFileLimit() != null) {
                    bairenFileConfigDTO.setFileLimit(BigDecimal.valueOf(bairenFileConfigDTO.getFileLimit()).multiply(gameExchangeRateVO.getExchangeRate()).intValue());
                }
                if (bairenFileConfigDTO.getRoomBetLowLimit() != null) {
                    bairenFileConfigDTO.setRoomBetLowLimit(BigDecimal.valueOf(bairenFileConfigDTO.getRoomBetLowLimit()).multiply(gameExchangeRateVO.getExchangeRate()).intValue());
                }
                if (bairenFileConfigDTO.getRedValue() != null) {
                    bairenFileConfigDTO.setRedValue(BigDecimal.valueOf(bairenFileConfigDTO.getRedValue()).multiply(gameExchangeRateVO.getExchangeRate()).intValue());
                }
                if (bairenFileConfigDTO.getAreaBetLowLimit() != null) {
                    bairenFileConfigDTO.setAreaBetLowLimit(BigDecimal.valueOf(bairenFileConfigDTO.getAreaBetLowLimit()).multiply(gameExchangeRateVO.getExchangeRate()).longValue());
                }
                if (bairenFileConfigDTO.getAreaBetUpLimit() != null) {
                    bairenFileConfigDTO.setAreaBetUpLimit(BigDecimal.valueOf(bairenFileConfigDTO.getAreaBetUpLimit()).multiply(gameExchangeRateVO.getExchangeRate()).longValue());
                }
                if (bairenFileConfigDTO.getApplyBankerLimit() != null) {
                    bairenFileConfigDTO.setApplyBankerLimit(BigDecimal.valueOf(bairenFileConfigDTO.getApplyBankerLimit()).multiply(gameExchangeRateVO.getExchangeRate()).intValue());
                }
                if (bairenFileConfigDTO.getBankerGoldLowLimit() != null) {
                    bairenFileConfigDTO.setBankerGoldLowLimit(BigDecimal.valueOf(bairenFileConfigDTO.getBankerGoldLowLimit()).multiply(gameExchangeRateVO.getExchangeRate()).intValue());
                }
                if (StringUtils.isNotBlank(bairenFileConfigDTO.getBetChips())) {
                    String betChips = "";
                    for (String str : bairenFileConfigDTO.getBetChips().split(",")) {
                        betChips = betChips.concat(String.valueOf(new BigDecimal(str).multiply(gameExchangeRateVO.getExchangeRate()).intValue())).concat(",");
                    }
                    bairenFileConfigDTO.setBetChips(betChips.substring(0, betChips.length() - 1));
                }
            }

            bairenGameConfigDTO.setBairenFileConfigDTO(bairenFileConfigDTO);

            if (bairenFileConfigBean == null) {
                throw new BusinessException(ResultEnum.CONFIG_ERROR.getCode() + "", ResultEnum.CONFIG_ERROR.getMsg());
            }
            //控制配置信息
            BairenControlConfigDTO bairenControlConfigDTO = new BairenControlConfigDTO();
            BairenControlConfigBean bairenControlConfigBean = bairenControlConfigDao.selectOne(new LambdaQueryWrapper<BairenControlConfigBean>()
                    .eq(BairenControlConfigBean::getFileBaseConfigId, fileBaseConfigBean.getId()));
            if (bairenControlConfigBean == null) {
                throw new BusinessException(ResultEnum.CONFIG_ERROR.getCode() + "", ResultEnum.CONFIG_ERROR.getMsg());
            }
            BeanUtils.copyProperties(bairenControlConfigBean, bairenControlConfigDTO);
            bairenControlConfigDTO.setCardTypeMultiple(JSON.parseObject(bairenControlConfigBean.getCardTypeMultiple()));
            if (gameExchangeRateVO != null) {
                if (bairenControlConfigDTO.getMaxPayout() != null) {
                    bairenControlConfigDTO.setMaxPayout(BigDecimal.valueOf(bairenControlConfigDTO.getMaxPayout()).multiply(gameExchangeRateVO.getExchangeRate()).intValue());
                }
                if (bairenControlConfigDTO.getMaxPayoutReferenceValue() != null) {
                    bairenControlConfigDTO.setMaxPayoutReferenceValue(BigDecimal.valueOf(bairenControlConfigDTO.getMaxPayoutReferenceValue()).multiply(gameExchangeRateVO.getExchangeRate()).intValue());
                }
            }
            bairenGameConfigDTO.setBairenControlConfigDTO(bairenControlConfigDTO);

            //水池配置信息
            List<BairenWaterPoolConfigBean> bairenWaterPoolConfigBeans = bairenWaterPoolConfigDao.selectList(new LambdaQueryWrapper<BairenWaterPoolConfigBean>().eq(BairenWaterPoolConfigBean::getFileBaseConfigId, fileBaseConfigBean.getId()).orderByAsc(BairenWaterPoolConfigBean::getWaterOrder));
            List<BairenGameConfigDTO.WaterPoolConfigDTO> waterPoolConfigDTOS = new LinkedList<>();
            for (BairenWaterPoolConfigBean bairenWaterPoolConfigBean : bairenWaterPoolConfigBeans) {
                BairenGameConfigDTO.WaterPoolConfigDTO waterPoolConfigDTO = new BairenGameConfigDTO.WaterPoolConfigDTO();
                BeanUtils.copyProperties(bairenWaterPoolConfigBean, waterPoolConfigDTO);
                if (gameExchangeRateVO != null) {
                    if (waterPoolConfigDTO.getWaterHigh() != null) {
                        waterPoolConfigDTO.setWaterHigh(BigDecimal.valueOf(waterPoolConfigDTO.getWaterHigh()).multiply(gameExchangeRateVO.getExchangeRate()).longValue());
                    }
                    if (waterPoolConfigDTO.getWaterLow() != null) {
                        waterPoolConfigDTO.setWaterLow(BigDecimal.valueOf(waterPoolConfigDTO.getWaterLow()).multiply(gameExchangeRateVO.getExchangeRate()).longValue());
                    }
                }
                waterPoolConfigDTOS.add(waterPoolConfigDTO);
            }
            bairenGameConfigDTO.setWaterPoolConfigDTOS(waterPoolConfigDTOS);

            bairenGameConfigDTOS.add(bairenGameConfigDTO);
        }
        return bairenGameConfigDTOS;
    }

}

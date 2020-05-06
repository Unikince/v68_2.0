package com.dmg.gameconfigserver.service.lhj.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.gameconfigserver.dao.lhj.LhjGameConfigDao;
import com.dmg.gameconfigserver.model.dto.lhj.LhjBonusConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjFieldConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjGameConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjGameListDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjInventoryConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjInventoryControlDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjOddsPoolConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjPayLimitConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjPlayerPayLimitConfigDTO;
import com.dmg.gameconfigserver.model.vo.config.rate.GameExchangeRateVO;
import com.dmg.gameconfigserver.service.config.GameExchangeRateService;
import com.dmg.gameconfigserver.service.lhj.LhjGameConfigApiService;


@Service("lhjGameConfigApiService")
public class LhjGameConfigApiServiceImpl implements LhjGameConfigApiService {

    @Autowired
    @Qualifier("lhjGameConfigDaoImp")
    private LhjGameConfigDao lhjGameConfigDao;
    
    @Autowired
    private GameExchangeRateService gameExchangeRateService;

    @Value("${exchange-rate-code}")
    private String exchangeRateCode;

    @Override
    public LhjGameConfigDTO getGameConfig(int gameId) {
        LhjGameConfigDTO lhjGameConfigDTO = new LhjGameConfigDTO();
        LhjFieldConfigDTO lhjFieldConfig = lhjGameConfigDao.querySingleGameInfo(gameId);
        GameExchangeRateVO gameExchangeRateVO = gameExchangeRateService.getExchangeRateByCode(exchangeRateCode);
        lhjGameConfigDTO.setGameId(lhjFieldConfig.getGameId());
    	 //场次配置信息
        if (gameExchangeRateVO != null) {
        	lhjGameConfigDTO.setExchangeRate(gameExchangeRateVO.getExchangeRate().doubleValue());
        	if (lhjFieldConfig.getInAmount() != 0d) {
        		lhjFieldConfig.setInAmount(BigDecimal.valueOf(lhjFieldConfig.getInAmount()).multiply(gameExchangeRateVO.getExchangeRate()).doubleValue());
			}
        	if (!StringUtils.isEmpty(lhjFieldConfig.getBetList())) {
        		List<Double> betList = new ArrayList<>();
        		String betStr = lhjFieldConfig.getBetList();
        		if (betStr.startsWith("\"")) {
        			betStr = betStr.substring(1, betStr.length());
        			if (betStr.endsWith("\"")) {
        				betStr = betStr.substring(0, betStr.length() - 1);
        			}
        		} 
        		betList = JSONObject.parseArray(betStr, Double.class);
        		betList.forEach(bet -> BigDecimal.valueOf(bet).multiply(gameExchangeRateVO.getExchangeRate()).doubleValue());
			}
        	if (lhjFieldConfig.getCheckAmount() != 0d) {
        		lhjFieldConfig.setCheckAmount(BigDecimal.valueOf(lhjFieldConfig.getCheckAmount()).multiply(gameExchangeRateVO.getExchangeRate()).doubleValue());
			}
       }
        lhjGameConfigDTO.setLhjFieldConfig(lhjFieldConfig);
        // 库存配置
        List<LhjInventoryConfigDTO> lhjInventoryConfigList = lhjGameConfigDao.queryInventoryInfo(gameId);
        if (lhjInventoryConfigList != null && lhjInventoryConfigList.size() > 0) {
        	lhjGameConfigDTO.setLhjInventoryConfig(lhjInventoryConfigList);
		}
        // 库存控制配置
        LhjInventoryControlDTO lhjInventoryControlDTO = lhjGameConfigDao.queryInventoryControl(gameId);
        lhjGameConfigDTO.setLhjInventoryControl(lhjInventoryControlDTO);
        // 彩金配置
        List<LhjBonusConfigDTO> gamePoolConfigList = lhjGameConfigDao.queryGamePoolConfig(gameId);
        if (gamePoolConfigList != null && gamePoolConfigList.size() > 0) {
        	for (LhjBonusConfigDTO lhjBonusConfig : gamePoolConfigList) {
        		lhjBonusConfig.setInitAmount(BigDecimal.valueOf(lhjBonusConfig.getInitAmount()).multiply(gameExchangeRateVO.getExchangeRate()).doubleValue());
        		lhjBonusConfig.setLowBet(BigDecimal.valueOf(lhjBonusConfig.getLowBet()).multiply(gameExchangeRateVO.getExchangeRate()).doubleValue());
        		lhjBonusConfig.setPoolOpenLow(BigDecimal.valueOf(lhjBonusConfig.getPoolOpenLow()).multiply(gameExchangeRateVO.getExchangeRate()).doubleValue());
			}
        	lhjGameConfigDTO.setLhjBonusConfigList(gamePoolConfigList);
		}
        
        // 赔率奖池
        LhjOddsPoolConfigDTO lhjOddsPoolInfo = lhjGameConfigDao.queryOddsPoolInfo();
        if (lhjOddsPoolInfo != null) {
        	lhjGameConfigDTO.setLhjOddsPoolConfig(lhjOddsPoolInfo);
		}
        
        // 派奖条件
        List<LhjPayLimitConfigDTO> payLimitInfoList = lhjGameConfigDao.queryPayLimitInfo();
        if (payLimitInfoList != null && payLimitInfoList.size() > 0) {
        	for (LhjPayLimitConfigDTO lhjPayLimitConfig : payLimitInfoList) {
        		lhjPayLimitConfig.setPayLowLimit(BigDecimal.valueOf(lhjPayLimitConfig.getPayLowLimit()).multiply(gameExchangeRateVO.getExchangeRate()).doubleValue());
			}
        	lhjGameConfigDTO.setLhjPayLimitConfigList(payLimitInfoList);
		}
        // 玩家派奖条件
        List<LhjPlayerPayLimitConfigDTO> payPlayerLimitInfoList = lhjGameConfigDao.queryPayPlayerLimitInfo();
        if (payPlayerLimitInfoList != null && payPlayerLimitInfoList.size() > 0) {
			for (LhjPlayerPayLimitConfigDTO lhjPlayerPayLimitConfig : payPlayerLimitInfoList) {
				lhjPlayerPayLimitConfig.setDayLowLimit(BigDecimal.valueOf(lhjPlayerPayLimitConfig.getDayLowLimit()).multiply(gameExchangeRateVO.getExchangeRate()).doubleValue());
				lhjPlayerPayLimitConfig.setDayWaterLow(BigDecimal.valueOf(lhjPlayerPayLimitConfig.getDayWaterLow()).multiply(gameExchangeRateVO.getExchangeRate()).doubleValue());
				lhjPlayerPayLimitConfig.setTotalLowLimit(BigDecimal.valueOf(lhjPlayerPayLimitConfig.getTotalLowLimit()).multiply(gameExchangeRateVO.getExchangeRate()).doubleValue());
				lhjPlayerPayLimitConfig.setTotalWaterLow(BigDecimal.valueOf(lhjPlayerPayLimitConfig.getTotalWaterLow()).multiply(gameExchangeRateVO.getExchangeRate()).doubleValue());
			}
		}
        lhjGameConfigDTO.setLhjPlayerPayLimitConfigList(payPlayerLimitInfoList);
        return lhjGameConfigDTO;
    }

	@Override
	public List<LhjGameListDTO> getGameList() {
		List<LhjGameListDTO> soltList = lhjGameConfigDao.getSoltList();
		return soltList;
	}

}

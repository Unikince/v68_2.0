package com.dmg.gameconfigserver.dao.finance;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.gameconfigserver.model.bean.finance.PayChannelBean;

/**
 * 支付渠道
 */
@DS("v68")
public interface PayChannelDao extends BaseMapper<PayChannelBean> {
}

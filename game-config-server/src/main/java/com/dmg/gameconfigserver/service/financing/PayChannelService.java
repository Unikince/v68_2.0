package com.dmg.gameconfigserver.service.financing;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.bean.finance.PayChannelBean;
import com.dmg.gameconfigserver.model.vo.finance.PayChannelListRecv;

/**
 * 支付渠道
 */
public interface PayChannelService {
    /** 所有渠道，用于下拉框选择 */
    String ALL_CHANNEL = "所有渠道";

    /** 新增 */
    void saveOne(PayChannelBean bean);

    /** 删除 */
    void delete(Long id);

    /** 更新 */
    void updateOne(PayChannelBean bean);

    /** 获取 */
    PayChannelBean get(Long id);

    /** 获取列表 */
    IPage<PayChannelBean> getList(PayChannelListRecv reqVo);

    /** 所有渠道，用于下拉框选择 */
    List<String> channelGroup();
}

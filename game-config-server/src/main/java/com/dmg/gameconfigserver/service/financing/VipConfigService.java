package com.dmg.gameconfigserver.service.financing;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.bean.finance.VipConfigBean;
import com.dmg.gameconfigserver.model.vo.finance.VipListRecv;

/**
 * VIP配置
 */
public interface VipConfigService {
    /** 新增 */
    void saveOne(VipConfigBean bean, Long loginId);

    /** 删除 */
    void delete(Long id);

    /** 更新 */
    void updateOne(VipConfigBean bean, Long loginId);

    /** 获取 */
    VipConfigBean get(Long id);

    /** 获取列表 */
    IPage<VipConfigBean> getList(VipListRecv reqVo);

    /** VIP数量 */
    int vipSum();

}

package com.dmg.agentserver.business.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.agentserviceapi.business.transferaccountrecord.model.pojo.TransferAccountRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 转账纪录
 */
@Data
@TableName("a_transfer_account_record")
@EqualsAndHashCode(callSuper = false)
public class TransferAccountRecordPo extends TransferAccountRecord {
}

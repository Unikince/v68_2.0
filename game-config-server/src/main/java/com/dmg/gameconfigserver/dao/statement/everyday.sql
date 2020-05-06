/** 创建表 */
DROP TABLE IF EXISTS `statement_everyday`;
CREATE TABLE `statement_everyday` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '逻辑键',
  `day_str` date DEFAULT NULL COMMENT '时间',
  `new_player_num` int(11) DEFAULT '0' COMMENT '新增人数',
  `active_player_num` int(11) DEFAULT '0' COMMENT '活跃人数',
  `sum_win` decimal(20,2) DEFAULT '0.00' COMMENT '总盈利',
  `sum_bet` decimal(20,2) DEFAULT '0.00' COMMENT '总下注',
  `sum_pay` decimal(20,2) DEFAULT '0.00' COMMENT '总赔付',
  `charge` decimal(20,2) DEFAULT '0.00' COMMENT '服务费',
  `sum_recharge` decimal(20,2) DEFAULT '0.00' COMMENT '总充值',
  `sum_withdraw` decimal(20,2) DEFAULT '0.00' COMMENT '总提款',
  `diff_recharge_sub_withdraw` decimal(20,2) DEFAULT '0.00' COMMENT '提存差',
  `arpu` decimal(20,2) DEFAULT '0.00' COMMENT 'ARPU',
  PRIMARY KEY (`id`)
) COMMENT='每日报表';
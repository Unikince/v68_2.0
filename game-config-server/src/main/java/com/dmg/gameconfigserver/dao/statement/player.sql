/**创建表-玩家每日数据*/
DROP TABLE IF EXISTS statement_player_user;
CREATE TABLE statement_player_user (
  id INT(11) NOT NULL AUTO_INCREMENT COMMENT '逻辑键',
  day_str DATE COMMENT '日期',
  user_id INT(11) COMMENT '玩家id',
  nickname VARCHAR(255) COMMENT '玩家昵称',
  sum_win DECIMAL(20,2) DEFAULT 0 COMMENT '总盈利',
  sum_bet DECIMAL(20,2) DEFAULT 0 COMMENT '总下注',
  sum_pay DECIMAL(20,2) DEFAULT 0 COMMENT '总赔付',
  game_times INT(11) DEFAULT 0 COMMENT '游戏次数',
  win_times INT(11) DEFAULT 0 COMMENT '中奖次数',
  charge DECIMAL(20,2) DEFAULT 0 COMMENT '服务费',
  max_bet DECIMAL(20,2) DEFAULT 0 COMMENT '最大下注',
  max_pay DECIMAL(20,2) DEFAULT 0 COMMENT '最大赔付',
  sum_recharge DECIMAL(20,2) DEFAULT 0 COMMENT '总充值',
  sum_withdraw DECIMAL(20,2) DEFAULT 0 COMMENT '总提款',
  PRIMARY KEY (id)
)COMMENT='玩家报表数据-玩家每日数据';


/**创建表-游戏每日数据*/
DROP TABLE IF EXISTS statement_player_game;
CREATE TABLE statement_player_game (
  id INT(11) NOT NULL AUTO_INCREMENT COMMENT '逻辑键',
  day_str DATE COMMENT '日期',
  user_id INT(11) COMMENT '玩家id',
  game_id INT(11) COMMENT '游戏id',
  game_name VARCHAR(255) COMMENT '游戏名',
  sum_win DECIMAL(20,2) DEFAULT 0 COMMENT '总盈利',
  sum_bet DECIMAL(20,2) DEFAULT 0 COMMENT '总下注',
  sum_pay DECIMAL(20,2) DEFAULT 0 COMMENT '总赔付',
  game_times INT(11) DEFAULT 0 COMMENT '游戏次数',
  win_times INT(11) DEFAULT 0 COMMENT '中奖次数',
  charge DECIMAL(20,2) DEFAULT 0 COMMENT '服务费',
  max_bet DECIMAL(20,2) DEFAULT 0 COMMENT '最大下注',
  max_pay DECIMAL(20,2) DEFAULT 0 COMMENT '最大赔付',
  PRIMARY KEY (id)
)COMMENT='玩家报表数据-游戏每日数据';


/**生成一条新数据--玩家每日数据*/
DELIMITER $$
DROP PROCEDURE IF EXISTS statement_player_insert_user_new_data $$
CREATE PROCEDURE statement_player_insert_user_new_data (
  /** 日期 */
  IN t_day_str VARCHAR (255),
  /** 玩家id */
  IN t_user_id INT (11),
  /** 玩家昵称 */
  IN t_nickname VARCHAR (255)
) 
BEGIN
  IF (SELECT IFNULL((SELECT id FROM statement_player_user WHERE day_str = t_day_str AND user_id = t_user_id),'N') = 'N') THEN 
    INSERT INTO statement_player_user (day_str,user_id,nickname) 
    VALUES (t_day_str, t_user_id, t_nickname) ;
  END IF;
END $$
DELIMITER ;


/**生成一条新数据--游戏每日数据*/
DELIMITER $$
DROP PROCEDURE IF EXISTS statement_player_insert_game_new_data $$
CREATE PROCEDURE statement_player_insert_game_new_data (
  /** 日期 */
  IN t_day_str VARCHAR (255),
  /** 玩家id */
  IN t_user_id INT (11),
    /** 游戏id */
  IN t_game_id INT (11),
  /** 游戏名 */
  IN t_game_name VARCHAR (255)
) 
BEGIN
  IF (SELECT IFNULL((SELECT id FROM statement_player_game WHERE day_str = t_day_str AND user_id = t_user_id AND game_id = t_game_id),'N') = 'N') THEN 
    INSERT INTO statement_player_game (day_str,user_id,game_id,game_name) 
    VALUES (t_day_str, t_user_id, t_game_id,t_game_name) ;
  END IF;
END $$
DELIMITER ;


/**更新累加数据--战绩(t_dmg_game_record新增调用)*/
DELIMITER $$
DROP PROCEDURE IF EXISTS statement_player_update_data_by_record $$
CREATE PROCEDURE statement_player_update_data_by_record (
  /** 日期 */
  IN t_day_str VARCHAR (255),
   /** 玩家id */
  IN t_user_id INT (11),
  /** 玩家昵称 */
  IN t_nickname VARCHAR (255),
  /** 游戏id */
  IN t_game_id INT (11),
  /** 游戏名 */
  IN t_game_name VARCHAR (255),
  /** 输赢金币 */
  IN t_win_los_gold DECIMAL (20,2),
  /** 服务费 */
  IN t_service_charge DECIMAL (20,2),
   /** 下注 */
  IN t_bets_gold DECIMAL (20,2)
) 
BEGIN
  /**statement_player_user*/
  CALL statement_player_insert_user_new_data (t_day_str, t_user_id, t_nickname);
  UPDATE statement_player_user SET   
  sum_win=sum_win+t_win_los_gold,
  sum_bet=sum_bet+t_bets_gold,
  sum_pay=sum_pay+t_win_los_gold+t_bets_gold,
  game_times=game_times+1,
  win_times=win_times+IF(t_win_los_gold>0 ,1,0),
  charge=charge+t_service_charge,
  max_bet=IF(t_bets_gold>max_bet,t_bets_gold,max_bet),
  max_pay=IF((t_win_los_gold+t_bets_gold)>max_pay ,(t_win_los_gold+t_bets_gold),max_pay)
  WHERE day_str = t_day_str AND user_id = t_user_id;
  /**statement_player_game*/
  CALL statement_player_insert_game_new_data (t_day_str, t_user_id, t_game_id,t_game_name);
  UPDATE statement_player_game SET   
  sum_win=sum_win+t_win_los_gold,
  sum_bet=sum_bet+t_bets_gold,
  sum_pay=sum_pay+t_win_los_gold+t_bets_gold,
  game_times=game_times+1,
  win_times=win_times+IF(t_win_los_gold>0 ,1,0),
  charge=charge+t_service_charge,
  max_bet=IF(t_bets_gold>max_bet,t_bets_gold,max_bet),
  max_pay=IF((t_win_los_gold+t_bets_gold)>max_pay ,(t_win_los_gold+t_bets_gold),max_pay)
  WHERE day_str = t_day_str AND user_id = t_user_id AND game_id = t_game_id;
END $$
DELIMITER ;

# 更新累加数据--充值调用(t_persion_recharge_log完成、t_platform_recharge_log完成)
DELIMITER $$
DROP PROCEDURE IF EXISTS statement_player_update_data_by_recharge $$
CREATE PROCEDURE statement_player_update_data_by_recharge (
  /** 日期 */
  IN t_day_str VARCHAR (255),
   /** 玩家id */
  IN t_user_id INT (11),
  /** 玩家昵称 */
  IN t_nickname VARCHAR (255),
  /** 充值金币 */
  IN recharge_money DECIMAL (20,2)
) 
BEGIN
  /**statement_player_user*/
  CALL statement_player_insert_user_new_data (t_day_str, t_user_id, t_nickname);
  UPDATE statement_player_user SET   
  sum_recharge=sum_recharge+recharge_money
  WHERE day_str = t_day_str AND user_id = t_user_id;
END $$
DELIMITER ;


# 更新累加数据--提款调用(t_withdraw_order完成)
DELIMITER $$
DROP PROCEDURE IF EXISTS statement_player_update_data_by_withdraw $$
CREATE PROCEDURE statement_player_update_data_by_withdraw (
  /** 日期 */
  IN t_day_str VARCHAR (255),
   /** 玩家id */
  IN t_user_id INT (11),
  /** 玩家昵称 */
  IN t_nickname VARCHAR (255),
  /** 提款金币 */
  IN withdraw_money DECIMAL (20,2)
) 
BEGIN
  /**statement_player_user*/
  CALL statement_player_insert_user_new_data (t_day_str, t_user_id, t_nickname);
  UPDATE statement_player_user SET   
  sum_withdraw=sum_withdraw+withdraw_money
  WHERE day_str = t_day_str AND user_id = t_user_id;
END $$
DELIMITER ;


/**循环所有t_dmg_game_record数据初始化数据*/
DELIMITER $$
DROP PROCEDURE IF EXISTS statement_player_circulation_all_data_by_t_dmg_game_record $$
CREATE PROCEDURE statement_player_circulation_all_data_by_t_dmg_game_record () 
BEGIN
  /** 日期 */
  DECLARE t_day_str VARCHAR (255);
  /** 玩家id */
  DECLARE t_user_id INT (11);
  /** 玩家昵称 */
  DECLARE t_nickname VARCHAR (255);
  /** 游戏id */
  DECLARE t_game_id INT (11);
  /** 游戏名 */
  DECLARE t_game_name VARCHAR (255);
  /** 输赢金币 */
  DECLARE t_win_los_gold DECIMAL (20,2);
  /** 服务费 */
  DECLARE t_service_charge DECIMAL (20,2);
   /** 下注 */
  DECLARE t_bets_gold DECIMAL (20,2);
  
  /**用于处理游标到达最后一行的情况 */
  DECLARE end_flag INT DEFAULT 0 ;
  /**声明游标cursor_name（cursor_name是个多行结果集）  */
  DECLARE cursor_name CURSOR FOR 
  SELECT 
    DATE_FORMAT(game_date, '%Y-%m-%d') AS day_str,
    user_id,user_name,game_id,game_name,win_los_gold,service_charge,bets_gold
  FROM
    t_dmg_game_record WHERE is_robot=0;
  /**设置一个终止标记   */
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET end_flag = 1 ;
  /**打开游标 */
  OPEN cursor_name ;
  /**获取游标当前指针的记录，读取一行数据并传给变量*/
  FETCH cursor_name INTO t_day_str,t_user_id,t_nickname,t_game_id,t_game_name,t_win_los_gold,t_service_charge,t_bets_gold ;
  /**开始循环，判断是否游标已经到达了最后作为循环条件 */
  WHILE
    end_flag <> 1 DO 
    CALL statement_player_update_data_by_record(t_day_str,t_user_id,t_nickname,t_game_id,t_game_name,t_win_los_gold,t_service_charge,t_bets_gold);
    FETCH cursor_name INTO t_day_str,t_user_id,t_nickname,t_game_id,t_game_name,t_win_los_gold,t_service_charge,t_bets_gold ;
  END WHILE ;
  /**关闭游标 */
  CLOSE cursor_name ;
END $$
DELIMITER ;
CALL statement_player_circulation_all_data_by_t_dmg_game_record ()  ;
DROP PROCEDURE statement_player_circulation_all_data_by_t_dmg_game_record  ;


/**循环所有t_persion_recharge_log数据初始化数据*/
DELIMITER $$
DROP PROCEDURE IF EXISTS statement_player_circulation_all_data_by_t_persion_recharge_log $$
CREATE PROCEDURE statement_player_circulation_all_data_by_t_persion_recharge_log () 
BEGIN
  /** 日期 */
  DECLARE t_day_str VARCHAR (255);
  /** 玩家id */
  DECLARE t_user_id INT (11);
  /** 玩家昵称 */
  DECLARE t_nickname VARCHAR (255);
  /** 充值金币 */
  DECLARE t_recharge_money DECIMAL (20,2);
  
  /**用于处理游标到达最后一行的情况 */
  DECLARE end_flag INT DEFAULT 0 ;
  /**声明游标cursor_name（cursor_name是个多行结果集）  */
  DECLARE cursor_name CURSOR FOR 
  SELECT 
    DATE_FORMAT(deal_date, '%Y-%m-%d') AS day_str,
    user_id,nickname,account_amount
  FROM
    t_persion_recharge_log WHERE `status`=2;
  /**设置一个终止标记   */
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET end_flag = 1 ;
  /**打开游标 */
  OPEN cursor_name ;
  /**获取游标当前指针的记录，读取一行数据并传给变量*/
  FETCH cursor_name INTO t_day_str,t_user_id,t_nickname,t_recharge_money ;
  /**开始循环，判断是否游标已经到达了最后作为循环条件 */
  WHILE
    end_flag <> 1 DO 
    CALL statement_player_update_data_by_recharge(t_day_str,t_user_id,t_nickname,t_recharge_money);
    FETCH cursor_name INTO t_day_str,t_user_id,t_nickname,t_recharge_money ;
  END WHILE ;
  /**关闭游标 */
  CLOSE cursor_name ;
END $$
DELIMITER ;
CALL statement_player_circulation_all_data_by_t_persion_recharge_log () ;
DROP PROCEDURE statement_player_circulation_all_data_by_t_persion_recharge_log ;


/**循环所有t_platform_recharge_log数据初始化数据*/
DELIMITER $$
DROP PROCEDURE IF EXISTS statement_player_circulation_all_data_by_t_platform_recharge_log $$
CREATE PROCEDURE statement_player_circulation_all_data_by_t_platform_recharge_log () 
BEGIN
  /** 日期 */
  DECLARE t_day_str VARCHAR (255);
  /** 玩家id */
  DECLARE t_user_id INT (11);
  /** 玩家昵称 */
  DECLARE t_nickname VARCHAR (255);
  /** 充值金币 */
  DECLARE t_recharge_money DECIMAL (20,2);
  
  /**用于处理游标到达最后一行的情况 */
  DECLARE end_flag INT DEFAULT 0 ;
  /**声明游标cursor_name（cursor_name是个多行结果集）  */
  DECLARE cursor_name CURSOR FOR 
  SELECT 
    DATE_FORMAT(deal_date, '%Y-%m-%d') AS day_str,
    user_id,nick_name,recharge_amount
  FROM
    t_platform_recharge_log WHERE order_status=15 OR order_status=20;
  /**设置一个终止标记   */
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET end_flag = 1 ;
  /**打开游标 */
  OPEN cursor_name ;
  /**获取游标当前指针的记录，读取一行数据并传给变量*/
  FETCH cursor_name INTO t_day_str,t_user_id,t_nickname,t_recharge_money ;
  /**开始循环，判断是否游标已经到达了最后作为循环条件 */
  WHILE
    end_flag <> 1 DO 
    CALL statement_player_update_data_by_recharge(t_day_str,t_user_id,t_nickname,t_recharge_money);
    FETCH cursor_name INTO t_day_str,t_user_id,t_nickname,t_recharge_money ;
  END WHILE ;
  /**关闭游标 */
  CLOSE cursor_name ;
END $$
DELIMITER ;
CALL statement_player_circulation_all_data_by_t_platform_recharge_log () ;
DROP PROCEDURE statement_player_circulation_all_data_by_t_platform_recharge_log ;


/**循环所有t_withdraw_order数据初始化数据*/
DELIMITER $$
DROP PROCEDURE IF EXISTS statement_player_circulation_all_data_by_t_withdraw_order $$
CREATE PROCEDURE statement_player_circulation_all_data_by_t_withdraw_order () 
BEGIN
  /** 日期 */
  DECLARE t_day_str VARCHAR (255);
  /** 玩家id */
  DECLARE t_user_id INT (11);
  /** 玩家昵称 */
  DECLARE t_nickname VARCHAR (255);
  /** 充值金币 */
  DECLARE t_withdraw_money DECIMAL (20,2);
  
  /**用于处理游标到达最后一行的情况 */
  DECLARE end_flag INT DEFAULT 0 ;
  /**声明游标cursor_name（cursor_name是个多行结果集）  */
  DECLARE cursor_name CURSOR FOR 
  SELECT 
    DATE_FORMAT(account_date, '%Y-%m-%d') AS day_str,
    user_id,nick_name,account
  FROM
    t_withdraw_order WHERE order_status=15 OR order_status=20;
  /**设置一个终止标记   */
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET end_flag = 1 ;
  /**打开游标 */
  OPEN cursor_name ;
  /**获取游标当前指针的记录，读取一行数据并传给变量*/
  FETCH cursor_name INTO t_day_str,t_user_id,t_nickname,t_withdraw_money ;
  /**开始循环，判断是否游标已经到达了最后作为循环条件 */
  WHILE
    end_flag <> 1 DO 
    CALL statement_player_update_data_by_withdraw(t_day_str,t_user_id,t_nickname,t_withdraw_money);
    FETCH cursor_name INTO t_day_str,t_user_id,t_nickname,t_withdraw_money ;
  END WHILE ;
  /**关闭游标 */
  CLOSE cursor_name ;
END $$
DELIMITER ;
CALL statement_player_circulation_all_data_by_t_withdraw_order () ;
DROP PROCEDURE statement_player_circulation_all_data_by_t_withdraw_order ;


/**触发器--t_dmg_game_record新增*/
DELIMITER $$
DROP TRIGGER IF EXISTS statement_player_trigger_t_dmg_game_record $$
CREATE TRIGGER statement_player_trigger_t_dmg_game_record AFTER INSERT 
ON t_dmg_game_record FOR EACH ROW 
BEGIN
  IF(new.is_robot = 0) 
  THEN CALL statement_player_update_data_by_record (
    DATE_FORMAT(new.game_date, '%Y-%m-%d'),
    new.user_id,
    new.user_name,
    new.game_id,
    new.game_name,
    new.win_los_gold,
    new.service_charge,
    new.bets_gold
  ) ;
  END IF ;
END $$
DELIMITER ;


/**触发器--t_persion_recharge_log新增*/
DELIMITER $$
DROP TRIGGER IF EXISTS statement_player_trigger_t_persion_recharge_log $$
CREATE TRIGGER statement_player_trigger_t_persion_recharge_log AFTER UPDATE 
ON t_persion_recharge_log FOR EACH ROW 
BEGIN
  IF(new.status=2) 
  THEN CALL statement_player_update_data_by_recharge (
    DATE_FORMAT(new.deal_date, '%Y-%m-%d'),
    new.user_id,
    new.nickname,
    new.account_amount
  ) ;
  END IF ;
END $$
DELIMITER ;


/**触发器--t_platform_recharge_log新增*/
DELIMITER $$
DROP TRIGGER IF EXISTS statement_player_trigger_t_platform_recharge_log $$
CREATE TRIGGER statement_player_trigger_t_platform_recharge_log AFTER UPDATE 
ON t_platform_recharge_log FOR EACH ROW 
BEGIN
  IF(new.order_status=15 OR new.order_status=20) 
  THEN CALL statement_player_update_data_by_recharge (
    DATE_FORMAT(new.deal_date, '%Y-%m-%d'),
    new.user_id,
    new.nick_name,
    new.recharge_amount
  ) ;
  END IF ;
END $$
DELIMITER ;


/**触发器--t_withdraw_order新增*/
DELIMITER $$
DROP TRIGGER IF EXISTS statement_player_trigger_t_withdraw_order $$
CREATE TRIGGER statement_player_trigger_t_withdraw_order AFTER UPDATE 
ON t_withdraw_order FOR EACH ROW 
BEGIN
  IF(new.order_status=15 OR new.order_status=20) 
  THEN CALL statement_player_update_data_by_withdraw (
    DATE_FORMAT(new.account_date, '%Y-%m-%d'),
    new.user_id,
    new.nick_name,
    new.account
  ) ;
  END IF ;
END $$
DELIMITER ;
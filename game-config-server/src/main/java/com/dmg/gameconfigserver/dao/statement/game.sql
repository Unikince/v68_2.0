/**创建表*/
DROP TABLE IF EXISTS statement_game;
CREATE TABLE statement_game (
  id INT(11) NOT NULL AUTO_INCREMENT COMMENT '逻辑键',
  day_str DATE COMMENT '日期',
  game_id INT(11) COMMENT '游戏id',
  game_name VARCHAR(255) COMMENT '游戏名',
  file_id INT(11) COMMENT '场次id',
  file_name VARCHAR(255) COMMENT '场次名称',
  sum_win DECIMAL(20,2) DEFAULT 0 COMMENT '总盈利',
  sum_bet DECIMAL(20,2) DEFAULT 0 COMMENT '总下注',
  sum_pay DECIMAL(20,2) DEFAULT 0 COMMENT '总赔付',
  game_times INT(11) DEFAULT 0 COMMENT '游戏次数',
  win_times INT(11) DEFAULT 0 COMMENT '中奖次数',
  charge DECIMAL(20,2) DEFAULT 0 COMMENT '服务费',
  max_bet DECIMAL(20,2) DEFAULT 0 COMMENT '最大下注',
  max_pay DECIMAL(20,2) DEFAULT 0 COMMENT '最大赔付',
  PRIMARY KEY (id)
)COMMENT='游戏报表数据';


/**生成一条新数据*/
DELIMITER $$
DROP PROCEDURE IF EXISTS statement_game_insert_new_data $$
CREATE PROCEDURE statement_game_insert_new_data (
  /** 日期 */
  IN t_day_str VARCHAR (255),
  /** 游戏id */
  IN t_game_id INT (11),
  /** 游戏名 */
  IN t_game_name VARCHAR (255),
  /** 场次id */
  IN t_file_id INT (11),
  /** 场次名称 */
  IN t_file_name VARCHAR (255)
) 
BEGIN
  IF (SELECT IFNULL((SELECT id FROM statement_game WHERE day_str = t_day_str AND game_id = t_game_id AND file_id = t_file_id),'N') = 'N') THEN 
    INSERT INTO statement_game (day_str,game_id,game_name, file_id, file_name) 
    VALUES (t_day_str, t_game_id, t_game_name, t_file_id, t_file_name) ;
  END IF;
END $$
DELIMITER ;


/**更新累加数据--t_dmg_game_record新增调用*/
DELIMITER $$
DROP PROCEDURE IF EXISTS statement_game_update_data $$
CREATE PROCEDURE statement_game_update_data (
  /** 日期 */
  IN t_day_str VARCHAR (255),
  /** 游戏id */
  IN t_game_id INT (11),
  /** 游戏名 */
  IN t_game_name VARCHAR (255),
  /** 场次id */
  IN t_file_id INT (11),
  /** 场次名称 */
  IN t_file_name VARCHAR (255),
  /** 输赢金币 */
  IN t_win_los_gold DECIMAL (20,2),
  /** 服务费 */
  IN t_service_charge DECIMAL (20,2),
   /** 下注 */
  IN t_bets_gold DECIMAL (20,2)
) 
BEGIN
  CALL statement_game_insert_new_data (t_day_str,t_game_id,t_game_name,t_file_id,t_file_name);
  UPDATE statement_game SET   
  sum_win=sum_win-t_win_los_gold,
  sum_bet=sum_bet+t_bets_gold,
  sum_pay=sum_pay+t_win_los_gold+t_bets_gold,
  game_times=game_times+1,
  win_times=win_times+IF(t_win_los_gold>0 ,1,0),
  charge=charge+t_service_charge,
  max_bet=IF(t_bets_gold>max_bet,t_bets_gold,max_bet),
  max_pay=IF((t_win_los_gold+t_bets_gold)>max_pay ,(t_win_los_gold+t_bets_gold),max_pay)
  WHERE day_str = t_day_str AND game_id = t_game_id AND file_id = t_file_id;
END $$
DELIMITER ;

/**循环所有t_dmg_game_record数据初始化数据*/
DELIMITER $$
DROP PROCEDURE IF EXISTS statement_game_circulation_t_dmg_game_record $$
CREATE PROCEDURE statement_game_circulation_t_dmg_game_record () 
BEGIN
  /** 日期 */
  DECLARE t_day_str VARCHAR (255);
  /** 游戏id */
  DECLARE t_game_id INT (11);
  /** 游戏名 */
  DECLARE t_game_name VARCHAR (255);
  /** 场次id */
  DECLARE t_file_id INT (11);
  /** 场次名称 */
  DECLARE t_file_name VARCHAR (255);
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
    game_id,game_name,file_id,file_name,win_los_gold,service_charge,bets_gold
  FROM
    t_dmg_game_record WHERE is_robot=0;
  /**设置一个终止标记   */
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET end_flag = 1 ;
  /**打开游标 */
  OPEN cursor_name ;
  /**获取游标当前指针的记录，读取一行数据并传给变量*/
  FETCH cursor_name INTO t_day_str,t_game_id,t_game_name,t_file_id,t_file_name,t_win_los_gold,t_service_charge,t_bets_gold ;
  /**开始循环，判断是否游标已经到达了最后作为循环条件 */
  WHILE
    end_flag <> 1 DO 
    CALL statement_game_update_data(t_day_str,t_game_id,t_game_name,t_file_id,t_file_name,t_win_los_gold,t_service_charge,t_bets_gold);
    FETCH cursor_name INTO t_day_str,t_game_id,t_game_name,t_file_id,t_file_name,t_win_los_gold,t_service_charge,t_bets_gold ;
  END WHILE ;
  /**关闭游标 */
  CLOSE cursor_name ;
END $$
DELIMITER ;
CALL statement_game_circulation_t_dmg_game_record () ;
DROP PROCEDURE statement_game_circulation_t_dmg_game_record ;


/**触发器--t_dmg_game_record新增*/
DELIMITER $$
DROP TRIGGER IF EXISTS statement_game_trigger_t_dmg_game_record $$
CREATE TRIGGER statement_game_trigger_t_dmg_game_record AFTER INSERT 
ON t_dmg_game_record FOR EACH ROW 
BEGIN
  IF(new.is_robot = 0) 
  THEN CALL statement_game_update_data (
    DATE_FORMAT(new.game_date, '%Y-%m-%d'),
    new.game_id,
    new.game_name,
    new.file_id,
    new.file_name,
    new.win_los_gold,
    new.service_charge,
    new.bets_gold
  ) ;
  END IF ;
END $$
DELIMITER ;

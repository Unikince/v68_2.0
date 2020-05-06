DELIMITER $$
DROP PROCEDURE IF EXISTS agent_new_child_num $$
CREATE PROCEDURE agent_new_child_num (
  /** 代理id */
  IN t_user_id INT (11)
) 
BEGIN
  DECLARE t_day_str DATE;
  SELECT DATE(NOW()) INTO t_day_str;
  IF (SELECT IFNULL((SELECT id FROM a_agent_new_people WHERE day_str = t_day_str AND user_id = t_user_id),'N') = 'N') THEN 
    INSERT INTO a_agent_new_people (day_str,user_id,num) 
    VALUES (t_day_str, t_user_id, 0) ;
  END IF;
  UPDATE a_agent_new_people SET num=num+1 WHERE day_str=t_day_str AND user_id = t_user_id;
END $$
DELIMITER ;
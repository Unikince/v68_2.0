/**
 * 
 */
package com.dmg.clubserver.mq;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * @author 俱乐部游戏记录消费者
 */
@Service
@Order
public class ClubGameRecordConsumer {
	/*@Autowired
	private ClubGameRecordDao clubGameRecordDao;

	@Override
	public Groups getGroup() {
		return Groups.CLUB;
	}

	@Override
	public Topics getTopic() {
		return Topics.CLUB;
	}

	@Override
	public Tags getTag() {
		return Tags.CLUB_GAME_RECORD;
	}

	@Override
	public void handler(String msg) throws Exception {
		ClubGameRecordBean vo = JSONObject.parseObject(msg).toJavaObject(ClubGameRecordBean.class);
		// 记录战绩
		ClubGameRecordBean clubGameRecordBean = clubGameRecordDao.selectOne(new LambdaQueryWrapper<ClubGameRecordBean>()
				.eq(ClubGameRecordBean::getRoleId,vo.getRoleId())
				.eq(ClubGameRecordBean::getGameId,vo.getGameId())
				.eq(ClubGameRecordBean::getRecordType,1));
		// 添加总记录
		if (clubGameRecordBean == null) {
			clubGameRecordBean = new ClubGameRecordBean();
			clubGameRecordBean.setClubId(vo.getClubId());
			clubGameRecordBean.setRoleId(vo.getRoleId());
			clubGameRecordBean.setGameType(vo.getGameType());
			clubGameRecordBean.setEndDate(new Date());
			clubGameRecordBean.setScore(vo.getScore());
			clubGameRecordBean.setRecordType(1);
			clubGameRecordBean.setRoomCreatorId(vo.getRoomCreatorId());
			clubGameRecordBean.setRoomId(vo.getRoomId());
			clubGameRecordBean.setGameId(vo.getGameId());
			clubGameRecordBean.setRound(vo.getRound());
			clubGameRecordBean.setRoomCardConsumeNum(vo.getRoomCardConsumeNum());
			clubGameRecordDao.insert(clubGameRecordBean);
			// 细节
			clubGameRecordBean.setId(null);
			clubGameRecordBean.setRecordType(2);
			clubGameRecordBean.setHu(vo.getHu());
			clubGameRecordDao.insert(clubGameRecordBean);
		} else {
			// 更新总分
			clubGameRecordBean.setScore(clubGameRecordBean.getScore() + vo.getScore());
			clubGameRecordBean.setRound(vo.getRound());
			clubGameRecordBean.setEndDate(new Date());
			clubGameRecordDao.updateById(clubGameRecordBean);

			// 添加细节
			ClubGameRecordBean detail = new ClubGameRecordBean();
			detail.setRoleId(vo.getRoleId());
			detail.setGameType(vo.getGameType());
			detail.setEndDate(new Date());
			detail.setScore(vo.getScore());
			detail.setRecordType(2);
			detail.setRoomId(vo.getRoomId());
			detail.setGameId(vo.getGameId());
			detail.setRound(vo.getRound());
			detail.setHu(vo.getHu());
			detail.setClubId(vo.getClubId());
			detail.setRoomCreatorId(vo.getRoomCreatorId());
			detail.setRoomCardConsumeNum(vo.getRoomCardConsumeNum());
			clubGameRecordDao.insert(detail);
		}
	}*/

}

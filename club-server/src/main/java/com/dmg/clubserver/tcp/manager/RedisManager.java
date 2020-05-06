package com.dmg.clubserver.tcp.manager;

import com.zyhy.common_server.constants.Common;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;


@Service
public class RedisManager {
	@Resource(name = "redisTemplate")
	private RedisTemplate<CharSequence, String> redisTemplate;
	
	/**
	 * 添加redis 客户端channel通道对应数据
	 * @param machineGroup 安卓，苹果等。。
	 * @param uid 玩家uid
	 */
	public void addUserChannel(int machineGroup, String uid) {
		StringBuilder str = new StringBuilder(Common.USER_CHANNEL_GROUP);
		str.append(Common.BRIDGING).append(machineGroup).append(Common.BRIDGING).append(Common.ChildGroup.NONE.getValue());
		redisTemplate.opsForSet().add(str, uid);
	}
	
	/**
	 * 客户端切换子group
	 * @param machineGroup
	 * @param oldChild 当前子组
	 * @param childGroup 切换子组id
	 * @param uid
	 */
	public void changeUserChannel(int machineGroup,int oldChild,int childGroup, String uid) {
		StringBuilder str = new StringBuilder(Common.USER_CHANNEL_GROUP);
		str.append(Common.BRIDGING).append(machineGroup).append(Common.BRIDGING);
		StringBuilder str0 = str.append(childGroup);
		redisTemplate.opsForSet().move(str.append(oldChild), uid, str0);
	}
	
	/**
	 * 删除缓存当前uid
	 * @param machineGroup
	 * @param uid
	 */
	public void deleteUserChannel(int machineGroup,String uid) {
		StringBuilder str = new StringBuilder(Common.USER_CHANNEL_GROUP);
		str.append(Common.BRIDGING).append(machineGroup).append(Common.BRIDGING);
		SetOperations<CharSequence, String> ops = redisTemplate.opsForSet();
		for(int i = 0; i <= Common.ChildGroup.LHJ.getValue(); i ++) {
			if(ops.isMember(str.append(i), uid)) {
				ops.remove(str, uid);
				break;
			}
		}
	}
	/**
	 * 根据组，子组获取数据
	 * @param machineGroup
	 * @param childGroup
	 * @return
	 */
	public Set<String> getByGroupId(int machineGroup,int childGroup) {
		Set<String> set = new HashSet<>();
		SetOperations<CharSequence, String> ops = redisTemplate.opsForSet();
		StringBuilder str = new StringBuilder(Common.USER_CHANNEL_GROUP);
		str.append(Common.BRIDGING);
		//所有子组
		if(childGroup == Common.ChildGroup.NONE.getValue()) {
			//所有主组
			if(machineGroup == Common.MachineGroup.NONE.getValue()) {
				//返回所有
				return null;
			} else {
				//返回某组所有
				str.append(machineGroup).append(Common.BRIDGING);
				for(int i = 0; i <= Common.ChildGroup.LHJ.getValue(); i ++) {
					Cursor<String> curosr = ops.scan(str.append(i), ScanOptions.NONE);
					while(curosr.hasNext()) {
						set.add(curosr.next());
					}
				}
			}
		} else {//某一子组
			//所有主组
			if(machineGroup == Common.MachineGroup.NONE.getValue()) {
				//返回所有某一子组
				for(int i = 0; i <= Common.MachineGroup.APPLE.getValue(); i ++) {
					str.append(i).append(Common.BRIDGING).append(childGroup);
					Cursor<String> curosr = ops.scan(str, ScanOptions.NONE);
					while(curosr.hasNext()) {
						set.add(curosr.next());
					}
				}
			} else {
				//返回某组某一子组
				str.append(machineGroup).append(Common.BRIDGING).append(childGroup);
				Cursor<String> curosr = ops.scan(str, ScanOptions.NONE);
				while(curosr.hasNext()) {
					set.add(curosr.next());
				}
			}
		}
		return set;
	}
}

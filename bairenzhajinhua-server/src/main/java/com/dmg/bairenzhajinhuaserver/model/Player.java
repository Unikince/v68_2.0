package com.dmg.bairenzhajinhuaserver.model;


import com.dmg.bairenzhajinhuaserver.common.model.BasePlayer;
import com.dmg.bairenzhajinhuaserver.common.result.MessageResult;
import com.dmg.bairenzhajinhuaserver.common.utils.SpringContextUtil;
import com.dmg.bairenzhajinhuaserver.service.PushService;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/29 14:42
 * @Version V1.0
 **/
public class Player extends BasePlayer {

	private static final long serialVersionUID = 1L;

	public void push(String m,Integer res,Object msg){
		PushService service = SpringContextUtil.getBean(PushService.class);
		service.push(getUserId(), m, res, msg);
    }

    public void push(String m, Integer res) {
    	PushService service = SpringContextUtil.getBean(PushService.class);
		service.push(getUserId(), m, res);
    }

    public void push(String m) {
    	PushService service = SpringContextUtil.getBean(PushService.class);
		service.push(getUserId(), m);
    }

    public void push(MessageResult messageResult) {
    	PushService service = SpringContextUtil.getBean(PushService.class);
		service.push(getUserId(), messageResult);
    }

}
/**
 * 
 */
package com.dmg.common.starter.rocketmq.common.message;

import java.nio.charset.Charset;

import org.apache.rocketmq.common.message.Message;

/**
 * @author ASUS
 *
 */
public class MessageBuild {

	public static Message build(String topic, String tags, String data){
		Message msg = new Message(topic, tags, data.getBytes(Charset.forName("UTF-8")));
		return msg;
	}

}

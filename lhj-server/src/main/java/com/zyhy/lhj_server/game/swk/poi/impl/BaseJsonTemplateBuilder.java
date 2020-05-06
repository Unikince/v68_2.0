/**   
* @Title: BaseJsonTemplateBuilder.java
* @Package com.longma.ssss.poi.impl
* @Description:
* @author nanjun.li   
* @date 2017-1-11 下午2:26:56
* @version V1.0   
*/


package com.zyhy.lhj_server.game.swk.poi.impl;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: BaseJsonTemplateBuilder
 * @Description:
 * @author nanjun.li
 * @date 2017-1-11 下午2:26:56
 */

public interface BaseJsonTemplateBuilder {

	TemplateObject buildTemplate (JSONObject jsonObject) ;
}

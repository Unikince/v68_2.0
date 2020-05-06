/**
 * 
 */
package com.dmg.clubserver.config.rule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.zyhy.common_server.util.IpUtils;

/**
 * 根据用户IP哈希做Rest路由
 * @author nanjun.li
 */
public class UserIpHashRule extends AbstractLoadBalancerRule{
	
	@Override
	public Server choose(Object key) {
		ILoadBalancer lb = getLoadBalancer();
		Server server = null;
		int count = 0;
		while (server == null && count++ < 10) {
			List<Server> readchableServers = lb.getReachableServers();
			List<Server> allServers = lb.getAllServers();
			int upCount = readchableServers.size();
			int serverCount = allServers.size();
			if (upCount == 0 || serverCount == 0) {
				return null;
			}
			int nextServerIndex = ipUserHash(serverCount);
			server = allServers.get(nextServerIndex);
			if (server == null) {
				// 从运行状态切换成就绪状态,等待下次执行
				Thread.yield();
				continue;
			}
			if (server.isAlive() && server.isReadyToServe()) {
				return server;
			}
		}
		return null;
	}
	
	/**
	 * 根据服务个数计算IP请求分配给其中之一
	 * @param serverCount 服务个数
	 * @return
	 */
	private int ipUserHash(int serverCount) {
		HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String userIp = IpUtils.getIpAddress(request);
        int userHashCode = Math.abs((userIp).hashCode());
		return userHashCode%serverCount;
	}

	@Override
	public void initWithNiwsConfig(IClientConfig clientConfig) {}

}

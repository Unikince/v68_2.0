package vequn.lobby.test;

import com.alibaba.fastjson.JSON;
import com.dmg.core.manager.DefFactory;
import com.dmg.lobby.entity.IPInfo;
import com.dmg.lobby.service.IPService;
import com.dmg.lobby.service.impl.IPServiceImpl;

import util.RandUtil;
import util.ip.IPLocation;
import util.ip.IPSeeker;

/**
 * @author zhuqd
 * @Date 2017年9月12日
 * @Desc
 */
public class TestIP {

	public void testIP() {
		IPSeeker seeker = new IPSeeker();
		IPLocation ip = seeker.getIPLocation("103.163.231.179");
		System.out.println(ip.getArea() + " , " + ip.getCountry());
	}

	public void testIPService() {
		IPService service = new IPServiceImpl();
		try {
			DefFactory.instance().init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// IPInfo info = service.parseIP("171.217.52.37");
		// System.out.println("ip:" + "" + ", -- " + JSON.toJSONString(info));
		for (int i = 0; i < 100; i++) {
			String ip = createRandIP();
			IPInfo info = service.parseIP(ip);
			System.out.println("ip:" + ip + ", -- " + JSON.toJSONString(info));
		}
	}

	private String createRandIP() {
		int a = RandUtil.randInt(254) + 1;
		int b = RandUtil.randInt(254) + 1;
		int c = RandUtil.randInt(254) + 1;
		int d = RandUtil.randInt(254) + 1;
		return a + "." + b + "." + c + "." + d;
	}

	public void testBaiduIP() {
		IPServiceImpl service = new IPServiceImpl();
		IPInfo address = service.baiduIP("sdasadsad");
		System.out.println(JSON.toJSONString(address));
	}

	public static void main(String[] args) {
		TestIP t = new TestIP();
		// t.testIP();
		// t.testIPService();
		t.testBaiduIP();
	}

}

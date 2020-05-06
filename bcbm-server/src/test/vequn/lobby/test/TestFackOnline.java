package vequn.lobby.test;

/**
 * @author zhuqd
 * @Date 2017年10月18日
 * @Desc
 */
public class TestFackOnline {

	public void func() {
		int base = 10;
		int serverId = 11;
		int online = 0;
		for (int hour = 2; hour < 9; hour++) {
			int add = 250 / 7 / 6;
			for (int min = 0; min < 6; min++) {
				online = online + add;
				String sql = "INSERT INTO fakeOnline(serverId,hour,minute,online) VALUES(";
				sql += serverId + ",";
				sql += hour + ",";
				sql += min * 10 + ",";
				sql += (online + base) + ");";
				System.out.println(sql);
			}

		}
		for (int hour = 9; hour < 12; hour++) {
			int add = 300 / 3 / 6;
			for (int min = 0; min < 6; min++) {
				online = online + add;
				String sql = "INSERT INTO fakeOnline(serverId,hour,minute,online) VALUES(";
				sql += serverId + ",";
				sql += hour + ",";
				sql += min * 10 + ",";
				sql += (online + base) + ");";
				System.out.println(sql);
			}
		}
		for (int hour = 12; hour < 21; hour++) {
			int add = 700 / 9 / 6;
			for (int min = 0; min < 6; min++) {
				online = online + add;
				String sql = "INSERT INTO fakeOnline(serverId,hour,minute,online) VALUES(";
				sql += serverId + ",";
				sql += hour + ",";
				sql += min * 10 + ",";
				sql += (online + base) + ");";
				System.out.println(sql);
			}
		}
		for (int hour = 21; hour < 26; hour++) {
			int add = -1150 / 5 / 6;
			for (int min = 0; min < 6; min++) {
				int hour0 = hour;
				if (hour == 24) {
					hour0 = 0;
				} else if (hour == 25) {
					hour0 = 1;
				}
				online = online + add;
				String sql = "INSERT INTO fakeOnline(serverId,hour,minute,online) VALUES(";
				sql += serverId + ",";
				sql += hour0 + ",";
				sql += min * 10 + ",";
				sql += (online + base) + ");";
				System.out.println(sql);
			}
		}
	}

	public static void main(String[] args) {
		TestFackOnline t = new TestFackOnline();
		t.func();
	}

}

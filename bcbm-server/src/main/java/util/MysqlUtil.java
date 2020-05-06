package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Date: 2015年9月30日 下午3:46:03
 * @Author: zhuqd
 * @Description:
 */
public class MysqlUtil {

	/**
	 * @param sql
	 * @return
	 */
	public static Integer findInteger(Connection conn, String sql) {
		return Value.parserInt(findString(conn, sql));
	}

	/**
	 *
	 * @param sql
	 * @return
	 */
	public static Long findLong(Connection conn, String sql) {
		return Value.parserLong(findString(conn, sql));
	}

	/**
	 *
	 * @param sql
	 * @return
	 */
	public static String findString(Connection conn, String sql) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String object = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				object = rs.getString(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return object;
	}

	/**
	 * 插入
	 *
	 * @param sql
	 * @return
	 */
	public static int insert(Connection conn, String sql) {
		PreparedStatement ps = null;
		int execute = 0;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			execute = ps.executeUpdate(sql);
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return execute;
	}

	/**
	 * 插入
	 *
	 * @param sql
	 * @return
	 */
	public static boolean update(Connection conn, String sql) {
		PreparedStatement ps = null;
		boolean success = false;
		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			ps.executeUpdate(sql);
			conn.commit();
			success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}
}

package util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.alibaba.fastjson.JSONObject;

import util.StringHelper;

/**
 * @author zhuqd
 * @Date 2017年7月27日
 * @Desc
 */
public class HttpUtil {

	/**
	 * 发送https请求
	 * 
	 * @param requestUrl
	 * @return
	 */
	public static JSONObject sendHttpsGet(String url) {
		return sendHttpsGet(url, "");
	}

	/**
	 * 发送https请求
	 *
	 * @param requestUrl
	 *            请求地址
	 * @param outputString
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject sendHttpsGet(String requestUrl, String outputString) {
		JSONObject json = null;
		StringBuffer buffer = new StringBuffer();
		try {
			TrustManager[] tm = { new DefaultX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");

			// 当outputStr不为null时向输出流写数据
			if (StringHelper.isEmpty(outputString)) {
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(outputString.getBytes("utf-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				buffer.append(line);
			}

			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			conn.disconnect();
			json = JSONObject.parseObject(buffer.toString());
		} catch (IOException | KeyManagementException | NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		}
		return json;
	}
}

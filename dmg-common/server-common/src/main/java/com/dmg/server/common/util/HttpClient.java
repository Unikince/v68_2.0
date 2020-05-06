package com.dmg.server.common.util;

import com.google.common.collect.Maps;
import net.sf.cglib.beans.BeanMap;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @Author liubo
 * @Description //TODO HttpClient
 * @Date 10:20 2020/2/12
 **/
public final class HttpClient {

	private static volatile HttpClient instance;
	private OkHttpClient okHttpClient;
	private Request.Builder requestBuilder;
	private static final long DEFAULT_READ_TIMEOUT = 60000;
	private static final long DEFAULT_CONNECT_TIMEOUT = 10000;
	private static final long DEFAULT_WRITE_TIMEOUT = 60000;
	private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

	private HttpClient(OkHttpClient okHttpClient, Request.Builder requestBuilder) {
		this.okHttpClient = okHttpClient;
		this.requestBuilder = requestBuilder;
	}

	public static HttpClient getInstance() {
		return getInstance(DEFAULT_READ_TIMEOUT, DEFAULT_CONNECT_TIMEOUT, DEFAULT_WRITE_TIMEOUT);
	}

	@SuppressWarnings("deprecation")
	public static HttpClient getInstance(long readTimeout, long connectTimeout, long writeTimeout) {
		if (null == instance) {
			synchronized (HttpClient.class) {
				if (null == instance) {
					OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
					clientBuilder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
					clientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
					clientBuilder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
					clientBuilder.sslSocketFactory(getSSLSocketFactory());
					clientBuilder.hostnameVerifier(getHostnameVerifier());
					instance = new HttpClient(clientBuilder.build(), new Request.Builder());
				}
			}
		}
		return instance;
	}

	/**
	 * 设置请求头
	 * 
	 * @param params
	 * @return
	 */
	public HttpClient headers(Map<String, String> params) {
		Headers.Builder builder = new Headers.Builder();
		if (null != params) {
			Iterator<String> iterator = params.keySet().iterator();
			String key = "";
			while (iterator.hasNext()) {
				key = iterator.next().toString();
				builder.add(key, params.get(key));
			}
		}
		requestBuilder.headers(builder.build());

		return this;
	}

	/**
	 * GET请求
	 * 
	 * @return
	 */
	public HttpClient get(String url) {
		get(url, null);

		return this;
	}

	/**
	 * GET请求
	 * 
	 * @param params
	 * @return
	 */
	public HttpClient get(String url, Map<String, String> params) {
		if (null != params) {
			url = url.concat("?").concat(toUrlParams(params));
		}
		requestBuilder.url(url).get();

		return this;
	}

	/**
	 * GET请求
	 * 
	 * @param url
	 * @param bean
	 * @return
	 */
	public <T> HttpClient get(String url, T bean) {
		requestBuilder.url(url.concat("?").concat(toUrlParams(bean))).get();

		return this;
	}

	/**
	 * POST请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public HttpClient post(String url, Map<String, String> params) {
		requestBuilder.url(url).post(setRequestBody(params));

		return this;
	}

	/**
	 * POST请求
	 * 
	 * @param url
	 * @param bean
	 * @return
	 */
	public <T> HttpClient post(String url, T bean) {
		requestBuilder.url(url).post(setRequestBody(toMap(bean)));

		return this;
	}

	/**
	 * POST请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public HttpClient postForJson(String url, Map<String, String> params) {
		requestBuilder.url(url).post(RequestBody.create(MEDIA_TYPE_JSON, JacksonUtils.toJson(params)));

		return this;
	}

	/**
	 * POST请求
	 * 
	 * @param url
	 * @param bean
	 * @return
	 */
	public <T> HttpClient postForJson(String url, T bean) {
		requestBuilder.url(url).post(RequestBody.create(MEDIA_TYPE_JSON, JacksonUtils.toJson(bean)));

		return this;
	}

	/**
	 * PUT请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public HttpClient put(String url, Map<String, String> params) {
		requestBuilder.url(url).put(setRequestBody(params));

		return this;
	}

	/**
	 * PUT请求
	 * 
	 * @param url
	 * @param bean
	 * @return
	 */
	public <T> HttpClient put(String url, T bean) {
		requestBuilder.url(url).put(setRequestBody(toMap(bean)));

		return this;
	}

	/**
	 * PUT请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public HttpClient putForJson(String url, Map<String, String> params) {
		requestBuilder.url(url).put(RequestBody.create(MEDIA_TYPE_JSON, JacksonUtils.toJson(params)));

		return this;
	}

	/**
	 * PUT请求
	 * 
	 * @param url
	 * @param bean
	 * @return
	 */
	public <T> HttpClient putForJson(String url, T bean) {
		requestBuilder.url(url).put(RequestBody.create(MEDIA_TYPE_JSON, JacksonUtils.toJson(bean)));

		return this;
	}

	/**
	 * DELETE请求
	 * 
	 * @param url
	 * @return
	 */
	public HttpClient delete(String url) {
		requestBuilder.url(url).delete();

		return this;
	}

	/**
	 * DELETE请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public HttpClient delete(String url, Map<String, String> params) {
		requestBuilder.url(url).delete(setRequestBody(params));

		return this;
	}

	/**
	 * DELETE请求
	 * 
	 * @param url
	 * @param bean
	 * @return
	 */
	public <T> HttpClient delete(String url, T bean) {
		requestBuilder.url(url).delete(setRequestBody(toMap(bean)));

		return this;
	}

	/**
	 * DELETE请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public HttpClient deleteForJson(String url, Map<String, String> params) {
		requestBuilder.url(url).delete(RequestBody.create(MEDIA_TYPE_JSON, JacksonUtils.toJson(params)));

		return this;
	}

	/**
	 * DELETE请求
	 * 
	 * @param url
	 * @param bean
	 * @return
	 */
	public <T> HttpClient deleteForJson(String url, T bean) {
		requestBuilder.url(url).delete(RequestBody.create(MEDIA_TYPE_JSON, JacksonUtils.toJson(bean)));

		return this;
	}

	/**
	 * DELETE请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public HttpClient deleteForJson(String url, Object... params) {
		requestBuilder.url(url).delete(RequestBody.create(MEDIA_TYPE_JSON, JacksonUtils.toJson(params)));

		return this;
	}

	/**
	 * 执行http请求
	 * 
	 * @param responseType
	 *            返回类型
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public <T> T execute(Class<T> responseType) throws IOException {
		Request request = requestBuilder.build();
		Response response = this.okHttpClient.newCall(request).execute();
		String body = response.body().string();
		if (!response.isSuccessful()) {
			throw new IOException("请求失败: " + response);
		}
		if ("class java.math.BigDecimal".equals(responseType.toString())
				|| "class java.lang.Double".equals(responseType.toString())
				|| "class java.lang.Boolean".equals(responseType.toString())
				|| "class java.lang.Long".equals(responseType.toString())
				|| "class java.lang.Integer".equals(responseType.toString())
				|| "class java.lang.String".equals(responseType.toString())) {
			return (T) body;
		}

		return JacksonUtils.toObject(body, responseType);
	}

	/**
	 * 将对象转换为Map
	 * 
	 * @param obj
	 * @return
	 */
	private <T> Map<String, String> toMap(T obj) {
		Map<String, String> map = Maps.newHashMap();
		if (obj != null) {
			BeanMap beanMap = BeanMap.create(obj);
			for (Object key : beanMap.keySet()) {
				Object val = beanMap.get(key);
				if (null != val) {
					if (val instanceof Date) {
						map.put(String.valueOf(key), DateUtils.formatDate((Date) val, "yyyy-MM-dd HH:mm:ss"));
					} else {
						map.put(String.valueOf(key), String.valueOf(val));
					}
				}
			}
		}
		return map;
	}

	/**
	 * 将对象转换为URL参数
	 * 
	 * @param obj
	 * @return
	 */
	private <T> String toUrlParams(T obj) {
		String urlParams = "";
		if (null != obj) {
			BeanMap beanMap = BeanMap.create(obj);
			for (Object key : beanMap.keySet()) {
				Object val = beanMap.get(key);
				if (null != val) {
					if (val instanceof Date) {
						urlParams += String.valueOf(key) + "=" + DateUtils.formatDate((Date) val, "yyyy-MM-dd HH:mm:ss")
								+ "&";
					} else {
						urlParams += String.valueOf(key) + "=" + String.valueOf(val) + "&";
					}
				}
			}
			urlParams = StringUtils.removeEnd(urlParams, "&");
		}
		return urlParams;
	}

	/**
	 * 将Map转换为URL参数
	 * 
	 * @param map
	 * @return
	 */
	private String toUrlParams(Map<String, String> map) {
		String urlParams = "";
		if (null != map) {
			Iterator<String> iterator = map.keySet().iterator();
			String key = "";
			while (iterator.hasNext()) {
				key = iterator.next().toString();
				urlParams += key + "=" + map.get(key) + "&";
			}
			urlParams = StringUtils.removeEnd(urlParams, "&");
		}
		return urlParams;
	}

	/**
	 * 设置请求参数
	 * 
	 * @param params
	 * @return
	 */
	private RequestBody setRequestBody(Map<String, String> params) {
		FormBody.Builder builder = new FormBody.Builder();
		if (null != params) {
			Iterator<String> iterator = params.keySet().iterator();
			String key = "";
			while (iterator.hasNext()) {
				key = iterator.next().toString();
				builder.add(key, params.get(key));
			}
		}
		return builder.build();

	}

	private static SSLSocketFactory getSSLSocketFactory() {
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, getTrustManager(), new SecureRandom());
			return sslContext.getSocketFactory();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static TrustManager[] getTrustManager() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}
		} };
		return trustAllCerts;
	}

	private static HostnameVerifier getHostnameVerifier() {
		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			@Override
			public boolean verify(String s, SSLSession sslSession) {
				return true;
			}
		};
		return hostnameVerifier;
	}
}

package org.hsu.hsuapp.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class ApacheHttpRequester {

	public enum HttpResponseEnum {
		STATUS_CODE, ENTITY_CONTENT
	}

	protected static Logger logger = Logger.getLogger(ApacheHttpRequester.class);
	// 請求超時時間,這個時間定義了socket讀數據的超時時間，也就是連接到服務器之後到從服務器獲取響應數據需要等待的時間,發生超時，會拋出SocketTimeoutException異常。
	private static final int SOCKET_TIME_OUT = 60000;
	// 連接超時時間,這個時間定義了通過網絡與服務器建立連接的超時時間，也就是取得了連接池中的某個連接之後到接通目標url的連接等待時間。發生超時，會拋出ConnectionTimeoutException異常
	private static final int CONNECT_TIME_OUT = 60000;

	private HttpClientBuilder httpClientBuilder;
	private CloseableHttpClient closeableHttpClient;

	private String user = "";
	private String password = "";

	public ApacheHttpRequester() {
		super();
	}
	
	public ApacheHttpRequester(String user, String password) {
		super();
		this.user = user;
		this.password = password;
	}

	/**
	 * 
	 * @param url
	 * @param headers
	 * @param params
	 * @return
	 */
	public Map<ApacheHttpRequester.HttpResponseEnum, String> sendGET(String url, Map<String, Object> headers,
			Map<String, Object> params) {

		Map<ApacheHttpRequester.HttpResponseEnum, String> resultMap = new HashMap<ApacheHttpRequester.HttpResponseEnum, String>();
		CloseableHttpResponse response = null;
		HttpEntity entity = null;

		try {

			if (user != null && password != null) {
				createCloseableHttpClientWithBasicAuth(user, password);
			} else {
				createCloseableHttpClient();
			}

			HttpGet req = new HttpGet(url);

			// setConfig,添加配置,如設置請求超時時間,連接超時時間
			RequestConfig reqConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIME_OUT)
					.setConnectTimeout(CONNECT_TIME_OUT).build();
			req.setConfig(reqConfig);

			// setHeader,添加頭文件
			Set<String> keys = headers.keySet();
			for (String key : keys) {
				req.setHeader(key, headers.get(key).toString());
			}

			// 參數處理
			if (params != null) {
				StringBuffer param = new StringBuffer();
				int i = 0;
				for (String key : params.keySet()) {
					if (i == 0)
						param.append("?");
					else
						param.append("&");
					param.append(key).append("=").append(params.get(key));
					i++;
				}
				url += param;
			}

			response = closeableHttpClient.execute(req);
			entity = response.getEntity();
			String result = EntityUtils.toString(entity, "UTF-8");

			// 回傳結果
			resultMap.put(HttpResponseEnum.STATUS_CODE, String.valueOf(response.getStatusLine().getStatusCode()));
			resultMap.put(HttpResponseEnum.ENTITY_CONTENT, result);
		} catch (Exception e) {
			logger.error("sendGET error ", e);
			e.printStackTrace();
		} finally {
			try {
				EntityUtils.consume(entity);
				if (response != null)
					response.close();
				if (closeableHttpClient != null)
					closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return resultMap;
	}

	public Map<ApacheHttpRequester.HttpResponseEnum, String> sendPOST(String url, Map<String, Object> headers,
			Map<String, Object> param) {

		// 目前HttpClient最新版的實現類為CloseableHttpClient
		// CloseableHttpClient client = HttpClients.createDefault();
		Map<ApacheHttpRequester.HttpResponseEnum, String> resultMap = new HashMap<ApacheHttpRequester.HttpResponseEnum, String>();
		CloseableHttpResponse response = null;
		HttpEntity entity = null;

		try {
			if (user != null && password != null) {
				createCloseableHttpClientWithBasicAuth(user, password);
			} else {
				createCloseableHttpClient();
			}

			// 建立Request的對象，一般用目標url來構造，Request一般配置addHeader、setEntity、setConfig
			HttpPost req = new HttpPost(url);

			// setConfig,添加配置,如設置請求超時時間,連接超時時間
			RequestConfig reqConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIME_OUT)
					.setConnectTimeout(CONNECT_TIME_OUT).build();
			req.setConfig(reqConfig);

			// setHeader,添加頭文件
			Set<String> keys = headers.keySet();
			for (String key : keys) {
				req.setHeader(key, headers.get(key).toString());
			}

			// setEntity,添加內容
			if (param != null) {
				entity = new UrlEncodedFormEntity(createParam(param), Consts.UTF_8);
			}
			req.setEntity(entity);

			// 執行Request請求,CloseableHttpClient的execute方法返回的response都是CloseableHttpResponse類型
			// 其常用方法有getFirstHeader(String)、getLastHeader(String)、headerIterator（String）取得某個Header
			// name對應的叠代器、getAllHeaders()、getEntity、getStatus等
			response = closeableHttpClient.execute(req);
			entity = response.getEntity();

			// 用EntityUtils.toString()這個靜態方法將HttpEntity轉換成字符串,防止服務器返回的數據帶有中文,所以在轉換的時候將字符集指定成utf-8就可以了
			String result = EntityUtils.toString(entity, "UTF-8");

			// 回傳結果
			resultMap.put(HttpResponseEnum.STATUS_CODE, String.valueOf(response.getStatusLine().getStatusCode()));
			resultMap.put(HttpResponseEnum.ENTITY_CONTENT, result);

		} catch (Exception e) {
			logger.error("sendPOST error ", e);
			e.printStackTrace();
		} finally {
			// 一定要記得把entity fully consume掉，否則連接池中的connection就會一直處於占用狀態
			try {
				EntityUtils.consume(entity);
				if (response != null)
					response.close();
				if (closeableHttpClient != null)
					closeableHttpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return resultMap;
	}

	private static List<NameValuePair> createParam(Map<String, Object> param) {
		// 建立一個NameValuePair數組，用於存儲欲傳送的參數
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (param != null) {
			for (String k : param.keySet()) {
				nvps.add(new BasicNameValuePair(k, param.get(k).toString()));
			}
		}
		return nvps;
	}

	/**
	 * 
	 */
	public void createCloseableHttpClient() {
		// if (closeableHttpClient == null) {
		logger.debug("create closeableHttpClient...");
		// 創建HttpClientBuilder
		httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		closeableHttpClient = httpClientBuilder.build();
		// }
	}

	/**
	 * 
	 * @param user
	 * @param password
	 */
	public void createCloseableHttpClientWithBasicAuth(String user, String password) {
		// if (closeableHttpClient == null) {
		logger.debug("create closeableHttpClient... " + user + " / " + password);
		// 創建HttpClientBuilder
		httpClientBuilder = HttpClientBuilder.create();

		// Create the authentication scope
		AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
		// Create credential pair，在此處填寫用戶名和密碼
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, password);

		// 設置BasicAuth
		CredentialsProvider provider = new BasicCredentialsProvider();
		// Inject the credentials
		provider.setCredentials(scope, credentials);

		// Set the default credentials provider
		httpClientBuilder.setDefaultCredentialsProvider(provider);
		// HttpClient
		closeableHttpClient = httpClientBuilder.build();
		// }
	}

}

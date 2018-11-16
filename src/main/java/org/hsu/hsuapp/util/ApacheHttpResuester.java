package org.hsu.hsuapp.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


public class ApacheHttpResuester {

	public enum HttpResponseEnum {
		STATUS_CODE, ENTITY_CONTENT
	}

	private HttpClientBuilder httpClientBuilder;
	private CloseableHttpClient closeableHttpClient;

	/**
	 * 
	 * @param urlString
	 * @param user
	 * @param password
	 * @param charset
	 * @param params
	 * @return
	 */
	public Map<ApacheHttpResuester.HttpResponseEnum, String> sendBasicAuthGET(String urlString, String user,
			String password, String charset, Map<String, String> params) {
		return sendGET(urlString, user, password, charset, params);
	}

	private Map<ApacheHttpResuester.HttpResponseEnum, String> sendGET(String urlString, String user, String password,
			String charset, Map<String, String> params) {
		
		if (closeableHttpClient == null) {
			if (user != null && password != null) {
				createCloseableHttpClientWithBasicAuth(user, password);
			} else {
				createCloseableHttpClient();
			}
		}

		HttpGet httpGet = null;
		HttpResponse httpResponse = null;
		HttpEntity entity = null;
		String result = "";
		Map<ApacheHttpResuester.HttpResponseEnum, String> resultMap = new HashMap<ApacheHttpResuester.HttpResponseEnum, String>();

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
			urlString += param;
		}

		try {
			httpGet = new HttpGet(urlString);
			httpResponse = closeableHttpClient.execute(httpGet);
			entity = httpResponse.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity);
			}

			// 回傳結果
			resultMap.put(HttpResponseEnum.STATUS_CODE, String.valueOf(httpResponse.getStatusLine().getStatusCode()));
			resultMap.put(HttpResponseEnum.ENTITY_CONTENT, result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}

	private void sendPOST() {

	}

	/**
	 * 
	 */
	public void createCloseableHttpClient() {
		if (closeableHttpClient == null) {
			// 創建HttpClientBuilder
			httpClientBuilder = HttpClientBuilder.create();
			// HttpClient
			closeableHttpClient = httpClientBuilder.build();
		}
	}

	/**
	 * 
	 * @param user
	 * @param password
	 */
	public void createCloseableHttpClientWithBasicAuth(String user, String password) {
		if (closeableHttpClient == null) {
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
		}
	}

}

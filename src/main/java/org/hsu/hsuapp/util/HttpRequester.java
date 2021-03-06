package org.hsu.hsuapp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.http.HttpStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * HTTP請求對象
 * 
 * @author YYmmiinngg
 */
@Slf4j
public class HttpRequester {

	private String defaultContentEncoding;
	private String user;
	private String password;

	// Constant Key
	public static String HTTP_RESPONSE_CODE = "HttpResponseCode";
	public static String HTTP_RESPONSE_DATA = "HttpResponseData";

	public HttpRequester() {
		this.defaultContentEncoding = Charset.defaultCharset().name();
	}

	/**
	 * 取得response data
	 * 
	 * @param code
	 * @param content
	 * @return
	 */
	public Map<String, Object> getResponseMap(int code, String content) {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put(HTTP_RESPONSE_CODE, code);
		rtnMap.put(HTTP_RESPONSE_DATA, content);
		return rtnMap;
	}

	/**
	 * 發送GET請求
	 * 
	 * @param urlString URL地址
	 * @return 響應物件
	 * @throws IOException
	 */
	public Map<String, Object> sendGet(String urlString) throws IOException {
		return this.send(urlString, "GET", null, null);
	}

	/**
	 * 發送GET請求
	 * 
	 * @param urlString URL地址
	 * @param params    參數集合
	 * @return 響應物件
	 * @throws IOException
	 */
	public Map<String, Object> sendGet(String urlString, Map<String, String> params) throws IOException {
		return this.send(urlString, "GET", params, null);
	}

	/**
	 * 發送GET請求
	 * 
	 * @param urlString URL地址
	 * @param params    參數集合
	 * @param propertys 請求屬性
	 * @return 響應物件
	 * @throws IOException
	 */
	public Map<String, Object> sendGet(String urlString, Map<String, String> params, Map<String, String> propertys)
			throws IOException {
		return this.send(urlString, "GET", params, propertys);
	}

	/**
	 * 發送POST請求
	 * 
	 * @param urlString URL地址
	 * @return 響應物件
	 * @throws IOException
	 */
	public Map<String, Object> sendPost(String urlString) throws IOException {
		return this.send(urlString, "POST", null, null);
	}

	/**
	 * 發送POST請求
	 * 
	 * @param urlString URL地址
	 * @param params    參數集合
	 * @return 響應物件
	 * @throws IOException
	 */
	public Map<String, Object> sendPost(String urlString, Map<String, String> params) throws IOException {
		return this.send(urlString, "POST", params, null);
	}

	/**
	 * 發送POST請求
	 * 
	 * @param urlString URL地址
	 * @param params    參數集合
	 * @param charset   編碼
	 * @return 響應物件
	 * @throws IOException
	 */
	public Map<String, Object> sendPost(String urlString, Map<String, String> params, String charset)
			throws IOException {
		return this.send(urlString, "POST", params, null, charset);
	}

	/**
	 * 發送POST請求
	 * 
	 * @param urlString URL地址
	 * @param params    參數集合
	 * @param propertys 請求屬性
	 * @return 響應物件
	 * @throws IOException
	 */
	public Map<String, Object> sendPost(String urlString, Map<String, String> params, Map<String, String> propertys)
			throws IOException {
		return this.send(urlString, "POST", params, propertys);
	}

	/**
	 * 發送POST請求
	 * 
	 * @param urlString  URL地址
	 * @param method     請求方式
	 * @param parameters 參數集合
	 * @param propertys  請求屬性
	 * @return 響應物件
	 * @throws IOException
	 */
	private Map<String, Object> send(String urlString, String method, Map<String, String> parameters,
			Map<String, String> propertys) throws IOException {
		return this.send(urlString, method, parameters, propertys, "UTF-8");
	}

	/**
	 * 發送HTTP請求
	 * 
	 * @param urlString
	 * @return 響映對象
	 * @throws IOException
	 */
	private Map<String, Object> send(String urlString, String method, Map<String, String> parameters,
			Map<String, String> propertys, String charset) {

		HttpURLConnection urlConnection = null;
		try {
			if (method.equalsIgnoreCase("GET") && parameters != null) {
				StringBuffer param = new StringBuffer();
				int i = 0;
				for (String key : parameters.keySet()) {
					if (i == 0)
						param.append("?");
					else
						param.append("&");
					param.append(key).append("=").append(parameters.get(key));
					i++;
				}
				urlString += param;
			}
			URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod(method);
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + charset);

			if (user != null & password != null) {
				byte[] message = (user + ":" + password).getBytes("UTF-8");
				String encoded = javax.xml.bind.DatatypeConverter.printBase64Binary(message);
				urlConnection.setRequestProperty("Authorization", "Basic " + encoded);
				clearUser();
			}

			if (propertys != null)
				for (String key : propertys.keySet()) {
					urlConnection.addRequestProperty(key, propertys.get(key));
				}

			if (method.equalsIgnoreCase("POST") && parameters != null) {
				StringBuffer param = new StringBuffer();
				for (String key : parameters.keySet()) {
					param.append("&");
					param.append(key).append("=").append(parameters.get(key));
				}
				urlConnection.getOutputStream().write(param.toString().getBytes("UTF-8"));
				urlConnection.getOutputStream().flush();
				urlConnection.getOutputStream().close();
			}

			return this.makeContent(urlConnection);
		} catch (Exception e) {
			e.printStackTrace();
			return getResponseMap(HttpStatus.SC_INTERNAL_SERVER_ERROR, "");
		}
	}

	/**
	 * 
	 * @param user
	 * @param password
	 */
	public void setUser(String user, String password) {
		this.user = user;
		this.password = password;
	}

	/**
	 * 
	 */
	public void clearUser() {
		this.user = null;
		this.password = null;
	}

	/**
	 * 發送HTTP請求
	 * 
	 * @param urlString
	 * @return 響映對象
	 * @throws IOException
	 */
	public Map<String, Object> sendAppJson(String urlString, String strJson, String charset) {

		HttpURLConnection urlConnection = null;

		try {
			URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod("POST");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			urlConnection.addRequestProperty("Content-Type", "application/json; charset=" + charset);

			StringBuffer sbParam = new StringBuffer();
			sbParam.append(strJson);
			urlConnection.getOutputStream().write(sbParam.toString().getBytes("UTF-8"));
			urlConnection.getOutputStream().flush();
			urlConnection.getOutputStream().close();

			return this.makeContent(urlConnection);
		} catch (Exception e) {
			e.printStackTrace();
			return getResponseMap(HttpStatus.SC_INTERNAL_SERVER_ERROR, "");
		}

	}

	/**
	 * 得到回應物件
	 * 
	 * @param urlConnection
	 * @return 響應物件
	 * @throws IOException
	 */
	private Map<String, Object> makeContent(HttpURLConnection urlConnection) throws IOException {

		int code = HttpStatus.SC_OK;
		String content = "";
		try {
			code = urlConnection.getResponseCode();
			if (HttpStatus.SC_OK == code) {
				InputStream in = urlConnection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
				Vector<String> contentCollection = new Vector<String>();

				StringBuffer temp = new StringBuffer();
				String line = bufferedReader.readLine();
				while (line != null) {
					contentCollection.add(line);
					temp.append(line).append("\r\n");
					line = bufferedReader.readLine();
				}
				bufferedReader.close();

				String ecod = urlConnection.getContentEncoding();
				if (ecod == null)
					ecod = this.defaultContentEncoding;

				content = new String(temp.toString().getBytes(), ecod);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}

		return getResponseMap(code, content);
	}

	/**
	 * 默認的回應字元集
	 */
	public String getDefaultContentEncoding() {
		return this.defaultContentEncoding;
	}

	/**
	 * 設置默認的回應字元集
	 */
	public void setDefaultContentEncoding(String defaultContentEncoding) {
		this.defaultContentEncoding = defaultContentEncoding;
	}

	public static void main(String[] args) {
		try {
			Map<String, String> params = new HashMap<String, String>();
//			params.put("url", "www.google.com");
//			params.put("title", "建國測試008");
//			params.put("owner", "TESTPORTAL003");
			HttpRequester request = new HttpRequester();
			// request.defaultContentEncoding = "UTF-8";
			// String hr = request.sendGet("http://203.64.154.30/AdminPortal/addTodolist",
			// params);
			Map<String, Object> hr = request.sendGet(
					"http://api.fanyi.baidu.com/api/trans/vip/translate?q=apple&from=en&to=zh&appid=2015063000000001&salt=1435660288&sign=f89f9594663708c1605f3d736d01d2d4",
					params);
			System.out.println("============: " + hr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.hsu.hsuapp.util;

import java.util.HashMap;
import java.util.Map;

import org.hsu.hsuapp.util.HttpRequester;
import org.junit.Before;

public class HttpRequesterTest {

	private String JBPM_HOST = "http://127.0.0.1:8080/jbpm-console";
	private String REST_QUERYTASK_URL = JBPM_HOST + "/rest/query/task";
	private String LOGOUT_URL = JBPM_HOST + "/logout.jsp";

	HttpRequester httpRequester = null;

	@Before
	public void init() {
		httpRequester = new HttpRequester();
	}

	// @Test
	public void test() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("s", "2"); // 每頁筆數
			params.put("p", "1"); // 頁數

			String user = "jack";
			String password = "1234";

			HttpRequester request = new HttpRequester();
			request.setUser(user, password);

			Map hr = request.sendGet(REST_QUERYTASK_URL, params);
			System.out.println(hr);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

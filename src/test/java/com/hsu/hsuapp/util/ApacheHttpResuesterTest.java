package com.hsu.hsuapp.util;

import java.util.HashMap;
import java.util.Map;

import org.hsu.hsuapp.util.ApacheHttpRequester;
import org.hsu.hsuapp.util.ApacheHttpRequester.HttpResponseEnum;
import org.junit.Before;
import org.junit.Test;

public class ApacheHttpResuesterTest {

	ApacheHttpRequester apacheHttpResuester;

	@Before
	public void init() {
		apacheHttpResuester = new ApacheHttpRequester();
	}

	@Test
	public void sendGET() {
		String url = "http://127.0.0.1:8080/jbpm-console/rest/query/task";
		String user = "jack";
		String password = "jack";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", "Reserved");

		Map<HttpResponseEnum, String> rtnMap = apacheHttpResuester.sendGET(url, null, params);
		String status_code = rtnMap.get(HttpResponseEnum.STATUS_CODE);
		String entity_content = rtnMap.get(HttpResponseEnum.ENTITY_CONTENT);

		System.out.println(status_code);
		System.out.println(entity_content);

	}

}

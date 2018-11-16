package com.hsu.hsuapp.util;

import java.util.HashMap;
import java.util.Map;

import org.hsu.hsuapp.util.ApacheHttpResuester;
import org.hsu.hsuapp.util.ApacheHttpResuester.HttpResponseEnum;
import org.junit.Before;
import org.junit.Test;

public class ApacheHttpResuesterTest {

	ApacheHttpResuester apacheHttpResuester;
	
	@Before
	public void init(){
		apacheHttpResuester = new ApacheHttpResuester();
	}
	
	@Test
	public void sendGET() {
		String url = "http://127.0.0.1:8080/jbpm-console/rest/query/task";
		String user = "jack";
		String password = "jack";
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("status", "Reserved");
		
		Map<HttpResponseEnum, String> rtnMap = apacheHttpResuester.sendBasicAuthGET(url, user, password, null, params);
		String status_code = rtnMap.get(HttpResponseEnum.STATUS_CODE);
		String entity_content = rtnMap.get(HttpResponseEnum.ENTITY_CONTENT);
		
		System.out.println(status_code);
		System.out.println(entity_content);
		
		
		
	}
	
}

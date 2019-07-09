package org.hsu.hsuapp.util;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {

	/**
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> jsonToMap(String jsonStr) {

		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> rtnMap = null;

		try {
			rtnMap = mapper.readValue(jsonStr, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rtnMap;
	}

	/**
	 * 
	 * @param jsonString
	 * @param prototype
	 * @return
	 */
	public static <T> T getEntity(String jsonString, Class<T> prototype) {

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return (T) objectMapper.readValue(jsonString, prototype);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static String getJson(Object obj) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}
	
	
	
}

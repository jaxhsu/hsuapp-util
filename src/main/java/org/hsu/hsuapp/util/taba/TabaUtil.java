package org.hsu.hsuapp.util.taba;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hsu.hsuapp.util.FileUtil;
import org.hsu.hsuapp.util.JacksonUtil;
import org.hsu.hsuapp.util.MapUtil;

public class TabaUtil {

	private static String SCRIPT_FILE_PATH = "C:\\Users\\USER\\Downloads\\taba";
	private static String SCRIPT_FILE_NAME = "test.js";
	private static String[] REMOVE_CHAR = new String[] { "#", "　", " " };
	
	/**
	 * 顯示 cxx 內容
	 * 
	 * @param dataList
	 */
	public static void show_cxx_data(List<Map<String,Object>> dataList) {
		for (int idx = 0; idx < dataList.size(); idx++) {
			show_cxx_data(dataList.get(idx));
		}		
	}
	
	/**
	 * 顯示 cxx 內容
	 * 
	 * @param dataMap
	 */
	public static void show_cxx_data(Map<String, Object> dataMap) {
		String id = MapUtil.getString(dataMap, "id", "");
		System.out.println("id=" + id);

		List<Map<String, Object>> script_data = dataMap.get("script") != null ? (List) dataMap.get("script")
				: new LinkedList<Map<String, Object>>();
		System.out.println("script size=" + script_data.size());

		for (int idx = 0; idx < script_data.size(); idx++) {
			show_script_data(script_data.get(idx));
		}
	}

	/**
	 * 顯示 cxx中 script 的內容
	 * 
	 * @param dataMap
	 */
	public static void show_script_data(Map<String, Object> dataMap) {

		String type = MapUtil.getString(dataMap, "type", "");
		String name = MapUtil.getString(dataMap, "name", "");
		String text = MapUtil.getString(dataMap, "text", "");

		if ("TXT".equals(type)) {
			System.out.println("type=" + type);
			System.out.println("name=" + name);
			System.out.println("text=" + remove_char(text));
		}
	}
	
	/**
	 * 移除影響翻譯的字符
	 * 
	 * @param str
	 * @return
	 */
	public static String remove_char(String str) {
		for (int i = 0; i < REMOVE_CHAR.length; i++) {
			str = str.replaceAll(REMOVE_CHAR[i], "");
		}
		return str;
	}
	
	public static void main(String[] args) {
		
		String charsetName = "UTF-8";
		File f = new File(SCRIPT_FILE_PATH, SCRIPT_FILE_NAME);
		
		StringBuilder sb = FileUtil.readFile(f.getAbsolutePath(), charsetName);
		
//		ScriptJsonData result = JacksonUtil.getEntity(sb.toString(), ScriptJsonData.class);
//		System.out.println(result.getData().size());
		
		Map<String, Object> dataMap = JacksonUtil.jsonToMap(sb.toString());
		List<Object> list = dataMap.get("data") != null ? (List) dataMap.get("data")
				: new LinkedList<Object>();
		
		System.out.println("list.size()=" + list.size());
		
		for (int idx = 0; idx < list.size(); idx++) {
			Object obj = list.get(idx);
			if (obj instanceof Map) {
				show_cxx_data((Map) obj);
			}
			else if (obj instanceof List) {
				show_cxx_data((List) obj);
			} 
			else {
				System.out.println("不是map or list , idx=" + idx);
			}
		}
		
	}	
}

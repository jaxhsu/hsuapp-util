package org.hsu.hsuapp.trans.taba;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hsu.hsuapp.baidu.BaiduTransApi;
import org.hsu.hsuapp.baidu.TransJsonResult;
import org.hsu.hsuapp.trans.AutoGenTrans;
import org.hsu.hsuapp.util.FileUtil;
import org.hsu.hsuapp.util.HttpRequester;
import org.hsu.hsuapp.util.JacksonUtil;
import org.hsu.hsuapp.util.MapUtil;
import org.hsu.hsuapp.util.StringUtil;

public class TabaUtil {
	
	private static String GAME_PATH = "D:\\taba";
	private static String SCRIPT_FILE_NAME = "test.js";
	private static String OUTPUT_FILE_NAME = "_out.txt";
	
	private static String[] REMOVE_CHAR = new String[] { "#", "　", " ", "=" };
	
	private static AutoGenTrans autoGenTrans;
	
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
			
			String t = remove_char(text);
			System.out.println("text=" + t);
			if (!StringUtil.isBlank(t)) {
				t = trans(t);
				dataMap.put("text", t);
			}
			System.out.println("text trans=" + t);
			
		}
	}
	
	/**
	 * 翻譯內容
	 * 
	 * @param from
	 * @return
	 */
	public static String trans(String q) {
		boolean needUpdateTmpData = false;
		String trans = "";
		String trans_tmp = "";
		// 查詢暫存檔
		trans_tmp = autoGenTrans.query(q);
		// 百度翻譯
		if (StringUtil.isBlank(trans_tmp)) {
			trans_tmp = baiduTrans(q);
			if (!StringUtil.isBlank(trans_tmp))
				needUpdateTmpData = true;
		}
		// 更新暫存檔
		if (needUpdateTmpData) {
			autoGenTrans.write(q, trans_tmp);
		}
		
		if (StringUtil.isBlank(trans_tmp)) {
			trans = "";
		} else {
			trans = trans_tmp;
		}
		
		return trans;
	}

	/**
	 * 百度翻譯
	 * 
	 * @param q
	 * @return
	 */
	public static String baiduTrans(String q) {
		String q_trans = "";

		String appid = "20160515000021090";
		String securityKey = "PZtwy_mAh5WdXfsDC_J8";
		String from = "jp";
		String to = "cht";
		
		//HttpRequester request = new HttpRequester();		
		BaiduTransApi BaiduTransApi = new BaiduTransApi(appid, securityKey);		
		
		String hr = "";
		try {
			System.out.println("百度翻譯中...");
			hr = BaiduTransApi.getTransResult(q, from, to);
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransJsonResult transJsonResult = JacksonUtil.getEntity(hr, TransJsonResult.class);
		if (StringUtil.isBlank(transJsonResult.getErrorCode())) {
			q_trans = transJsonResult.getTransResult().get(0).getDst();
			if(!StringUtil.isBlank(q_trans)) {
				System.out.println("翻譯成功...");
			}else {
				System.out.println("翻譯失敗..." + transJsonResult);
				q_trans = "";
			}
		} else {
			System.out.println("翻譯失敗..." + transJsonResult.getErrorCode());
		}
		
		return q_trans;
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
		File f = new File(GAME_PATH, SCRIPT_FILE_NAME);
		autoGenTrans = new AutoGenTrans(GAME_PATH);
		autoGenTrans.load();
		
		StringBuilder sb = FileUtil.readFile(f.getAbsolutePath(), charsetName);
		
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
		
		// 寫檔
		File of = new File(GAME_PATH, OUTPUT_FILE_NAME);
		FileUtil.writeFile(of.getAbsolutePath(), JacksonUtil.getJson(list));
	}	
}

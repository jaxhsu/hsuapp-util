package org.hsu.hsuapp.baidu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.hsu.hsuapp.util.HttpRequester;
import org.hsu.hsuapp.util.JacksonUtil;
import org.hsu.hsuapp.util.MapUtil;

public class BaiduTransApi {

	private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

	private String appid;
	private String securityKey;

	public BaiduTransApi(String appid, String securityKey) {
		this.appid = appid;
		this.securityKey = securityKey;
	}

	public Map<String, Object> getTransResult(String query, String from, String to) throws Exception {
		Map<String, String> params = buildParams(query, from, to);
		HttpRequester request = new HttpRequester();
		return request.sendGet(TRANS_API_HOST, params);
	}

	private Map<String, String> buildParams(String query, String from, String to) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("q", query);
		params.put("from", from);
		params.put("to", to);

		params.put("appid", appid);

		// 隨機數
		String salt = String.valueOf(System.currentTimeMillis());
		params.put("salt", salt);

		// 簽名
		String src = appid + query + salt + securityKey; // 加密前的原文
		params.put("sign", MD5.md5(src));

		return params;
	}

	public static void main(String[] args) {

		JacksonUtil jacksonUtil = new JacksonUtil();
		HttpRequester request = new HttpRequester();
		String json_str = "{\"from\":\"en\",\"to\":\"zh\",\"trans_result\":[{\"src\":\"apple\",\"dst\":\"\u82f9\u679c\"}]}";
		String baidu_trans_url = "http://api.fanyi.baidu.com/api/trans/vip/translate";

		String appid = "20160515000021090";
		String securityKey = "PZtwy_mAh5WdXfsDC_J8";
		BaiduTransApi BaiduTransApi = new BaiduTransApi(appid, securityKey);

		String q = "終わりのない調教の日々、繰り返されるアナルレイプ、処女であるにも関わらず覚え込まされた子宮快楽。";
		q = q.replaceAll("#", "");

		String from = "jp";
		String to = "cht";

		// 測試範例
//		String q = "apple";
//		String from = "en";
//		String to = "zh";		
//		String appid = "2015063000000001";
//		String salt = "1435660288";
//		String sign = "f89f9594663708c1605f3d736d01d2d4";
//		Map<String, String> param1 = new HashMap<String, String>();
//		param1.put("q", q);
//		param1.put("from", from);
//		param1.put("to", to);
//		param1.put("appid", appid);
//		param1.put("salt", salt);
//		param1.put("sign", sign);

		// 參數範例
		Map<String, String> param2 = BaiduTransApi.buildParams(q, from, to);
		System.out.println(param2);

		String hr = "";
		try {
			Map dataMap = request.sendGet(baidu_trans_url, param2);
			hr = MapUtil.getString(dataMap, HttpRequester.HTTP_RESPONSE_DATA);
		} catch (IOException e) {
			e.printStackTrace();
		}

		TransJsonResult transJsonResult = jacksonUtil.getEntity(hr, TransJsonResult.class);
		System.out.println(transJsonResult);

	}

}

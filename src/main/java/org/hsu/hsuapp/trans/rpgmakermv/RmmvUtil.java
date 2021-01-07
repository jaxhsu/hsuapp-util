package org.hsu.hsuapp.trans.rpgmakermv;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hsu.hsuapp.baidu.BaiduTransApi;
import org.hsu.hsuapp.baidu.TransJsonResult;
import org.hsu.hsuapp.trans.AutoGenTrans;
import org.hsu.hsuapp.trans.rpgmakermv.pojo.CommonEvents.CommonEventsList;
import org.hsu.hsuapp.trans.rpgmakermv.pojo.CommonEvents.CommonEventsRoot;
import org.hsu.hsuapp.util.FileUtil;
import org.hsu.hsuapp.util.HttpRequester;
import org.hsu.hsuapp.util.JacksonUtil;
import org.hsu.hsuapp.util.StringUtil;

public class RmmvUtil {

	public static void main(String[] args) {
		// 時間
		long time1, time2;
		// 檔案
		FileUtil fUtil = new FileUtil();
		String fpath = "D:\\tmp\\data_1_test";
		String commonEvent_fpath = new File(fpath, "CommonEvents.json").getAbsolutePath();
		// 翻譯紀錄
		AutoGenTrans autoGenTrans = new AutoGenTrans(fpath);
		autoGenTrans.load();
		// 百度翻譯
		String appid = "20160515000021090";
		String securityKey = "PZtwy_mAh5WdXfsDC_J8";
		String from = "jp";
		String to = "cht";
		BaiduTransApi BaiduTransApi = new BaiduTransApi(appid, securityKey);
		// 讀檔
		String jsonStr = fUtil.readFileContent(commonEvent_fpath);
		// System.out.println(jsonStr);

		CommonEventsRoot[] roots = JacksonUtil.getEntityArray(jsonStr, CommonEventsRoot[].class);
		// System.out.println(roots);

		time1 = System.currentTimeMillis();

		// 翻譯
		CommonEventsRoot[] trans_root = Arrays.stream(roots).map(root -> {

			CommonEventsRoot croot = new CommonEventsRoot();
			croot.setId(root.getId());
			croot.setName(root.getName());
			croot.setSwitchId(root.getSwitchId());
			croot.setTrigger(root.getTrigger());

			List<CommonEventsList> _list = root.getList().stream().map(datalist -> {
				CommonEventsList clist = new CommonEventsList();
				clist.setCode(datalist.getCode());
				clist.setIndent(datalist.getIndent());
				clist.setParameters(datalist.getParameters().stream().map(obj -> {
					// 執行翻譯
					if (obj instanceof String) {
						String _str = (String) obj;
						if (StringUtil.isJapanese1(_str)) {
							String _src_str = _str;
							String _query_history_str = autoGenTrans.query(_str);//查詢歷史翻譯紀錄
							if (StringUtil.isBlank(_query_history_str)) {
								//查詢百度API
//								String _query_baidu_str = baiduTrans(BaiduTransApi, _str, from, to);
//								if (!StringUtil.isBlank(_query_baidu_str)) {
//									autoGenTrans.write(_src_str, _query_baidu_str);
//									_str = _query_baidu_str;
//								}
							} else {
								//使用歷史翻譯紀錄
								_str = _query_history_str;
							}
							return _str;
						} else {
							return obj;
						}
					} else {
						return obj;
					}
				}).collect(Collectors.toList()));
				return clist;
			}).collect(Collectors.toList());

			croot.setList(_list);

			return croot;
		}).toArray(CommonEventsRoot[]::new);

		time2 = System.currentTimeMillis();
		System.out.println("doSomething()花了：" + (time2 - time1) + "豪秒");

		// 顯示內容
//		Arrays.stream(trans_root).forEach(root -> {
//			root.getList().forEach(datalist -> {
//				datalist.getParameters().forEach(System.out::println);
//			});
//		});

		// 產檔
		System.out.println(JacksonUtil.getJson(trans_root));

	}

	public static String baiduTrans(BaiduTransApi BaiduTransApi, String q, String from, String to) {
		String q_trans = "";
		HttpRequester request = new HttpRequester();
		String hr = "";
		try {
			System.out.println("百度翻譯中...q=" + q);
			hr = BaiduTransApi.getTransResult(q, from, to);
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			TransJsonResult transJsonResult = JacksonUtil.getEntity(hr, TransJsonResult.class);
			if (StringUtil.isBlank(transJsonResult.getErrorCode())) {
				q_trans = transJsonResult.getTransResult().get(0).getDst();
				if (!StringUtil.isBlank(q_trans)) {
					System.out.println("翻譯成功...");
				} else {
					System.out.println("翻譯失敗..." + transJsonResult);
					q_trans = "";
				}
			} else {
				System.out.println("翻譯失敗..." + transJsonResult.getErrorCode());
			}			
		} catch (Exception e) {
			System.out.println("轉換失敗...hr=" + hr);
			e.printStackTrace();
		}
		//字串替換
		q_trans = q_trans.replaceAll("“", "「");
		q_trans = q_trans.replaceAll("”", "」");
		
		return q_trans;
	}

}

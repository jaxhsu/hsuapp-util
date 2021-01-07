package org.hsu.hsuapp.trans.rpgmakermv;

import java.io.File;
import java.util.Arrays;

import org.hsu.hsuapp.trans.rpgmakermv.pojo.CommonEvents.CommonEventsRoot;
import org.hsu.hsuapp.util.FileUtil;
import org.hsu.hsuapp.util.JacksonUtil;

public class Test {
	public static void main(String[] args) {
		// 檔案
		FileUtil fUtil = new FileUtil();
		String fpath = "D:\\tmp\\data_1_test";
		String commonEvent_fpath = new File(fpath, "CommonEvents.json").getAbsolutePath();
		// 讀檔
		String jsonStr = fUtil.readFileContent(commonEvent_fpath);
		// System.out.println(jsonStr);
		CommonEventsRoot[] roots = JacksonUtil.getEntityArray(jsonStr, CommonEventsRoot[].class);
		Arrays.stream(roots).forEach(data->{
			data.getList().stream().forEach(datalist->{
				datalist.getParameters().stream()
				.filter(obj -> obj instanceof String)
				.map(obj -> (String) obj)
				.filter(str -> str.contains("「うわっ・・・なんだこれ・・"))
				.forEach(System.out::println);
			});
		});
		
//		File outputFile = new File(fpath, "222.txt");
//		FileUtil.writeFile(outputFile.getAbsolutePath(), jsonStr);
	}
}

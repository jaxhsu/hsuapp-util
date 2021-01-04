package org.hsu.hsuapp.util.rpgmakermv;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hsu.hsuapp.util.FileUtil;
import org.hsu.hsuapp.util.JacksonUtil;
import org.hsu.hsuapp.util.rpgmakermv.pojo.CommonEvents.CommonEventsList;
import org.hsu.hsuapp.util.rpgmakermv.pojo.CommonEvents.CommonEventsRoot;

public class TransUtil {

	public static void main(String[] args) {
		FileUtil fUtil = new FileUtil();
		String fpath = "C:\\Users\\bluepobo\\Downloads\\Compressed\\Legendery Strangers-1.00\\Legendery Strangers-1.00\\src\\www\\data_test\\CommonEvents.json";
		String jsonStr = fUtil.readFileContent(fpath);

		// System.out.println(jsonStr);

		CommonEventsRoot[] roots = JacksonUtil.getEntityArray(jsonStr, CommonEventsRoot[].class);
		System.out.println(roots);
		
		Arrays.stream(roots).forEach(root -> {
			root.getList().stream().forEach(datalist -> {
				datalist.getParameters().stream().filter(a -> a instanceof String).forEach(System.out::println);
			});
		});
		
	}

}

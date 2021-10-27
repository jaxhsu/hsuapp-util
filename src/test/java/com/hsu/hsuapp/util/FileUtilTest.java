package com.hsu.hsuapp.util;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.hsu.hsuapp.util.FileUtil;
import org.hsu.hsuapp.util.ZipUtil;
import org.junit.Before;
import org.junit.Test;

public class FileUtilTest {

	FileUtil fileUtil;

	@Before
	public void init() {
		fileUtil = new FileUtil();
	}

	// @Test
	public void test() {
		try {
			FileUtils.write(new File("D:/cxyapi.txt"), "程序换api", "UTF-8", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test02() {
		try {
			List<Map<String, String>> sendFiles = new LinkedList<Map<String, String>>();
			String baseFolder = "D:/vpmdat/vpmadm";
			String srcFolder = "apply/110102N0002";
			String srcPattern = ".*";

			FileUtil.findFiles(sendFiles, baseFolder, srcFolder, srcPattern);

			for (Map<String, String> map : sendFiles) {
				System.out.println("map=" + map);
			}
			
			String destZipFilePath = "D:\\temp\\Test.zip";
			if (ZipUtil.createZipFile(sendFiles, destZipFilePath)) {
				System.out.println("產檔success");

				String destDirPath = "D:\\temp\\unzip";
				if (ZipUtil.unzip(destZipFilePath, destDirPath)) {
					System.out.println("解壓成功");
				} else {
					System.out.println("解壓失敗");
				}
			} else {
				System.out.println("產檔fail");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

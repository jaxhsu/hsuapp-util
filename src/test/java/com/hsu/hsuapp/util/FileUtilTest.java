package com.hsu.hsuapp.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hsu.hsuapp.util.FileUtil;
import org.junit.Before;
import org.junit.Test;

public class FileUtilTest {

	FileUtil fileUtil;
	
	@Before
	public void init() {
		fileUtil = new FileUtil();
	}
	
	@Test
	public void test(){
		try {
			FileUtils.write(new File("D:/cxyapi.txt"), "程序换api", "UTF-8", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

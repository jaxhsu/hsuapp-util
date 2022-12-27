package com.hsu.hsuapp.util;

import java.io.File;

import org.hsu.hsuapp.util.FileUtil;
import org.hsu.hsuapp.util.GzipUtil;
import org.junit.Test;

public class GzipUtilTest {
	
	FileUtil fileUtil = new FileUtil();
	GzipUtil gzipUtil = new GzipUtil();
	
	@Test
	public void compress() {
		// 讀取
		String filePath = "D:\\tmp\\gzip\\cut2.log";
		System.out.println("來源檔案=" + filePath);
		String fileContent = fileUtil.readFileContent(filePath, null, "\r\n", 1024);

		// 壓縮
		System.out.println("開始壓縮...");
		String compressFileContent = gzipUtil.compress(fileContent);
		System.out.println("壓縮完成...");

		// 寫入
		String outFilePath = "D:\\tmp\\gzip\\compress\\" + new File(filePath).getName() + "_compress.txt";
		System.out.println("目的檔案=" + outFilePath);
		boolean outFile_boolean = fileUtil.writeFile(outFilePath, compressFileContent);
		System.out.println("產壓縮檔=" + outFile_boolean);
	}

	@Test
	public void uncompress() {
		// 讀取
		String filePath = "D:\\tmp\\gzip\\compress\\cut2.log_compress.txt";
		System.out.println("來源檔案=" + filePath);
		String fileContent = fileUtil.readFileContent(filePath);
		
		// 解壓縮
		System.out.println("開始解壓縮...");
		String uncompressFileContent = gzipUtil.uncompress(fileContent);
		System.out.println("解壓縮完成...");
		
		// 寫入
		String outFilePath = "D:\\tmp\\gzip\\uncompress\\a.txt";
		System.out.println("目的檔案=" + outFilePath);
		boolean outFile_boolean = fileUtil.writeFile(outFilePath, uncompressFileContent);
		System.out.println("解壓縮檔=" + outFile_boolean);
		
	}
	
}

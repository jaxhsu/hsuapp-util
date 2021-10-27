package org.hsu.hsuapp.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class ZipUtil {

	protected static Logger logger = Logger.getLogger(ZipUtil.class);

	/**
	 * 壓縮zip
	 * 
	 * @param sendFiles
	 * @param destZipFilePath
	 * @return
	 */
	public static boolean createZipFile(List<Map<String, String>> sendFiles, String destZipFilePath) {
		boolean status = false;

		try {
			Map<String, String> sentMap = new HashMap<String, String>();
			File destZipFile = new File(destZipFilePath);
			if (destZipFile.exists()) {
				destZipFile.delete();
			} else {
				destZipFile.getParentFile().mkdirs();
			}
			logger.debug("create zip file：" + destZipFile.getAbsolutePath());

			CheckedOutputStream csum = new CheckedOutputStream(new FileOutputStream(destZipFile), new CRC32());
			ZipOutputStream zipOutStream = new ZipOutputStream(new BufferedOutputStream(csum));
			for (Map<String, String> fMap : sendFiles) {
				String filePath = fMap.get("BASE") + "/" + fMap.get("PATH");
				if (sentMap.containsKey(filePath)) { // 避免重複
					continue;
				}
				logger.debug("add to zip：" + fMap.get("PATH"));

				File fileIn = new File(fMap.get("BASE"), fMap.get("PATH"));
				FileInputStream fileInStream = new FileInputStream(fileIn);

				ZipEntry zipEntry = new ZipEntry(fMap.get("PATH")); // 存放至zip中的目錄位置
				zipEntry.setTime(fileIn.lastModified());
				zipEntry.setSize(fileIn.length());
				zipOutStream.putNextEntry(zipEntry);

				int inLen = 0;
				byte[] bytBuf = new byte[20480];
				while ((inLen = fileInStream.read(bytBuf)) != -1) {
					zipOutStream.write(bytBuf, 0, inLen);
				}
				fileInStream.close();
				sentMap.put(filePath, "Y");
			}
			zipOutStream.close();
			csum.close();

			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return status;
	}

	/**
	 * 解壓zip
	 * 
	 * @param unZipFile
	 * @param destDirPath	儲存目錄
	 * @return
	 */
	public static boolean unzip(String unZipFile, String destDirPath) {
		boolean flag = false;
		String fileZip = unZipFile;
		File destDir = new File(destDirPath);

		ZipInputStream zis = null;
		ZipEntry zipEntry = null;
		try {
			byte[] buffer = new byte[1024];
			zis = new ZipInputStream(new FileInputStream(fileZip));
			zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				while (zipEntry != null) {
					File newFile = newFile(destDir, zipEntry);
					if (zipEntry.isDirectory()) {
						if (!newFile.isDirectory() && !newFile.mkdirs()) {
							throw new IOException("Failed to create directory " + newFile);
						}
					} else {
						// fix for Windows-created archives
						File parent = newFile.getParentFile();
						if (!parent.isDirectory() && !parent.mkdirs()) {
							throw new IOException("Failed to create directory " + parent);
						}

						// write file content
						FileOutputStream fos = new FileOutputStream(newFile);
						int len;
						while ((len = zis.read(buffer)) > 0) {
							fos.write(buffer, 0, len);
						}
						fos.close();
					}
					zipEntry = zis.getNextEntry();
				}
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			try {
				zis.closeEntry();
				zis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return flag;
	}

	/**
	 * 
	 * @param destinationDir
	 * @param zipEntry
	 * @return File
	 * @throws IOException
	 */
	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}

}

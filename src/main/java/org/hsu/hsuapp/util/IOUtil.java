package org.hsu.hsuapp.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOUtil {

	private IOUtil() {
		
	}

	/**
	 * Close closable object and wrap {@link IOException} with
	 * {@link RuntimeException}
	 *
	 * @param closeable
	 *            closeable object
	 */
	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				throw new RuntimeException("IOException occurred. ", e);
			}
		}
	}

	/**
	 * Close closable and hide possible {@link IOException}
	 *
	 * @param closeable
	 *            closeable object
	 */
	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				// Ignored
			}
		}
	}

	/**
	 * 保存文本
	 * 
	 * @param fileName
	 *            文件名字
	 * @param content
	 *            內容
	 * @param append
	 *            是否累加
	 * @return 是否成功
	 */
	public static boolean saveTextValue(String fileName, String content, boolean append) {

		try {
			File textFile = new File(fileName);
			if (!append && textFile.exists())
				textFile.delete();

			FileOutputStream os = new FileOutputStream(textFile);
			os.write(content.getBytes("UTF-8"));
			os.close();
		} catch (Exception ee) {
			return false;
		}

		return true;
	}

	/**
	 * 刪除目錄下所有文件
	 * 
	 * @param Path
	 *            路徑
	 */
	public static void deleteAllFile(String Path) {

		// 刪除目錄下所有文件
		File path = new File(Path);
		File files[] = path.listFiles();
		if (files != null) {
			for (File tfi : files) {
				if (tfi.isDirectory()) {
					System.out.println(tfi.getName());
				} else {
					tfi.delete();
				}
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

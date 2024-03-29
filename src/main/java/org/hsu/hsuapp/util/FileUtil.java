package org.hsu.hsuapp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FileUtil {

	public final static String FILE_EXTENSION_SEPARATOR = ".";
	/** URI類型：file */
	public static final String URI_TYPE_FILE = "file";

	public FileUtil() {

	}

	/**
	 * 列出指定目錄下的檔案
	 * 
	 * @param sendFiles  存放檔案資料
	 * @param baseFolder 根目錄(預設為BASE)
	 * @param srcFolder  相對目錄(不為null時，以此目錄為PATH開頭)
	 * @param srcPattern 符合條件檔名(正則表示式)
	 */
	public static void findFiles(List<Map<String, String>> sendFiles, String baseFolder, String srcFolder,
			String srcPattern) {
		File srcDir = srcFolder == null ? new File(baseFolder) : new File(baseFolder, srcFolder);
		if (srcDir.exists() && srcDir.isDirectory()) {
			File[] fileList = srcDir.listFiles();
			for (File file : fileList) {
				if (srcPattern == null || file.getName().matches(srcPattern)) {
					String filePath = (srcFolder == null ? "" : srcFolder + "/") + file.getName();
					if (file.isDirectory()) {
						findFiles(sendFiles, baseFolder, filePath, null); // 全部
					} else {
						Map<String, String> fMap = new HashMap<String, String>();
						fMap.put("BASE", baseFolder);
						fMap.put("PATH", filePath);
						sendFiles.add(fMap);
					}
				}
			} // end of for
		} // end of if
	}

	/**
	 * read file
	 *
	 * @param filePath    路徑
	 * @param charsetName The name of a supported {@link java.nio.charset.Charset
	 *                    </code>charset<code>}
	 * @return if file not exist, return null, else return content of file
	 * @throws RuntimeException if an error occurs while operator BufferedReader
	 */
	public static StringBuilder readFile(String filePath, String charsetName) {

		File file = new File(filePath);
		StringBuilder fileContent = new StringBuilder("");
		if (file == null || !file.isFile()) {
			return null;
		}

		BufferedReader reader = null;
		try {
			InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
			reader = new BufferedReader(is);
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (!fileContent.toString().equals("")) {
					fileContent.append("\r\n");
				}
				fileContent.append(line);
			}
			return fileContent;
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			IOUtil.close(reader);
		}
	}

	/**
	 * write file
	 *
	 * @param filePath 路徑
	 * @param content  上下文
	 * @param append   is append, if true, write to the end of file, else clear
	 *                 content of file and write into it
	 * @return return false if content is empty, true otherwise
	 * @throws RuntimeException if an error occurs while operator FileWriter
	 */
	public static boolean writeFile(String filePath, String content, boolean append) {

		if (StringUtil.isEmpty(content)) {
			return false;
		}

		FileWriter fileWriter = null;
		try {
			makeDirs(filePath);
			fileWriter = new FileWriter(filePath, append);
			fileWriter.write(content);
			return true;
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			IOUtil.close(fileWriter);
		}
	}

	/**
	 * write file
	 *
	 * @param filePath    路徑
	 * @param contentList 集合
	 * @param append      is append, if true, write to the end of file, else clear
	 *                    content of file and write into it
	 * @return return false if contentList is empty, true otherwise
	 * @throws RuntimeException if an error occurs while operator FileWriter
	 */
	public static boolean writeFile(String filePath, List<String> contentList, boolean append) {

		if (contentList.size() == 0 || null == contentList) {
			return false;
		}

		FileWriter fileWriter = null;
		try {
			makeDirs(filePath);
			fileWriter = new FileWriter(filePath, append);
			int i = 0;
			for (String line : contentList) {
				if (i++ > 0) {
					fileWriter.write("\r\n");
				}
				fileWriter.write(line);
			}
			return true;
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			IOUtil.close(fileWriter);
		}
	}

	/**
	 * write file, the string will be written to the begin of the file
	 * 
	 * @param filePath 地址
	 * @param content  上下文
	 * @return 是否寫入成功
	 */
	public static boolean writeFile(String filePath, String content) {

		return writeFile(filePath, content, false);
	}

	/**
	 * write file, the string list will be written to the begin of the file
	 * 
	 * @param filePath    地址
	 * @param contentList 集合
	 * @return 是否寫入成功
	 */
	public static boolean writeFile(String filePath, List<String> contentList) {
		return writeFile(filePath, contentList, false);

	}

	/**
	 * write file, the bytes will be written to the begin of the file
	 *
	 * @param filePath 路徑
	 * @param stream   輸入流
	 * @return 返回是否寫入成功
	 */
	public static boolean writeFile(String filePath, InputStream stream) {
		return writeFile(filePath, stream, false);

	}

	/**
	 * write file
	 *
	 * @param filePath 路徑
	 * @param stream   the input stream
	 * @param append   if <code>true</code>, then bytes will be written to the end
	 *                 of the file rather than the beginning
	 * @return return true FileOutputStream
	 */
	public static boolean writeFile(String filePath, InputStream stream, boolean append) {

		return writeFile(filePath != null ? new File(filePath) : null, stream, append);
	}

	/**
	 * write file, the bytes will be written to the begin of the file
	 *
	 * @param file   文件對象
	 * @param stream 輸入流
	 * @return 返回是否寫入成功
	 */
	public static boolean writeFile(File file, InputStream stream) {
		return writeFile(file, stream, false);

	}

	/**
	 * write file
	 *
	 * @param file   the file to be opened for writing.
	 * @param stream the input stream
	 * @param append if <code>true</code>, then bytes will be written to the end of
	 *               the file rather than the beginning
	 * @return return true
	 * @throws RuntimeException if an error occurs while operator FileOutputStream
	 */
	public static boolean writeFile(File file, InputStream stream, boolean append) {
		OutputStream o = null;
		try {
			makeDirs(file.getAbsolutePath());
			o = new FileOutputStream(file, append);
			byte data[] = new byte[1024];
			int length = -1;
			while ((length = stream.read(data)) != -1) {
				o.write(data, 0, length);
			}
			o.flush();
			return true;
		} catch (FileNotFoundException e) {
			throw new RuntimeException("FileNotFoundException occurred. ", e);
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			IOUtil.close(o);
			IOUtil.close(stream);
		}
	}

	/**
	 * move file
	 * 
	 * @param sourceFilePath 資源路徑
	 * @param destFilePath   刪除的路徑
	 */
	public static void moveFile(String sourceFilePath, String destFilePath) {

		if (StringUtil.isBlank(sourceFilePath) || StringUtil.isBlank(destFilePath)) {
			throw new RuntimeException("Both sourceFilePath and destFilePath cannot be null.");
		}
		moveFile(new File(sourceFilePath), new File(destFilePath));
	}

	/**
	 * move file
	 * 
	 * @param srcFile  文件對象
	 * @param destFile 對象
	 */
	public static void moveFile(File srcFile, File destFile) {

		boolean rename = srcFile.renameTo(destFile);
		if (!rename) {
			copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
			deleteFile(srcFile.getAbsolutePath());
		}
	}

	/**
	 * copy file
	 *
	 * @param sourceFilePath 資源路徑
	 * @param destFilePath   刪除的文件
	 * @throws RuntimeException if an error occurs while operator FileOutputStream
	 * @return 返回是否成功
	 */
	public static boolean copyFile(String sourceFilePath, String destFilePath) {

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(sourceFilePath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("FileNotFoundException occurred. ", e);
		}
		return writeFile(destFilePath, inputStream);
	}

	/**
	 * read file to string list, a element of list is a line
	 *
	 * @param filePath    路徑
	 * @param charsetName The name of a supported {@link java.nio.charset.Charset
	 *                    </code>charset<code>}
	 * @return if file not exist, return null, else return content of file
	 * @throws RuntimeException if an error occurs while operator BufferedReader
	 */
	public static List<String> readFileToList(String filePath, String charsetName) {

		File file = new File(filePath);
		List<String> fileContent = new ArrayList<String>();
		if (file == null || !file.isFile()) {
			return null;
		}

		BufferedReader reader = null;
		try {
			InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
			reader = new BufferedReader(is);
			String line = null;
			while ((line = reader.readLine()) != null) {
				fileContent.add(line);
			}
			return fileContent;
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			IOUtil.close(reader);
		}
	}

	/**
	 *
	 * @param filePath 文件的路徑
	 * @return 返回文件的信息
	 */
	public static String getFileNameWithoutExtension(String filePath) {

		if (StringUtil.isEmpty(filePath)) {
			return filePath;
		}

		int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
		int filePosi = filePath.lastIndexOf(File.separator);
		if (filePosi == -1) {
			return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
		}
		if (extenPosi == -1) {
			return filePath.substring(filePosi + 1);
		}
		return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
	}

	/**
	 * get file name from path, include suffix
	 *
	 * <pre>
	 *      getFileName(null)               =   null
	 *      getFileName("")                 =   ""
	 *      getFileName("   ")              =   "   "
	 *      getFileName("a.mp3")            =   "a.mp3"
	 *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
	 *      getFileName("abc")              =   "abc"
	 *      getFileName("c:\\")              =   ""
	 *      getFileName("c:\\a")             =   "a"
	 *      getFileName("c:\\a.b")           =   "a.b"
	 *      getFileName("c:a.txt\\a")        =   "a"
	 *      getFileName("/home/admin")      =   "admin"
	 *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
	 * </pre>
	 *
	 * @param filePath 路徑
	 * @return file name from path, include suffix
	 */
	public static String getFileName(String filePath) {

		if (StringUtil.isEmpty(filePath)) {
			return filePath;
		}

		int filePosi = filePath.lastIndexOf(File.separator);
		return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
	}

	/**
	 * get folder name from path
	 *
	 * <pre>
	 *      getFolderName(null)               =   null
	 *      getFolderName("")                 =   ""
	 *      getFolderName("   ")              =   ""
	 *      getFolderName("a.mp3")            =   ""
	 *      getFolderName("a.b.rmvb")         =   ""
	 *      getFolderName("abc")              =   ""
	 *      getFolderName("c:\\")              =   "c:"
	 *      getFolderName("c:\\a")             =   "c:"
	 *      getFolderName("c:\\a.b")           =   "c:"
	 *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
	 *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
	 *      getFolderName("/home/admin")      =   "/home"
	 *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
	 * </pre>
	 * 
	 * @param filePath 路徑
	 * @return file name from path, include suffix
	 */
	public static String getFolderName(String filePath) {

		if (StringUtil.isEmpty(filePath)) {
			return filePath;
		}

		int filePosi = filePath.lastIndexOf(File.separator);
		return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
	}

	/**
	 * get suffix of file from path
	 *
	 * <pre>
	 *      getFileExtension(null)               =   ""
	 *      getFileExtension("")                 =   ""
	 *      getFileExtension("   ")              =   "   "
	 *      getFileExtension("a.mp3")            =   "mp3"
	 *      getFileExtension("a.b.rmvb")         =   "rmvb"
	 *      getFileExtension("abc")              =   ""
	 *      getFileExtension("c:\\")              =   ""
	 *      getFileExtension("c:\\a")             =   ""
	 *      getFileExtension("c:\\a.b")           =   "b"
	 *      getFileExtension("c:a.txt\\a")        =   ""
	 *      getFileExtension("/home/admin")      =   ""
	 *      getFileExtension("/home/admin/a.txt/b")  =   ""
	 *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
	 * </pre>
	 * 
	 * @param filePath 路徑
	 * @return 信息
	 */
	public static String getFileExtension(String filePath) {

		if (StringUtil.isBlank(filePath)) {
			return filePath;
		}

		int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
		int filePosi = filePath.lastIndexOf(File.separator);
		if (extenPosi == -1) {
			return "";
		}
		return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
	}

	/**
	 *
	 * @param filePath 路徑
	 * @return 是否創建成功
	 */
	public static boolean makeDirs(String filePath) {

		String folderName = getFolderName(filePath);
		if (StringUtil.isEmpty(folderName)) {
			return false;
		}

		File folder = new File(folderName);
		return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
	}

	/**
	 *
	 * @param filePath 路徑
	 * @return 是否創建成功
	 */
	public static boolean makeFolders(String filePath) {
		return makeDirs(filePath);

	}

	/**
	 *
	 * @param filePath 路徑
	 * @return 是否存在這個文件
	 */
	public static boolean isFileExist(String filePath) {
		if (StringUtil.isBlank(filePath)) {
			return false;
		}

		File file = new File(filePath);
		return (file.exists() && file.isFile());

	}

	/**
	 *
	 * @param directoryPath 路徑
	 * @return 是否有文件夾
	 */
	public static boolean isFolderExist(String directoryPath) {

		if (StringUtil.isBlank(directoryPath)) {
			return false;
		}

		File dire = new File(directoryPath);
		return (dire.exists() && dire.isDirectory());
	}

	/**
	 *
	 * @param path 路徑
	 * @return 是否刪除成功
	 */
	public static boolean deleteFile(String path) {

		if (StringUtil.isBlank(path)) {
			return true;
		}

		File file = new File(path);
		if (!file.exists()) {
			return true;
		}
		if (file.isFile()) {
			return file.delete();
		}
		if (!file.isDirectory()) {
			return false;
		}
		for (File f : file.listFiles()) {
			if (f.isFile()) {
				f.delete();
			} else if (f.isDirectory()) {
				deleteFile(f.getAbsolutePath());
			}
		}
		return file.delete();
	}

	/**
	 *
	 * @param path 路徑
	 * @return 返回文件大小
	 */
	public static long getFileSize(String path) {

		if (StringUtil.isBlank(path)) {
			return -1;
		}

		File file = new File(path);
		return (file.exists() && file.isFile() ? file.length() : -1);
	}

	/**
	 * 保存多媒體數據為文件.
	 *
	 * @param data     多媒體數據
	 * @param fileName 保存文件名
	 * @return 保存成功或失敗
	 */
	public static boolean save2File(InputStream data, String fileName) {
		File file = new File(fileName);
		FileOutputStream fos = null;
		try {
			// 文件或目錄不存在時,創建目錄和文件.
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}

			// 寫入數據
			fos = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int len;
			while ((len = data.read(b)) > -1) {
				fos.write(b, 0, len);
			}
			fos.close();

			return true;
		} catch (IOException ex) {

			return false;
		}
	}

	/**
	 * 讀取文件的字節數組.
	 *
	 * @param file 文件
	 * @return 字節數組
	 */
	public static byte[] readFile4Bytes(File file) {

		// 如果文件不存在,返回空
		if (!file.exists()) {
			return null;
		}
		FileInputStream fis = null;
		try {
			// 讀取文件內容.
			fis = new FileInputStream(file);
			byte[] arrData = new byte[(int) file.length()];
			fis.read(arrData);
			// 返回
			return arrData;
		} catch (IOException e) {

			return null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {

				}
			}
		}
	}

	/**
	 * 讀取文本文件內容，以行的形式讀取
	 *
	 * @param filePathAndName 帶有完整絕對路徑的文件名
	 * @return String 返回文本文件的內容
	 */
	public static String readFileContent(String filePathAndName) {
		try {
			return readFileContent(filePathAndName, null, null, 1024);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 讀取文本文件內容，以行的形式讀取
	 *
	 * @param filePathAndName 帶有完整絕對路徑的文件名
	 * @param encoding        文本文件打開的編碼方式 例如 GBK,UTF-8
	 * @param sep             分隔符 例如：#，默認為\n;
	 * @param bufLen          設置緩沖區大小
	 * @return String 返回文本文件的內容
	 */
	public static String readFileContent(String filePathAndName, String encoding, String sep, int bufLen) {
		if (filePathAndName == null || filePathAndName.equals("")) {
			return "";
		}
		if (sep == null || sep.equals("")) {
			sep = "\n";
		}
		if (!new File(filePathAndName).exists()) {
			return "";
		}
		StringBuffer str = new StringBuffer("");
		FileInputStream fs = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			fs = new FileInputStream(filePathAndName);
			if (encoding == null || encoding.trim().equals("")) {
				isr = new InputStreamReader(fs);
			} else {
				isr = new InputStreamReader(fs, encoding.trim());
			}
			br = new BufferedReader(isr, bufLen);

			String data = "";
			while ((data = br.readLine()) != null) {
				str.append(data).append(sep);
			}
		} catch (IOException e) {
		} finally {
			try {
				if (br != null)
					br.close();
				if (isr != null)
					isr.close();
				if (fs != null)
					fs.close();
			} catch (IOException e) {
			}
		}
		return str.toString();
	}

	/**
	 * 根據文件路徑，檢查文件是否不大於指定大小
	 *
	 * @param filepath 文件路徑
	 * @param maxSize  最大
	 * @return 是否
	 */
	public static boolean checkFileSize(String filepath, int maxSize) {

		File file = new File(filepath);
		if (!file.exists() || file.isDirectory()) {
			return false;
		}
		if (file.length() <= maxSize * 1024) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 將字符串以UTF-8編碼保存到文件中
	 * 
	 * @param str      保存的字符串
	 * @param fileName 文件的名字
	 * @return 是否保存成功
	 */
	public static boolean saveStrToFile(String str, String fileName) {

		return saveStrToFile(str, fileName, "UTF-8");
	}

	/**
	 * 將字符串以charsetName編碼保存到文件中
	 * 
	 * @param str         保存的字符串
	 * @param fileName    文件的名字
	 * @param charsetName 字符串編碼
	 * @return 是否保存成功
	 */
	public static boolean saveStrToFile(String str, String fileName, String charsetName) {

		if (str == null || "".equals(str)) {
			return false;
		}

		FileOutputStream stream = null;
		try {
			File file = new File(fileName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			byte[] b = null;
			if (charsetName != null && !"".equals(charsetName)) {
				b = str.getBytes(charsetName);
			} else {
				b = str.getBytes();
			}

			stream = new FileOutputStream(file);
			stream.write(b, 0, b.length);
			stream.flush();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (stream != null) {
				try {
					stream.close();
					stream = null;
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 文件重命名
	 *
	 * @param oldPath 舊的文件名字
	 * @param newPath 新的文件名字
	 */
	public static void renameFile(String oldPath, String newPath) {

		try {
			if (!StringUtil.isEmpty(oldPath) && !StringUtil.isEmpty(newPath) && !oldPath.equals(newPath)) {
				File fileOld = new File(oldPath);
				File fileNew = new File(newPath);
				fileOld.renameTo(fileNew);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

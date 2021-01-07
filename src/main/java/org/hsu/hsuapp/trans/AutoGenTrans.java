package org.hsu.hsuapp.trans;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hsu.hsuapp.util.FileUtil;
import org.hsu.hsuapp.util.StringUtil;

public class AutoGenTrans {

	private static String GAME_PATH = "C:\\tmp\\trans";
	private static String AUTO_GEN_TRANS_TXT = "_AutoGeneratedTranslations.txt";
	private String charsetName = "UTF-8";

	private List<String[]> autoGenTransList = new ArrayList<String[]>(); // 暫存已翻譯的資料

	public AutoGenTrans(String path) {
		this.GAME_PATH = path;
	}

	/**
	 * 讀取-已翻譯的資料
	 */
	public void load() {
		File f = new File(GAME_PATH, AUTO_GEN_TRANS_TXT);
		try {
			if (!f.exists())
				f.createNewFile();
			List<String> list = FileUtil.readFileToList(f.getAbsolutePath(), charsetName);
			for (String row : list) {
				String[] datas = row.split("=");
				if (datas.length > 1)
					autoGenTransList.add(datas);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查詢-已翻譯的資料
	 * 
	 * @param q
	 * @return
	 */
	public String query(String q) {
		String r = "";
		for (String[] data : autoGenTransList) {
			if (data.length > 1) {
				String from = data[0];
				if (q.equals(from))
					r = data[1];
			}
		}
		return r;
	}

	/**
	 * 寫入新的翻譯資料
	 * 
	 * @param from
	 * @param to
	 */
	public void write(String from, String to) {
		if ((!StringUtil.isBlank(from) && !StringUtil.isBlank(to))) {
			System.out.println("write autoGenTransList and File...");
			// 暫存
			autoGenTransList.add(new String[] { from, to });
			// 寫檔
			String data = from + "=" + to + "\r\n";
			File f = new File(GAME_PATH, AUTO_GEN_TRANS_TXT);
			FileUtil.writeFile(f.getAbsolutePath(), data, true);
		}
	}

	/**
	 * 將 autoGenTransList 寫入檔案
	 * @param outputFile
	 */
	public void writeListToFile(File outputFile) {
		for (String[] data : autoGenTransList) {
			String str = data[0] + "=" + data[1] + "\r\n";
			FileUtil.writeFile(outputFile.getAbsolutePath(), str, true);
		}
	}
	
	/**
	 * 刪除 autoGenTransList中空白內容
	 */
	public void deleteBlankData() {
		for (Iterator<String[]> it = autoGenTransList.iterator(); it.hasNext();) {
			String[] data = it.next();
			if ((StringUtil.isBlank(data[0]) || StringUtil.isBlank(data[1]))) {
				it.remove();
			}
		}
	}

	/**
	 * autoGenTransList 字符轉換
	 */
	public void transPunctuation() {
		for (Iterator<String[]> it = autoGenTransList.iterator(); it.hasNext();) {
			String[] data = it.next();
			if ((!StringUtil.isBlank(data[1]))) {
				data[1] = data[1].replaceAll("“", "「");
				data[1] = data[1].replaceAll("”", "」");				
			}
		}
	}
	
	public List<String[]> getAutoGenTransList() {
		return autoGenTransList;
	}	
	
	public static void main(String[] args) {
		String GAME_PATH = "D:\\tmp\\data_1_test";
		AutoGenTrans autoGenTrans = new AutoGenTrans(GAME_PATH);
		autoGenTrans.load();
		autoGenTrans.deleteBlankData();
		System.out.println("刪除後=" + autoGenTrans.getAutoGenTransList().size());
		autoGenTrans.transPunctuation();
		File outputFile = new File(GAME_PATH, "111.txt");
		System.out.println("寫檔....");
		autoGenTrans.writeListToFile(outputFile);
//		List<String[]> autoGenTransList = autoGenTrans.getAutoGenTransList();
//		String q = "キスで気持ちを表す";
//		System.out.println(q);
//		String trans = autoGenTrans.query(q);
//		System.out.println("TRANS=" + trans);
//
//		if (StringUtil.isBlank(trans)) {
//			String from = "キスで気持ちを表す";
//			String to = "用一個吻表達你的感受";
//			autoGenTrans.write(from, to);
//		}
//
//		System.out.println(q);
//		System.out.println("TRANS=" + autoGenTrans.query(q));
	}
}

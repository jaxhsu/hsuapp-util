package org.hsu.hsuapp.util;

import java.util.LinkedList;
import java.util.List;

public class CharacterUtil {

	/**
	 * 16 進制字串 轉 string
	 * 
	 * @param hexStr 16 進制字串
	 * @return string
	 */
	public static String hexStr_to_String(String hexStr) {
		String rtnStr = "";
		int codePoint = Integer.parseInt(hexStr, 16);
		rtnStr = String.valueOf(Character.toChars(codePoint));
		return rtnStr;
	}
	
	/**
	 * string(單一字) 轉 16 進制字串
	 * 
	 * @param str
	 * @return
	 */
	public static String string_to_hexStr(String str) {
		String hexStr = "";
		long str_codePoint_len = str.codePointCount(0, str.length());
		
		if(str_codePoint_len == 1) {
			int codePoint = str.codePointAt(0);
			hexStr = Integer.toHexString(codePoint).toUpperCase();
		}
		
		return hexStr;
	}
	
	/**
	 * 判斷是否有 輔助平面字符。</br>
	 * 超過 U+10000 的字符 
	 * 
	 * @param text 判斷的字串
	 * @return true : 有 , false : 沒有
	 */
	public static boolean has_surrogate_pair_char(String text) {
		boolean b = false;
		long text_len = text.length();
		long text_codePoint_len = text.codePointCount(0, text.length());

		if (text_len != text_codePoint_len) {
			b = true;
		}

		return b;
	}
	
	/**
	 * 查詢字串中，輔助平面字符的索引
	 * 
	 * @param text 判斷的字串
	 * @return int[0] : 起始索引 ， int[1] : 結束索引
	 */
	public static List<int[]> query_surrogate_pair_char_index(String text) {
		
		List<int[]> list = new LinkedList<int[]>();
		
		if (has_surrogate_pair_char(text)) {
			
			for (int i = 0; i < text.length(); i++) {
				int[] idx_datas = new int[2];
				int character = text.codePointAt(i);
				// 是否為輔助平面字符
				if (Character.isSupplementaryCodePoint(character)) {
					idx_datas[0] = i;
					idx_datas[1] = i + 2;
					list.add(idx_datas);
					// 如果是輔助平面字符，下一個char略過，則i+1
					i += 1;
				}
			}
		}
		return list;
	}
	
}

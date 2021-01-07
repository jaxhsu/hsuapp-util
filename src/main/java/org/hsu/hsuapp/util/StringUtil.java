package org.hsu.hsuapp.util;

import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h2>字符串工具類，提供一些字符串相關的便捷方法</h2>
 */
public class StringUtil {

	static Set<UnicodeBlock> japaneseUnicodeBlocks = new HashSet<UnicodeBlock>() {
		{
			add(UnicodeBlock.HIRAGANA);
			add(UnicodeBlock.KATAKANA);
			add(UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
		}
	};

	public StringUtil() {

	}

	/**
	 * 將字串起始的 0 刪除
	 * 
	 * @param str
	 * @return
	 */
	public static String delFirstZero(String str) {
		return str.replaceFirst("^0*", "");
	}

	/**
	 * is null or its length is 0 or it is made by space
	 *
	 * <pre>
	 * isBlank(null) = true;
	 * isBlank(&quot;&quot;) = true;
	 * isBlank(&quot;  &quot;) = true;
	 * isBlank(&quot;a&quot;) = false;
	 * isBlank(&quot;a &quot;) = false;
	 * isBlank(&quot; a&quot;) = false;
	 * isBlank(&quot;a b&quot;) = false;
	 * </pre>
	 *
	 * @param str str
	 * @return if string is null or its size is 0 or it is made by space, return
	 *         true, else return false.
	 */
	public static boolean isBlank(String str) {

		return (str == null || str.trim().length() == 0);
	}

	/**
	 * is null or its length is 0
	 *
	 * <pre>
	 * isEmpty(null) = true;
	 * isEmpty(&quot;&quot;) = true;
	 * isEmpty(&quot;  &quot;) = false;
	 * </pre>
	 *
	 * @param str str
	 * @return if string is null or its size is 0, return true, else return false.
	 */
	public static boolean isEmpty(CharSequence str) {

		return (str == null || str.length() == 0);
	}

	/**
	 * get length of CharSequence
	 *
	 * <pre>
	 * length(null) = 0;
	 * length(\"\") = 0;
	 * length(\"abc\") = 3;
	 * </pre>
	 *
	 * @param str str
	 * @return if str is null or empty, return 0, else return
	 *         {@link CharSequence#length()}.
	 */
	public static int length(CharSequence str) {

		return str == null ? 0 : str.length();
	}

	/**
	 * null Object to empty string
	 *
	 * <pre>
	 * nullStrToEmpty(null) = &quot;&quot;;
	 * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
	 * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
	 * </pre>
	 *
	 * @param str str
	 * @return String
	 */
	public static String nullStrToEmpty(Object str) {

		return (str == null ? "" : (str instanceof String ? (String) str : str.toString()));
	}

	/**
	 * @param str str
	 * @return String
	 */
	public static String capitalizeFirstLetter(String str) {

		if (isEmpty(str)) {
			return str;
		}

		char c = str.charAt(0);
		return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str
				: new StringBuilder(str.length()).append(Character.toUpperCase(c)).append(str.substring(1)).toString();
	}

	/**
	 * encoded in utf-8
	 *
	 * @param str 字符串
	 * @return 返回一個utf8的字符串
	 */
	public static String utf8Encode(String str) {

		if (!isEmpty(str) && str.getBytes().length != str.length()) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
			}
		}
		return str;
	}

	/**
	 * @param href 字符串
	 * @return 返回一個html
	 */
	public static String getHrefInnerHtml(String href) {

		if (isEmpty(href)) {
			return "";
		}

		String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
		Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
		Matcher hrefMatcher = hrefPattern.matcher(href);
		if (hrefMatcher.matches()) {
			return hrefMatcher.group(1);
		}
		return href;
	}

	/**
	 * @param source 字符串
	 * @return 返回htmL到字符串
	 */
	public static String htmlEscapeCharsToString(String source) {

		return StringUtil.isEmpty(source) ? source
				: source.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;",
						"\"");
	}

	/**
	 * @param s str
	 * @return String
	 */
	public static String fullWidthToHalfWidth(String s) {

		if (isEmpty(s)) {
			return s;
		}

		char[] source = s.toCharArray();
		for (int i = 0; i < source.length; i++) {
			if (source[i] == 12288) {
				source[i] = ' ';
				// } else if (source[i] == 12290) {
				// source[i] = '.';
			} else if (source[i] >= 65281 && source[i] <= 65374) {
				source[i] = (char) (source[i] - 65248);
			} else {
				source[i] = source[i];
			}
		}
		return new String(source);
	}

	/**
	 * @param s 字符串
	 * @return 返回的數值
	 */
	public static String halfWidthToFullWidth(String s) {

		if (isEmpty(s)) {
			return s;
		}

		char[] source = s.toCharArray();
		for (int i = 0; i < source.length; i++) {
			if (source[i] == ' ') {
				source[i] = (char) 12288;
				// } else if (source[i] == '.') {
				// source[i] = (char)12290;
			} else if (source[i] >= 33 && source[i] <= 126) {
				source[i] = (char) (source[i] + 65248);
			} else {
				source[i] = source[i];
			}
		}
		return new String(source);
	}

	/**
	 * @param str 資源
	 * @return 特殊字符串切換
	 */

	public static String replaceBlanktihuan(String str) {

		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 判斷給定的字符串是否為null或者是空的
	 *
	 * @param string 給定的字符串
	 */
	public static boolean isEmpty(String string) {
		return string == null || "".equals(string.trim());
	}

	/**
	 * 判斷給定的字符串是否不為null且不為空
	 *
	 * @param string 給定的字符串
	 */
	public static boolean isNotEmpty(String string) {
		return !isEmpty(string);
	}

	/**
	 * 判斷給定的字符串數組中的所有字符串是否都為null或者是空的
	 *
	 * @param strings 給定的字符串
	 */
	public static boolean isEmpty(String... strings) {
		boolean result = true;
		for (String string : strings) {
			if (isNotEmpty(string)) {
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * 判斷給定的字符串數組中是否全部都不為null且不為空
	 *
	 * @param strings 給定的字符串數組
	 * @return 是否全部都不為null且不為空
	 */
	public static boolean isNotEmpty(String... strings) {
		boolean result = true;
		for (String string : strings) {
			if (isEmpty(string)) {
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * 如果字符串是null或者空就返回""
	 */
	public static String filterEmpty(String string) {
		return StringUtil.isNotEmpty(string) ? string : "";
	}

	/**
	 * 在給定的字符串中，用新的字符替換所有舊的字符
	 *
	 * @param string  給定的字符串
	 * @param oldchar 舊的字符
	 * @param newchar 新的字符
	 * @return 替換後的字符串
	 */
	public static String replace(String string, char oldchar, char newchar) {
		char chars[] = string.toCharArray();
		for (int w = 0; w < chars.length; w++) {
			if (chars[w] == oldchar) {
				chars[w] = newchar;
				break;
			}
		}
		return new String(chars);
	}

	/**
	 * 把給定的字符串用給定的字符分割
	 *
	 * @param string 給定的字符串
	 * @param ch     給定的字符
	 * @return 分割後的字符串數組
	 */
	public static String[] split(String string, char ch) {
		ArrayList<String> stringList = new ArrayList<String>();
		char chars[] = string.toCharArray();
		int nextStart = 0;
		for (int w = 0; w < chars.length; w++) {
			if (ch == chars[w]) {
				stringList.add(new String(chars, nextStart, w - nextStart));
				nextStart = w + 1;
				if (nextStart == chars.length) { // 當最後一位是分割符的話，就再添加一個空的字符串到分割數組中去
					stringList.add("");
				}
			}
		}
		if (nextStart < chars.length) { // 如果最後一位不是分隔符的話，就將最後一個分割符到最後一個字符中間的左右字符串作為一個字符串添加到分割數組中去
			stringList.add(new String(chars, nextStart, chars.length - 1 - nextStart + 1));
		}
		return stringList.toArray(new String[stringList.size()]);
	}

	/**
	 * 計算給定的字符串的長度，計算規則是：一個漢字的長度為2，一個字符的長度為1
	 *
	 * @param string 給定的字符串
	 * @return 長度
	 */
	public static int countLength(String string) {
		int length = 0;
		char[] chars = string.toCharArray();
		for (int w = 0; w < string.length(); w++) {
			char ch = chars[w];
			if (ch >= '\u0391' && ch <= '\uFFE5') {
				length++;
				length++;
			} else {
				length++;
			}
		}
		return length;
	}

	private static char[] getChars(char[] chars, int startIndex) {
		int endIndex = startIndex + 1;
		// 如果第一個是數字
		if (Character.isDigit(chars[startIndex])) {
			// 如果下一個是數字
			while (endIndex < chars.length && Character.isDigit(chars[endIndex])) {
				endIndex++;
			}
		}
		char[] resultChars = new char[endIndex - startIndex];
		System.arraycopy(chars, startIndex, resultChars, 0, resultChars.length);
		return resultChars;
	}

	/**
	 * 是否全是數字
	 */
	public static boolean isAllDigital(char[] chars) {
		boolean result = true;
		for (int w = 0; w < chars.length; w++) {
			if (!Character.isDigit(chars[w])) {
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * 刪除給定字符串中所有的舊的字符
	 *
	 * @param string 源字符串
	 * @param ch     要刪除的字符
	 * @return 刪除後的字符串
	 */
	public static String removeChar(String string, char ch) {
		StringBuffer sb = new StringBuffer();
		for (char cha : string.toCharArray()) {
			if (cha != '-') {
				sb.append(cha);
			}
		}
		return sb.toString();
	}

	/**
	 * 刪除給定字符串中給定位置處的字符
	 *
	 * @param string 給定字符串
	 * @param index  給定位置
	 */
	public static String removeChar(String string, int index) {
		String result = null;
		char[] chars = string.toCharArray();
		if (index == 0) {
			result = new String(chars, 1, chars.length - 1);
		} else if (index == chars.length - 1) {
			result = new String(chars, 0, chars.length - 1);
		} else {
			result = new String(chars, 0, index) + new String(chars, index + 1, chars.length - index);
			;
		}
		return result;
	}

	/**
	 * 刪除給定字符串中給定位置處的字符
	 *
	 * @param string 給定字符串
	 * @param index  給定位置
	 * @param ch     如果同給定位置處的字符相同，則將給定位置處的字符刪除
	 */
	public static String removeChar(String string, int index, char ch) {
		String result = null;
		char[] chars = string.toCharArray();
		if (chars.length > 0 && chars[index] == ch) {
			if (index == 0) {
				result = new String(chars, 1, chars.length - 1);
			} else if (index == chars.length - 1) {
				result = new String(chars, 0, chars.length - 1);
			} else {
				result = new String(chars, 0, index) + new String(chars, index + 1, chars.length - index);
				;
			}
		} else {
			result = string;
		}
		return result;
	}

	/**
	 * 對給定的字符串進行空白過濾
	 *
	 * @param string 給定的字符串
	 * @return 如果給定的字符串是一個空白字符串，那麽返回null；否則返回本身。
	 */
	public static String filterBlank(String string) {
		if ("".equals(string)) {
			return null;
		} else {
			return string;
		}
	}

	/**
	 * 將給定字符串中給定的區域的字符轉換成小寫
	 *
	 * @param str        給定字符串中
	 * @param beginIndex 開始索引（包括）
	 * @param endIndex   結束索引（不包括）
	 * @return 新的字符串
	 */
	public static String toLowerCase(String str, int beginIndex, int endIndex) {
		return str.replaceFirst(str.substring(beginIndex, endIndex),
				str.substring(beginIndex, endIndex).toLowerCase(Locale.getDefault()));
	}

	/**
	 * 將給定字符串中給定的區域的字符轉換成大寫
	 *
	 * @param str        給定字符串中
	 * @param beginIndex 開始索引（包括）
	 * @param endIndex   結束索引（不包括）
	 * @return 新的字符串
	 */
	public static String toUpperCase(String str, int beginIndex, int endIndex) {
		return str.replaceFirst(str.substring(beginIndex, endIndex),
				str.substring(beginIndex, endIndex).toUpperCase(Locale.getDefault()));
	}

	/**
	 * 將給定字符串的首字母轉為小寫
	 *
	 * @param str 給定字符串
	 * @return 新的字符串
	 */
	public static String firstLetterToLowerCase(String str) {
		return toLowerCase(str, 0, 1);
	}

	/**
	 * 將給定字符串的首字母轉為大寫
	 *
	 * @param str 給定字符串
	 * @return 新的字符串
	 */
	public static String firstLetterToUpperCase(String str) {
		return toUpperCase(str, 0, 1);
	}

	/**
	 * 將給定的字符串MD5加密
	 *
	 * @param string 給定的字符串
	 * @return MD5加密後生成的字符串
	 */
	public static String MD5(String string) {
		String result = null;
		try {
			char[] charArray = string.toCharArray();
			byte[] byteArray = new byte[charArray.length];
			for (int i = 0; i < charArray.length; i++) {
				byteArray[i] = (byte) charArray[i];
			}

			StringBuffer hexValue = new StringBuffer();
			byte[] md5Bytes = MessageDigest.getInstance("MD5").digest(byteArray);
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}

			result = hexValue.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 判斷給定的字符串是否以一個特定的字符串開頭，忽略大小寫
	 *
	 * @param sourceString 給定的字符串
	 * @param newString    一個特定的字符串
	 */
	public static boolean startsWithIgnoreCase(String sourceString, String newString) {
		int newLength = newString.length();
		int sourceLength = sourceString.length();
		if (newLength == sourceLength) {
			return newString.equalsIgnoreCase(sourceString);
		} else if (newLength < sourceLength) {
			char[] newChars = new char[newLength];
			sourceString.getChars(0, newLength, newChars, 0);
			return newString.equalsIgnoreCase(String.valueOf(newChars));
		} else {
			return false;
		}
	}

	/**
	 * 判斷給定的字符串是否以一個特定的字符串結尾，忽略大小寫
	 *
	 * @param sourceString 給定的字符串
	 * @param newString    一個特定的字符串
	 */
	public static boolean endsWithIgnoreCase(String sourceString, String newString) {
		int newLength = newString.length();
		int sourceLength = sourceString.length();
		if (newLength == sourceLength) {
			return newString.equalsIgnoreCase(sourceString);
		} else if (newLength < sourceLength) {
			char[] newChars = new char[newLength];
			sourceString.getChars(sourceLength - newLength, sourceLength, newChars, 0);
			return newString.equalsIgnoreCase(String.valueOf(newChars));
		} else {
			return false;
		}
	}

	/**
	 * 檢查字符串長度，如果字符串的長度超過maxLength，就截取前maxLength個字符串並在末尾拼上appendString
	 */
	public static String checkLength(String string, int maxLength, String appendString) {
		if (string.length() > maxLength) {
			string = string.substring(0, maxLength);
			if (appendString != null) {
				string += appendString;
			}
		}
		return string;
	}

	/**
	 * 檢查字符串長度，如果字符串的長度超過maxLength，就截取前maxLength個字符串並在末尾拼上…
	 */
	public static String checkLength(String string, int maxLength) {
		return checkLength(string, maxLength, "…");
	}

	/**
	 * 刪除Html標簽
	 *
	 * @param inputString
	 * @return
	 */
	public static String htmlRemoveTag(String inputString) {
		if (inputString == null)
			return null;
		String htmlStr = inputString; // 含html標簽的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			// 定義script的正則表達式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
			// 定義style的正則表達式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
			String regEx_html = "<[^>]+>"; // 定義HTML標簽的正則表達式
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 過濾script標簽
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 過濾style標簽
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 過濾html標簽
			textStr = htmlStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return textStr;// 返回文本字符串
	}

	/**
	 * 去掉字符串中的標點符號、空格和數字
	 *
	 * @param input
	 * @return
	 */
	public static String removePunctuation(String input) {
		return input.replaceAll(" +", "").replaceAll("[\\pP\\p{Punct}]", "").replaceAll("\\d+", "");
	}

	/**
	 * 判斷字符串是否為簡體中文
	 *
	 * @param input
	 * @return
	 */
	public static boolean isSimpleChinese(String input) {
		return input.matches("^[\u4e00-\u9fa5]+$");
	}

	/**
	 * 判斷字符串是否含日文
	 *
	 * @param input
	 * @return
	 */
	public static boolean isJapanese(String input) {
		try {
			return input.getBytes("shift-jis").length >= (2 * input.length());
		} catch (UnsupportedEncodingException e) {
			return false;
		}
	}

	/**
	 * 判斷字符串是否含日文
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isJapanese1(String input) {
		boolean status = false;
		try {
			for (char c : input.toCharArray()) {
				if (japaneseUnicodeBlocks.contains(UnicodeBlock.of(c))) {
					status = true;
					break;
				} else {
					status = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		}
		return status;
	}

	/**
	 * 將字符串轉字碼點
	 *
	 * @param input
	 * @return
	 */
	public static String stringToUnicode(String input) throws UnsupportedEncodingException {
		StringBuffer out = new StringBuffer();
		byte[] bytes = input.getBytes("unicode");

		// 將其byte轉換成對應的16進制表示
		for (int i = 0; i < bytes.length - 1; i += 2) {
			out.append("\\u");
			String str = Integer.toHexString(bytes[i + 1] & 0xff);
			for (int j = str.length(); j < 2; j++) {
				out.append("0");
			}
			String str1 = Integer.toHexString(bytes[i] & 0xff);
			out.append(str1);
			out.append(str);
		}
		return out.toString();
	}

	/**
	 * 字碼點轉字符串
	 *
	 * @param unicode
	 * @return
	 */
	public static String unicodeToString(String unicode) {
		StringBuffer sb = new StringBuffer();
		String[] hex = unicode.split("\\\\u");
		for (int i = 1; i < hex.length; i++) {
			int index = Integer.parseInt(hex[i], 16);
			sb.append((char) index);
		}
		return sb.toString();
	}

}

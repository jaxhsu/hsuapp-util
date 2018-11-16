package org.hsu.hsuapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	
	/**
	 * 在符合Regex 的字串前後加上 mark
	 * 
	 * @param content
	 * @param regex
	 * @param pre_mark
	 * @param last_mark
	 * @return
	 */
	public String addmarkForRegexStr(String content, String regex, String pre_mark, String last_mark) {

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		StringBuffer new_content = new StringBuffer(content.length());

		while (m.find()) {
			String new_str = m.group();
			// 增加前後符號
			new_str = "<![CDATA[" + new_str + "]]>";
			m.appendReplacement(new_content, Matcher.quoteReplacement(new_str));
		}
		m.appendTail(new_content);
		
		return new_content.toString();
	}

}

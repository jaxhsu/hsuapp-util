package com.hsu.hsuapp.util;

import java.io.UnsupportedEncodingException;

import org.hsu.hsuapp.util.StringUtil;
import org.junit.Before;
import org.junit.Test;

public class StringUtilTest {

	StringUtil stringUtil;
	
	@Before
	public void init() {
		stringUtil = new StringUtil();
	}
	
	//@Test
	public void test() {
		
		System.out.println(StringUtil.delFirstZero("00005b26").toUpperCase());
		
	}
	
	@Test
	public void isJan() throws Exception {
        String ss = "中华人民共和国";
        String ss1 = "おはよう";
        System.out.println(ss + " shift-jis编码字符数：" + ss.getBytes("shift-jis").length);
        System.out.println(ss + " 2倍字符数：" + ss.length() * 2);
        System.out.println(ss1 + "shift-jis编码字符数：" + ss1.getBytes("shift-jis").length);
        System.out.println(ss1 + " 2倍字符数：" + ss1.length() * 2);
        System.out.println(ss + " 字码点：" + StringUtil.stringToUnicode(ss));
        System.out.println(ss1 + " 字码点：" + StringUtil.stringToUnicode(ss1));
        System.out.println("\\u4e00 -\\u9fa5" + "对应的中文是：" + StringUtil.unicodeToString("\\u4e00") + " - " + stringUtil.unicodeToString("\\u9fa5"));

        String s = "中华人民共和国，成立了~~~";
        String s1 = "1个和尚挑水喝， 2个和尚抬水喝， 3个和尚没呀没水喝";
        //String s2 = "あなたのお父さんとお母さんは大阪に行って、あなたのおじいさんとお婆さんはみんな東京に行って、あなたの弟の妹は北海道に行きました。";
        String s2 = "父母";
        String s3 = "1お、 2は、 3よ、 4う,呵呵";
        System.out.println("开始测试：");
        System.out.println(s + (StringUtil.isSimpleChinese(StringUtil.removePunctuation(s)) ? "是" : "不是") + "中文");
        System.out.println(s1 + (StringUtil.isSimpleChinese(StringUtil.removePunctuation(s1)) ? "是" : "不是") + "中文");
        System.out.println(s2 + (StringUtil.isJapanese(StringUtil.removePunctuation(s2)) ? "是" : "不是") + "日文");
        System.out.println(s3 + (StringUtil.isJapanese(StringUtil.removePunctuation(s3)) ? "是" : "不是") + "日文");		
	}
	
}

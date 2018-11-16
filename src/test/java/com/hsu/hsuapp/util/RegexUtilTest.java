package com.hsu.hsuapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hsu.hsuapp.util.RegexUtil;
import org.junit.Before;
import org.junit.Test;

public class RegexUtilTest {

	RegexUtil RegexUtil;

	@Before
	public void init() {
		RegexUtil = new RegexUtil();
	}
	
	//@Test
	public void test1() {
		
		String regex = "((<cnsd:code)\\s*(xmlns:cnsd=\"cnsd.xsd\")\\s*(encoding=\"HEX8\")\\s*>)\\s*(\\w{8})\\s*(</cnsd:code>)";
		Pattern p = Pattern.compile(regex);
		
		String content = "臺中市北屯區平安里青島路四段５８<cnsd:code xmlns:cnsd=\"cnsd.xsd\" encoding=\"HEX8\">00085b23</cnsd:code>１號臺中市北屯區平安里青島路四段５８<cnsd:code xmlns:cnsd=\"cnsd.xsd\" encoding=\"HEX8\">00085b24</cnsd:code>１號";
		content = "<?xml version=\"1.0\" encoding=\"utf-8\"?><CPWSResponse><Code>0</Code><Message>Success</Message><Result><ExeStatus><CP_PublicServiceInfo><MessageInfo><TicketID>10000092_1071001_185414_2035</TicketID></MessageInfo><ServiceInfo><ServiceID>PUB0000103</ServiceID><ServiceName>SmeaAPEX</ServiceName><ServiceAction>Async</ServiceAction></ServiceInfo><ServiceStatus><Status>COMPLETED</Status><Description>Complete</Description></ServiceStatus></CP_PublicServiceInfo><CP_AP_Service><Header><ServiceInfo><ServiceID>GOV0000177</ServiceID><ServiceName /><ServiceAction /></ServiceInfo><ServiceStatus><Status>Complete</Status><Description>Complete</Description></ServiceStatus></Header><Body><XMQ003_RS xmlns=\"http://li.etax.nat.gov.tw/MOF/XMQ003Res_new_definition\" xmlns:xmq1=\"http://li.etax.nat.gov.tw/MOF/XMQ003_new_definition\" xmlns:xmq=\"http://li.etax.nat.gov.tw/MOF/XMQ003073_definition\"><XMQ003_RS_001>10000092_1071001_185409_2075</XMQ003_RS_001><XMQ003_RS_002>10000092_1071001_185414_2035</XMQ003_RS_002><XMQ003_RS_003>S</XMQ003_RS_003><XMQ003_RS_004>1</XMQ003_RS_004><XMQ003_RS_005>10</XMQ003_RS_005><XMQ003_RS_006>查詢成功</XMQ003_RS_006><XMQ003_RSR><xmq:XMQ003073_RSD><xmq:XMQ003073_RSD_001>1</xmq:XMQ003073_RSD_001><xmq:XMQ003073_RSDR><xmq1:XMQ003_CMQ><xmq1:XMQ003_CMQ_001 /><xmq1:XMQ003_CMQ_002>1612322540000306</xmq1:XMQ003_CMQ_002><xmq1:XMQ003_CMQ_003>集祥鋁業股份有限公司</xmq1:XMQ003_CMQ_003><xmq1:XMQ003_CMQ_004>107</xmq1:XMQ003_CMQ_004><xmq1:XMQ003_CMQ_005 /></xmq1:XMQ003_CMQ><xmq1:XMQ003_CM_001>1</xmq1:XMQ003_CM_001><xmq1:XMQ003073_RSDR_001>10000092_1071001_185414_2035</xmq1:XMQ003073_RSDR_001><xmq1:XMQ003073_RSDR_002>1612322540000306</xmq1:XMQ003073_RSDR_002><xmq1:XMQ003073_RSDR_003>臺中市北屯區平安里青島路四段５８<cnsd:code xmlns:cnsd=\"cnsd.xsd\" encoding=\"HEX8\">00085b26</cnsd:code>１號</xmq1:XMQ003073_RSDR_003></xmq:XMQ003073_RSDR></xmq:XMQ003073_RSD></XMQ003_RSR></XMQ003_RS></Body></CP_AP_Service></ExeStatus></Result></CPWSResponse>";
		
		StringBuffer new_content = new StringBuffer(content.length());
				
		Matcher m = p.matcher(content);
		while (m.find()) {
			System.out.println(m.group());
			System.out.println(m.group(5).trim());
			System.out.println(m.start() + "..." + m.end());
			System.out.println(m.start(5) + "..." + m.end(5));
			
			String new_str = m.group();
			// 替換group(5)
			new_str = new_str.replace(m.group(5), "SSS");
			
			// 增加前後符號
			new_str = "<![CDATA[" + new_str + "]]>";
			m.appendReplacement(new_content, Matcher.quoteReplacement(new_str));
			
			System.out.println();
		}
		m.appendTail(new_content);
		
		System.out.println(new_content);
		
	}
	
	@Test
	public void test2() {
		String regex = "((<cnsd:code)\\s*(xmlns:cnsd=\"cnsd.xsd\")\\s*(encoding=\"HEX8\")\\s*>)\\s*(\\w{8})\\s*(</cnsd:code>)";
		
		String content = "臺中市北屯區平安里青島路四段５８<cnsd:code xmlns:cnsd=\"cnsd.xsd\" encoding=\"HEX8\">00085b23</cnsd:code>１號臺中市北屯區平安里青島路四段５８<cnsd:code xmlns:cnsd=\"cnsd.xsd\" encoding=\"HEX8\">00085b24</cnsd:code>１號";
		content = "<?xml version=\"1.0\" encoding=\"utf-8\"?><CPWSResponse><Code>0</Code><Message>Success</Message><Result><ExeStatus><CP_PublicServiceInfo><MessageInfo><TicketID>10000092_1071001_185414_2035</TicketID></MessageInfo><ServiceInfo><ServiceID>PUB0000103</ServiceID><ServiceName>SmeaAPEX</ServiceName><ServiceAction>Async</ServiceAction></ServiceInfo><ServiceStatus><Status>COMPLETED</Status><Description>Complete</Description></ServiceStatus></CP_PublicServiceInfo><CP_AP_Service><Header><ServiceInfo><ServiceID>GOV0000177</ServiceID><ServiceName /><ServiceAction /></ServiceInfo><ServiceStatus><Status>Complete</Status><Description>Complete</Description></ServiceStatus></Header><Body><XMQ003_RS xmlns=\"http://li.etax.nat.gov.tw/MOF/XMQ003Res_new_definition\" xmlns:xmq1=\"http://li.etax.nat.gov.tw/MOF/XMQ003_new_definition\" xmlns:xmq=\"http://li.etax.nat.gov.tw/MOF/XMQ003073_definition\"><XMQ003_RS_001>10000092_1071001_185409_2075</XMQ003_RS_001><XMQ003_RS_002>10000092_1071001_185414_2035</XMQ003_RS_002><XMQ003_RS_003>S</XMQ003_RS_003><XMQ003_RS_004>1</XMQ003_RS_004><XMQ003_RS_005>10</XMQ003_RS_005><XMQ003_RS_006>查詢成功</XMQ003_RS_006><XMQ003_RSR><xmq:XMQ003073_RSD><xmq:XMQ003073_RSD_001>1</xmq:XMQ003073_RSD_001><xmq:XMQ003073_RSDR><xmq1:XMQ003_CMQ><xmq1:XMQ003_CMQ_001 /><xmq1:XMQ003_CMQ_002>1612322540000306</xmq1:XMQ003_CMQ_002><xmq1:XMQ003_CMQ_003>集祥鋁業股份有限公司</xmq1:XMQ003_CMQ_003><xmq1:XMQ003_CMQ_004>107</xmq1:XMQ003_CMQ_004><xmq1:XMQ003_CMQ_005 /></xmq1:XMQ003_CMQ><xmq1:XMQ003_CM_001>1</xmq1:XMQ003_CM_001><xmq1:XMQ003073_RSDR_001>10000092_1071001_185414_2035</xmq1:XMQ003073_RSDR_001><xmq1:XMQ003073_RSDR_002>1612322540000306</xmq1:XMQ003073_RSDR_002><xmq1:XMQ003073_RSDR_003>臺中市北屯區平安里青島路四段５８<cnsd:code xmlns:cnsd=\"cnsd.xsd\" encoding=\"HEX8\">00085b26</cnsd:code>１號</xmq1:XMQ003073_RSDR_003></xmq:XMQ003073_RSDR></xmq:XMQ003073_RSD></XMQ003_RSR></XMQ003_RS></Body></CP_AP_Service></ExeStatus></Result></CPWSResponse>";
		
		String rlt = RegexUtil.addmarkForRegexStr(content, regex, "<![CDATA[", "]]>");
		System.out.println(rlt);
	}
	
	public static String replaceGroup(String regex, String source, int groupToReplace, String replacement) {
		return replaceGroup(regex, source, groupToReplace, 1, replacement);
	}
	
	public static String replaceGroup(String regex, String source, int groupToReplace, int groupOccurrence,
			String replacement) {
		Matcher m = Pattern.compile(regex).matcher(source);
		for (int i = 0; i < groupOccurrence; i++) {
			if (!m.find())
				return source;
		}
		return new StringBuilder(source).replace(m.start(groupToReplace), m.end(groupToReplace), replacement)
				.toString();
	}
	
}

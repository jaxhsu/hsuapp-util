package com.hsu.hsuapp.util;

import org.hsu.hsuapp.util.StringUtil;
import org.junit.Before;
import org.junit.Test;

public class StringUtilTest {

	StringUtil stringUtil;
	
	@Before
	public void init() {
		stringUtil = new StringUtil();
	}
	
	@Test
	public void test() {
		
		System.out.println(StringUtil.delFirstZero("00005b26").toUpperCase());
		
	}
	
}

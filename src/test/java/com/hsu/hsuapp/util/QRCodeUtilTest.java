package com.hsu.hsuapp.util;

import java.io.File;

import org.hsu.hsuapp.util.QRCodeUtil;
import org.junit.Before;
import org.junit.Test;

import com.google.zxing.BarcodeFormat;

public class QRCodeUtilTest {

	QRCodeUtil QRCodeUtil;
	
	@Before
	public void init() {
		QRCodeUtil = new QRCodeUtil();
	}
	
	@Test
	public void test() {
		File file = new File("test.png");
		QRCodeUtil.encode("中文二維碼信息", file, BarcodeFormat.QR_CODE, 200, 200, null);
		QRCodeUtil.decode(file);
	}
	
}

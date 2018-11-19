package com.hsu.hsuapp.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hsu.hsuapp.util.CharacterUtil;
import org.junit.Test;



public class CharacterUtilTest {
	
	CharacterUtil CharacterUtil = new CharacterUtil();
	
	@Test
	public void hexCodeStr_to_StringTest() {
		try {
			Map<String,String> map = new HashMap<String,String>();
			map.put("3-2144", "2000B");
			map.put("1-4A3C", "6211");
			map.put("10-212C", "2001B");
			map.put("85B26", "2015");
			
			String s = CharacterUtil.hexStr_to_String(map.get("85B26"));
			System.out.println("s=" + s);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//@Test
	public void query_surrogate_pair_char_indexTest() {
		try {
			String text = "<今?>$5%𠀋天我非:：常忙阿!𠀛ABCD";
			List<int[]> lists = CharacterUtil.query_surrogate_pair_char_index(text);
			for(int[] array:lists) {
				if(array.length > 0) {
					System.out.println(array[0] + "," + array[1]);
					String str = text.substring(array[0], array[1]);
					System.out.println(str);
					System.out.println(CharacterUtil.string_to_hexStr(str));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	// @Test
	public void string_to_hexStrTest() {
		String str = "󰀫";
		String hexStr = CharacterUtil.string_to_hexStr(str);
		System.out.println(str + " to Hex Str = " + hexStr);
	}
	
}

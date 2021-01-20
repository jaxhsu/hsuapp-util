package com.hsu.hsuapp.pojo;

import lombok.Data;

@Data
public class AppResult<T> {
	// 狀態碼
	private String code = "";
	// 需要傳遞的信息
	private String msg = "";
	// 需要傳遞的數據
	private T data;
}

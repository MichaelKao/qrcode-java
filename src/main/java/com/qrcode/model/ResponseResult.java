package com.qrcode.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseResult<T> {

	/**
	 * code HTTP 狀態碼
	 */
	private int code;

	/**
	 * message 提示訊息
	 */
	private String message;

	/**
	 * data 返回的數據，使用泛型
	 */
	private T data;

	/**
	 * timestamp 時間戳
	 */
	private long timestamp;

	/**
	 * success 成功
	 * 
	 * @param <T>
	 * @param data
	 * @return
	 */
	public static <T> ResponseResult<T> success(T data) {
		return ResponseResult.<T>builder().code(200).message("success").data(data).timestamp(System.currentTimeMillis())
				.build();
	}

	/**
	 * error 失敗
	 * 
	 * @param <T>
	 * @param code
	 * @param message
	 * @return
	 */
	public static <T> ResponseResult<T> error(int code, String message) {
		return ResponseResult.<T>builder().code(code).message(message).timestamp(System.currentTimeMillis()).build();
	}

}

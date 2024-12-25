package com.qrcode.model.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserVo 前端傳入使用者資訊
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVo {

	/**
	 * account 帳號
	 */
	@NotBlank(message = "帳號不能為空")
	private String account;

	/**
	 * password 密碼
	 */
	@NotBlank(message = "密碼不能為空")
	private String password;

	/**
	 * email 電子信箱
	 */
	@NotBlank(message = "電子信箱不能為空")
	private String email;
	
	/**
	 * code 驗證碼
	 */
	private String code;

}

package com.qrcode.model.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * VerificationVo 前端傳入驗證資訊
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationVo {

	/**
	 * email 電子信箱
	 */
	@NotBlank(message = "電子信箱不能為空")
	private String email;

	/**
	 * code 驗證碼
	 */
	@NotBlank(message = "驗證碼不能為空")
	private String code;

}

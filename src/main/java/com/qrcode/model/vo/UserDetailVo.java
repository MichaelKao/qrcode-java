package com.qrcode.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserDetailVo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailVo {

	/**
	 * seq 使用者序號
	 */
	private Long seq;

	/**
	 * account 帳號
	 */
	private String account;

	/**
	 * password 密碼
	 */
	private String password;

	/**
	 * email 電子信箱
	 */
	private String email;

	private UserStoreVo userStoreVo;

}

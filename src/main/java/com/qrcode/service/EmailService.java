package com.qrcode.service;

import com.qrcode.model.ResponseResult;

import jakarta.mail.MessagingException;

public interface EmailService {

	/**
	 * verificationCode 發送驗證碼
	 * 
	 * @param email 使用者電子信箱
	 * @throws MessagingException
	 */
	ResponseResult<String> verificationCode(String email) throws MessagingException;

	/**
	 * sendPassword 寄送密碼
	 * 
	 * @param email 電子信箱
	 * @throws MessagingException
	 */
	ResponseResult<String> sendPassword(String email);

}

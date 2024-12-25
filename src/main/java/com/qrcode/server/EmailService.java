package com.qrcode.server;

import jakarta.mail.MessagingException;

public interface EmailService {

	/**
	 * verificationCode 發送驗證碼
	 * 
	 * @param email 使用者電子信箱
	 * @throws MessagingException
	 */
	void verificationCode(String email) throws MessagingException;

	/**
	 * sendPassword 寄送密碼
	 * 
	 * @param email 電子信箱
	 * @throws MessagingException
	 */
	void sendPassword(String email) throws MessagingException;

}

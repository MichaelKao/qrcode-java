package com.qrcode.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.qrcode.model.po.User;
import com.qrcode.repository.UserRepository;
import com.qrcode.service.EmailService;
import com.qrcode.util.VerificationCodeUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private VerificationCodeUtil verificationCode;

	@Autowired
	private UserRepository userRepository;

	@Value("${spring.mail.username}")
	private String fromEmail;

	private final String SUBJECT = "掃一掃通知您";

	/**
	 * verificationCode 發送驗證碼
	 * 
	 * @param email 使用者電子信箱
	 * @throws MessagingException
	 */
	public void verificationCode(String email) throws MessagingException {
		// 生成6位數驗證碼
		String code = verificationCode.generateCode();
		// 內文
		String content = String.format("您的驗證碼是: %s，10分鐘內有效", code);

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		// 寄件人
		helper.setFrom(fromEmail);
		// 收件人
		helper.setTo(email);
		// 標題
		helper.setSubject(SUBJECT);
		// 內文
		helper.setText(content);

		try {
			mailSender.send(message);
			// 保存驗證碼
			verificationCode.saveCode(email, code);
		} catch (Exception e) {
			throw new RuntimeException("發送郵件失敗: " + e.getMessage());
		}
	}

	/**
	 * sendPassword 寄送密碼
	 * 
	 * @param email 電子信箱
	 * @throws MessagingException
	 */
	public void sendPassword(String email) throws MessagingException {

		Optional<User> userOptional = userRepository.findByEmail(email);

		if (userOptional.isPresent()) {
			// 帳號資訊
			User user = userOptional.get();
			// 密碼
			String password = user.getPassword();
			// 內文
			String content = String.format("您的密碼是: %s", password);

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			// 寄件人
			helper.setFrom(fromEmail);
			// 收件人
			helper.setTo(email);
			// 標題
			helper.setSubject(SUBJECT);
			// 內文
			helper.setText(content);
			mailSender.send(message);
		}

	}

}

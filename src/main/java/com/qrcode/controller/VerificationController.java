package com.qrcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qrcode.model.ResponseResult;
import com.qrcode.service.EmailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/qrcode/verification")
public class VerificationController {

	@Autowired
	private EmailService emailService;

	/**
	 * sendCode 發送驗證碼
	 * 
	 * @param email 使用者電子信箱
	 * @return 發送結果
	 * @throws MessagingException
	 */
	@GetMapping("/send/{email}")
	@Operation(summary = "發送驗證碼", description = "發送驗證碼")
	@ApiResponse(responseCode = "200", description = "OK")
	public ResponseEntity<ResponseResult<String>> sendCode(@PathVariable String email) throws MessagingException {
		// 發送驗證碼
		return ResponseEntity.ok(emailService.verificationCode(email));
	}

}

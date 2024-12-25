package com.qrcode.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qrcode.model.ResponseResult;
import com.qrcode.model.vo.VerificationVo;
import com.qrcode.server.EmailService;
import com.qrcode.util.VerificationCodeUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/qrcode/verification")
public class VerificationController {

	@Autowired
	private EmailService emailService;

	@Autowired
	private VerificationCodeUtil verificationCode;

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
	public ResponseResult<String> sendCode(@PathVariable String email) throws MessagingException {

		if (StringUtils.isBlank(email)) {
			return ResponseResult.error(400, "郵箱不能為空");
		}
		// 發送驗證碼
		emailService.verificationCode(email);

		return ResponseResult.success("驗證碼已發送");
	}

	/**
	 * verifyCode 驗證碼確認
	 * 
	 * @param email 使用者電子信箱
	 * @param code  驗證碼
	 * @return 驗證結果
	 */
	@PostMapping("/verify")
	@Operation(summary = "驗證碼確認", description = "驗證碼確認")
	@ApiResponse(responseCode = "200", description = "OK")
	public ResponseResult<String> verifyCode(@RequestBody @Valid VerificationVo request) {

		if (StringUtils.isBlank(request.getEmail()) || StringUtils.isBlank(request.getCode())) {
			return ResponseResult.error(400, "參數不完整");
		}

		boolean isValid = verificationCode.verifyCode(request);
		if (isValid) {
			return ResponseResult.success("驗證成功");
		} else {
			return ResponseResult.error(400, "驗證碼錯誤或已過期");
		}
	}

}

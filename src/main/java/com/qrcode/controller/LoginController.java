package com.qrcode.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qrcode.model.ResponseResult;
import com.qrcode.model.vo.UserDetailVo;
import com.qrcode.model.vo.UserVo;
import com.qrcode.service.EmailService;
import com.qrcode.service.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/qrcode/user")
public class LoginController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private EmailService emailService;

	/**
	 * signIn 使用者註冊
	 * 
	 * @param userVo 前端傳入使用者資訊
	 * @return 註冊結果
	 */
	@PostMapping(value = "/signIn", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "註冊", description = "使用者註冊")
	@ApiResponse(responseCode = "200", description = "註冊成功")
	@ApiResponse(responseCode = "401", description = "註冊失敗")
	public ResponseEntity<ResponseResult<UserDetailVo>> signIn(@RequestBody @Valid UserVo userVo) {
		// 使用者註冊
		return ResponseEntity.ok(loginService.signIn(userVo));

	}

	/**
	 * login 使用者登入
	 * 
	 * @param userVo 前端傳入使用者資訊
	 * @return 登入結果
	 * @throws IOException
	 */
	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "登入", description = "使用者登入")
	@ApiResponse(responseCode = "200", description = "登入成功")
	@ApiResponse(responseCode = "401", description = "登入失敗")
	public ResponseEntity<ResponseResult<UserDetailVo>> login(@RequestBody UserVo userVo) throws IOException {
		// 使用者登入
		return ResponseEntity.ok(loginService.login(userVo));

	}

	/**
	 * forgetPassword 忘記密碼
	 * 
	 * @param email 電子信箱
	 * @return
	 * @throws MessagingException
	 */
	@GetMapping(value = "/forgetPassword/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "忘記密碼", description = "忘記密碼")
	@ApiResponse(responseCode = "200", description = "OK")
	public ResponseEntity<ResponseResult<String>> forgetPassword(@PathVariable String email) {
		// 寄送密碼
		return ResponseEntity.ok(emailService.sendPassword(email));

	}

}

package com.qrcode.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qrcode.model.ResponseResult;
import com.qrcode.model.po.User;
import com.qrcode.model.vo.UserDetailVo;
import com.qrcode.model.vo.UserVo;
import com.qrcode.server.EmailService;
import com.qrcode.server.LoginService;

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

		return ResponseEntity.ok(ResponseResult.success(loginService.signIn(userVo)));

	}

	/**
	 * login 使用者登入
	 * 
	 * @param userVo 前端傳入使用者資訊
	 * @return 登入結果
	 */
	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "登入", description = "使用者登入")
	@ApiResponse(responseCode = "200", description = "登入成功")
	@ApiResponse(responseCode = "401", description = "登入失敗")
	public ResponseEntity<ResponseResult<UserDetailVo>> login(@RequestBody UserVo userVo) {

		UserDetailVo user = loginService.login(userVo);

		if (Objects.isNull(user)) {
			return ResponseEntity.ok(ResponseResult.error(401, "登入失敗"));
		}

		return ResponseEntity.ok(ResponseResult.success(user));

	}

	/**
	 * updateUserDetail 使用者資訊變更
	 * 
	 * @param userVo 前端傳入使用者資訊
	 * @return 變更結果
	 */
	@PutMapping(value = "/updateUserDetail", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "變更email", description = "變更email")
	@ApiResponse(responseCode = "200", description = "變更成功")
	@ApiResponse(responseCode = "401", description = "變更失敗")
	public ResponseEntity<ResponseResult<String>> updateUserDetail(@RequestBody UserVo userVo) {

		if (!loginService.updateUserDetail(userVo)) {
			return ResponseEntity.ok(ResponseResult.error(401, "變更失敗"));
		}

		return ResponseEntity.ok(ResponseResult.success("變更成功"));

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
	public ResponseEntity<ResponseResult<String>> forgetPassword(@PathVariable String email) throws MessagingException {

		emailService.sendPassword(email);

		return ResponseEntity.ok(ResponseResult.success("驗證碼以寄送至信箱"));

	}

}

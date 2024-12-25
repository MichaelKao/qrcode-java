package com.qrcode.server.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qrcode.model.po.User;
import com.qrcode.model.vo.UserVo;
import com.qrcode.model.vo.VerificationVo;
import com.qrcode.repository.UserRepository;
import com.qrcode.server.LoginService;
import com.qrcode.util.VerificationCodeUtil;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VerificationCodeUtil verificationCodeUtil;

	/**
	 * signIn 使用者註冊
	 * 
	 * @param userVo 前端傳入使用者資訊
	 * @return
	 */
	@Override
	public User signIn(UserVo userVo) {
		// 帳號
		String account = userVo.getAccount();
		// 電子信箱
		String email = userVo.getEmail();
		// 驗證碼
		String code = userVo.getCode();
		// 以帳號查詢使用者資訊
		Optional<User> userOptional = userRepository.findByAccount(account);
		// 檢查使用者是否存在
		if (userOptional.isPresent()
				|| verificationCodeUtil.verifyCode(VerificationVo.builder().email(email).code(code).build())) {

			return null;
		} else {

			User user = new User();
			// 前端傳入vo轉成資料庫所需po
			BeanUtils.copyProperties(userVo, user);
			// 建立時間
			user.setCreateTime(LocalDateTime.now());
			// 修改時間
			user.setUpdateTime(LocalDateTime.now());
			// 資料不存在則新增
			userRepository.saveAndFlush(user);

			return user;
		}
	}

	/**
	 * login 使用者登入
	 * 
	 * @param userVo 前端傳入使用者資訊
	 * @return
	 */
	@Override
	public User login(UserVo userVo) {
		// 帳號
		String account = userVo.getAccount();
		// 密碼
		String password = userVo.getPassword();
		// 以帳號查詢使用者資訊
		Optional<User> userOptional = userRepository.findByAccountAndPassword(account, password);
		// 檢查使用者是否存在
		if (userOptional.isPresent()) {

			return userOptional.get();
		} else {
			return null;
		}

	}

	/**
	 * updateUserDetail 使用者資訊變更
	 * 
	 * @param userVo 前端傳入使用者資訊
	 */
	@Override
	public boolean updateUserDetail(UserVo userVo) {
		// 帳號
		String account = userVo.getAccount();
		// 以帳號查詢使用者資訊
		Optional<User> userOptional = userRepository.findByAccount(account);
		// 檢查使用者是否存在
		if (userOptional.isPresent()) {

			User user = userOptional.get();
			// 密碼
			String password = userVo.getPassword();
			// 電子信箱
			String email = userVo.getEmail();
			// 更新密碼
			user.setPassword(password);
			// 更新電子信箱
			user.setEmail(email);
			// 更新
			userRepository.saveAndFlush(user);

			return true;
		} else {
			return false;
		}

	}

}

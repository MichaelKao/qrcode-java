package com.qrcode.service;

import java.io.IOException;

import com.qrcode.model.vo.UserDetailVo;
import com.qrcode.model.vo.UserVo;

public interface LoginService {

	/**
	 * signIn 使用者註冊
	 * 
	 * @param userVo 前端傳入使用者資訊
	 * @return
	 */
	UserDetailVo signIn(UserVo userVo);

	/**
	 * login 使用者登入
	 * 
	 * @param userVo 前端傳入使用者資訊
	 * @return
	 */
	UserDetailVo login(UserVo userVo) throws IOException;

	/**
	 * updateUserDetail 使用者資訊變更
	 * 
	 * @param userVo 前端傳入使用者資訊
	 */
	boolean updateUserDetail(UserVo userVo);
	
	UserDetailVo getUserDetailBySeq(Long seq)  throws Exception;

}

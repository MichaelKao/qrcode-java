package com.qrcode.server;

import com.qrcode.model.po.User;
import com.qrcode.model.vo.UserVo;

public interface LoginService {

	/**
	 * signIn 使用者註冊
	 * 
	 * @param userVo 前端傳入使用者資訊
	 * @return
	 */
	User signIn(UserVo userVo);

	/**
	 * login 使用者登入
	 * 
	 * @param userVo 前端傳入使用者資訊
	 * @return
	 */
	User login(UserVo userVo);

	/**
	 * updateUserDetail 使用者資訊變更
	 * 
	 * @param userVo 前端傳入使用者資訊
	 */
	boolean updateUserDetail(UserVo userVo);

}

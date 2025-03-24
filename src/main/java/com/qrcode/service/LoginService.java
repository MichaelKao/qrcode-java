package com.qrcode.service;

import java.io.IOException;

import com.qrcode.model.ResponseResult;
import com.qrcode.model.vo.UserDetailVo;
import com.qrcode.model.vo.UserVo;

public interface LoginService {

	/**
	 * signIn 使用者註冊
	 * 
	 * @param userVo 前端傳入使用者資訊
	 * @return
	 */
	ResponseResult<UserDetailVo> signIn(UserVo userVo);

	/**
	 * login 使用者登入
	 * 
	 * @param userVo 前端傳入使用者資訊
	 * @return
	 */
	ResponseResult<UserDetailVo> login(UserVo userVo) throws IOException;

	/**
	 * getUserDetailBySeq 用使用者序號查詢使用者資訊
	 * 
	 * @param seq 使用者序號
	 * @return 使用者資訊
	 * @throws Exception
	 */
	ResponseResult<UserDetailVo> getUserDetailBySeq(Long seq) throws IOException;

}

package com.qrcode.util;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qrcode.model.po.VerificationCode;
import com.qrcode.model.vo.VerificationVo;
import com.qrcode.repository.VerificationCodeRepository;

@Component
public class VerificationCodeUtil {

	@Autowired
	private VerificationCodeRepository verificationCodeRepository;

	/**
	 * generateCode生成6位數驗證碼
	 * 
	 * @return 驗證碼
	 */
	public String generateCode() {
		Random random = new Random();
		StringBuilder code = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			code.append(random.nextInt(10));
		}
		return code.toString();
	}

	/**
	 * saveCode 保存驗證碼
	 * 
	 * @param email
	 * @param code
	 */
	public void saveCode(String email, String code) {
		// 保存驗證碼
		verificationCodeRepository.saveAndFlush(
				VerificationCode.builder().email(email).code(code).createTime(LocalDateTime.now()).build());

	}

	/**
	 * verifyCode 驗證碼是否有效
	 * 
	 * @param request 使用者信箱/驗證碼
	 * @return 驗證結果
	 */
	public boolean verifyCode(VerificationVo request) {
		// 使用者信箱
		String email = request.getEmail();
		// 驗證碼
		String code = request.getCode();
		// 以電子信箱查詢驗證碼資訊
		List<VerificationCode> verificationCodeList = verificationCodeRepository.findByEmailOrderByCreateTimeDesc(email);
		// 當無資料時,報錯
		if (verificationCodeList.isEmpty()) {
			return false;
		} else {
			// 只取最新一筆資料
			VerificationCode verificationCode = verificationCodeList.stream().findFirst().orElse(null);
			// 取得驗證碼
			String queryCode = verificationCode.getCode();
			// 比較驗證碼
			return StringUtils.equals(code, queryCode);
		}

	}

}

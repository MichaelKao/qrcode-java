package com.qrcode.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qrcode.model.po.VerificationCode;
import com.qrcode.repository.VerificationCodeRepository;

@Component
public class batchScheduler {

	@Autowired
	private VerificationCodeRepository verificationCodeRepository;

	/**
	 * cleanExpiredCodes 每10分鐘執行一次
	 */
	@Scheduled(fixedRate = 600000)
	@Transactional
	public void cleanExpiredCodes() {
		try {
			LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

			List<VerificationCode> verificationCodeList = verificationCodeRepository.findAll().stream()
					.filter(code -> code.getCreateTime().isBefore(tenMinutesAgo)).toList();
			// 刪除10分鐘以前的驗證碼資訊
			verificationCodeRepository.deleteAll(verificationCodeList);

		} catch (Exception e) {

		}
	}

}

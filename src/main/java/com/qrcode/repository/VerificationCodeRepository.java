package com.qrcode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qrcode.model.po.VerificationCode;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

	/**
	 * findByEmailOrderByCreateTimeDesc 以電子信箱查詢驗證碼資訊
	 * 
	 * @param email 電子信箱
	 * @return 驗證碼資訊
	 */
	List<VerificationCode> findByEmailOrderByCreateTimeDesc(String email);

}

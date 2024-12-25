package com.qrcode.model.po;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * VerificationCode 驗證碼
 */
@Data
@Entity
@Table(name = "verification_code")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationCode {

	/**
	 * seq 使用者序號
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq")
	private Long seq;

	/**
	 * 
	 */
	@Column(name = "email")
	private String email;

	/**
	 * 
	 */
	@Column(name = "code")
	private String code;

	/**
	 * 
	 */
	@Column(name = "create_time")
	private LocalDateTime createTime;

}

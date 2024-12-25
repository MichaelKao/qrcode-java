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
 * User 使用者
 */
@Data
@Entity
@Table(name = "user_t")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

	/**
	 * seq 使用者序號
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq")
	private Long seq;

	/**
	 * account 帳號
	 */
	@Column(name = "account")
	private String account;

	/**
	 * password 密碼
	 */
	@Column(name = "password")
	private String password;

	/**
	 * email 電子信箱
	 */
	@Column(name = "email")
	private String email;

	/**
	 * createTime 建立時間
	 */
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * updateTime 修改時間
	 */
	@Column(name = "update_time")
	private LocalDateTime updateTime;

}

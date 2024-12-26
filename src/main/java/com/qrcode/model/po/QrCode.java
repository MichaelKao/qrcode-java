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
 * QrCode qrcode
 */
@Data
@Entity
@Table(name = "qrcode_t")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QrCode {

	/**
	 * seq 序號
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq")
	private Long seq;

	/**
	 * userSeq 商店序號 關聯user_t.seq
	 */
	@Column(name = "store_seq")
	private Long storeSeq;

	/**
	 * qrcode qrcode
	 */
	@Column(name = "qrcode")
	private String qrcode;

	/**
	 * num 位子
	 */
	@Column(name = "num")
	private int num;

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

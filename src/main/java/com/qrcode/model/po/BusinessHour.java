package com.qrcode.model.po;

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
 * business_hour_t 營業時間
 */
@Data
@Entity
@Table(name = "business_hour_t")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessHour {

	/**
	 * seq 序號
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq")
	private Long seq;

	/**
	 * userSeq 商店序號 關聯store_t.seq
	 */
	@Column(name = "store_seq")
	private Long storeSeq;

	/**
	 * isOpen 是否營業
	 */
	@Column(name = "is_open")
	private boolean isOpen;

	/**
	 * day 星期幾
	 */
	@Column(name = "week")
	private String week;

	/**
	 * openTime 開始時間
	 */
	@Column(name = "open_time")
	private String openTime;

	/**
	 * closeTime 結束時間
	 */
	@Column(name = "close_time")
	private String closeTime;

}

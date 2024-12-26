package com.qrcode.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BusinessHourVo 營業時間
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessHourVo {

	/**
	 * seq 序號
	 */
	private Long seq;

	/**
	 * userSeq 商店序號 關聯store_t.seq
	 */
	private Long storeSeq;

	/**
	 * isOpen 是否營業
	 */
	@JsonProperty("isOpen")
	private boolean isOpen;

	/**
	 * day 星期幾
	 */
	private String week;

	/**
	 * openTime 開始時間
	 */
	private String openTime;

	/**
	 * closeTime 結束時間
	 */
	private String closeTime;

}

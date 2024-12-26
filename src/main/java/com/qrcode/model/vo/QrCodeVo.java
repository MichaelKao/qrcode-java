package com.qrcode.model.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * QrCodeVo qrcode
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QrCodeVo {

	/**
	 * seq 序號
	 */
	private Long seq;

	/**
	 * userSeq 商店序號 關聯user_t.seq
	 */
	private Long storeSeq;

	/**
	 * qrcode qrcode
	 */
	private String qrcode;

	/**
	 * num 位子
	 */
	private int num;

	/**
	 * createTime 建立時間
	 */
	private LocalDateTime createTime;

	/**
	 * updateTime 修改時間
	 */
	private LocalDateTime updateTime;

}

package com.qrcode.model.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderVo {

	/**
	 * orderId 訂單編號
	 */
	private String orderId;

	/**
	 * storeSeq 商店序號 關聯store_t.seq
	 */
	private Long storeSeq;
	
	/**
	 * tableNum 座位號碼
	 */
	private Integer tableNum;

	/**
	 * totalAmount 訂單總金額
	 */
	private BigDecimal totalAmount;

	/**
	 * status 訂單狀態 (PENDING/ACCEPTED/REJECTED/COMPLETED/CANCELLED)
	 */
	private String status;

	/**
	 * createTime 建立時間
	 */
	private LocalDateTime createTime;

	private List<OrderItemVo> items;

}

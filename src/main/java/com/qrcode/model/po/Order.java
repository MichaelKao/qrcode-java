package com.qrcode.model.po;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.qrcode.model.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Order 訂單資訊
 */
@Data
@Entity
@Table(name = "orders_t")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
	/**
	 * seq 序號
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq")
	private Long seq;

	/**
	 * orderId 訂單編號
	 */
	@Column(name = "order_id", unique = true, nullable = false)
	private String orderId;

	/**
	 * storeSeq 商店序號 關聯store_t.seq
	 */
	@Column(name = "store_seq", nullable = false)
	private Long storeSeq;

	/**
	 * tableNum 座位號碼
	 */
	@Column(name = "table_num")
	private Integer tableNum;

	/**
	 * totalAmount 訂單總金額
	 */
	@Column(name = "total_amount", nullable = false)
	private BigDecimal totalAmount;

	/**
	 * status 訂單狀態 (pending/accepted/rejected/completed/cancelled)
	 */
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	/**
	 * createTime 建立時間
	 */
	@Column(name = "create_time", nullable = false)
	private LocalDateTime createTime;

	/**
	 * updateTime 修改時間
	 */
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * rejectReason 拒絕理由
	 */
	@Column(name = "reject_reason")
	private String rejectReason;
}
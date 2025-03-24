package com.qrcode.model.po;

import java.math.BigDecimal;
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
 * OrderItem 訂單明細
 */
@Data
@Entity
@Table(name = "order_items_t")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem {
	
	/**
	 * seq 序號
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq")
	private Long seq;

	/**
	 * orderSeq 訂單序號 關聯orders_t.seq
	 */
	@Column(name = "order_seq", nullable = false)
	private Long orderSeq;

	/**
	 * productSeq 商品序號
	 */
	@Column(name = "product_seq", nullable = false)
	private Long productSeq;

	/**
	 * productName 商品名稱
	 */
	@Column(name = "product_name", nullable = false)
	private String productName;

	/**
	 * quantity 數量
	 */
	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	/**
	 * price 單價
	 */
	@Column(name = "price", nullable = false)
	private BigDecimal price;

	/**
	 * spicyLevel 辣度(none/small/medium/large)
	 */
	@Column(name = "spicy_level")
	private String spicyLevel;

	/**
	 * wantsCoriander 是否要香菜
	 */
	@Column(name = "wants_coriander")
	private Boolean wantsCoriander;

	/**
	 * wantsVinegar 是否要醋
	 */
	@Column(name = "wants_vinegar")
	private Boolean wantsVinegar;

	/**
	 * createTime 建立時間
	 */
	@Column(name = "create_time", nullable = false)
	private LocalDateTime createTime;
}

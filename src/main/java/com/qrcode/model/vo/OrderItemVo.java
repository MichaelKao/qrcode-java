package com.qrcode.model.vo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OrderItemVo 訂單明細
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemVo {

	/**
	 * productSeq 商品序號
	 */
	private Long productSeq;

	/**
	 * productName 商品名稱
	 */
	private String productName;

	/**
	 * quantity 數量
	 */
	private Integer quantity;

	/**
	 * price 單價
	 */
	private BigDecimal price;

	/**
	 * spicyLevel 辣度(none/small/medium/large)
	 */
	private String spicyLevel;

	/**
	 * wantsCoriander 是否要香菜
	 */
	private Boolean wantsCoriander;

	/**
	 * wantsVinegar 是否要醋
	 */
	private Boolean wantsVinegar;

}

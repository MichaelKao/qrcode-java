package com.qrcode.model.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CreateStoreVo 商品資訊
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProductVo {

	/**
	 * seq 序號
	 */
	private Long seq;

	/**
	 * productSeq 商品序號 關聯store_t.seq
	 */
	private Long productSeq;

	/**
	 * productName 商品名稱
	 */
	private String productName;

	/**
	 * productPrice 商品價格
	 */
	private Integer productPrice;

	/**
	 * description 商品描述
	 */
	private String description;

	/**
	 * sortOrder 商品排序
	 */
	private Integer sortOrder;

	/**
	 * spicy 辣(可選)
	 */
	private boolean spicy;

	/**
	 * coriander 香菜(可選)
	 */
	private boolean coriander;

	/**
	 * vinegar 醋(可選)
	 */
	private boolean vinegar;

	/**
	 * picture 商品圖片
	 */
	private String picture;

	/**
	 * createTime 建立時間
	 */
	private LocalDateTime createTime;

	/**
	 * updateTime 修改時間
	 */
	private LocalDateTime updateTime;

}

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
 * Product 商品
 */
@Data
@Entity
@Table(name = "product_t")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

	/**
	 * seq 序號
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq")
	private Long seq;

	/**
	 * productSeq 商品序號 關聯store_t.seq
	 */
	@Column(name = "product_seq")
	private Long productSeq;

	/**
	 * productName 商品名稱
	 */
	@Column(name = "product_name")
	private String productName;

	/**
	 * productPrice 商品價格
	 */
	@Column(name = "product_price")
	private Integer productPrice;

	/**
	 * description 商品描述
	 */
	@Column(name = "description")
	private String description;
	
	/**
	 * spicy 辣(可選)
	 */
	@Column(name = "spicy")
	private boolean spicy;
	
	/**
	 * coriander 香菜(可選)
	 */
	@Column(name = "coriander")
	private boolean coriander;
	
	/**
	 * vinegar 醋(可選)
	 */
	@Column(name = "vinegar")
	private boolean vinegar;
	
	/**
	 * picture 商品圖片
	 */
	@Column(name = "picture")
	private String picture;

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

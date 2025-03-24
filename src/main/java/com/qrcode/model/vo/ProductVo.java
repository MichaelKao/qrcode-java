package com.qrcode.model.vo;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ProductVo {

	/**
	 * seq 商品序號
	 */
	private Long seq;
	
	/**
	 * storeSeq 商品序號 關聯store_t.seq
	 */
	@NotNull(message = "商店序號不能為空")
	private Long productSeq;

	/**
	 * productName 商品名稱
	 */
	@NotBlank(message = "商品名稱不能為空")
	private String productName;

	/**
	 * productPrice 商品價格
	 */
	@NotNull(message = "商品價格不能為空")
	@Min(value = 0, message = "商品價格必須大於等於0")
	private Integer productPrice;

	/**
	 * description 商品描述
	 */
	private String description;

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
	 * pictureFileName 商品圖片
	 */
	private MultipartFile  pictureFile;

}

package com.qrcode.model.vo;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CreateStoreVo 商店資訊
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreVo {

	/**
	 * seq 商店序號
	 */
	private Long seq;

	/**
	 * storeSeq 商店序號 關聯user_t.seq
	 */
	private Long storeSeq;

	/**
	 * storeName 商店名稱
	 */
	@NotBlank(message = "商店名稱不能為空")
	private String storeName;

	/**
	 * description 商店描述
	 */
	private String description;

	/**
	 * phone 商店號碼
	 */
	private String phone;

	/**
	 * email 商店信箱
	 */
	private String email;

	/**
	 * address 商店地址
	 */
	private String address;

	/**
	 * businessHours 商店營業時間
	 */
	private String businessHours;

	/**
	 * seats 商店座位
	 */
	@Column(name = "seats")
	private int seats;

	/**
	 * logo 商店商標
	 */
	private Optional<MultipartFile> logo;

}

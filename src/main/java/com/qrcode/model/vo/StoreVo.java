package com.qrcode.model.vo;

import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
	 * postCode 郵遞區號
	 */
    @NotBlank(message = "郵遞區號不能為空")
    private String postCode;
    
	/**
	 * city 縣市
	 */
	@NotBlank(message = "縣市不能為空")
    private String city;

	/**
	 * district 鄉鎮區域
	 */
    @NotBlank(message = "鄉鎮區域不能為空")  
    private String district;

	/**
	 * streetAddress 詳細地址
	 */
    @NotBlank(message = "詳細地址不能為空")
    private String streetAddress;

    /**
     * businessHours 營業時間
     */
    @NotEmpty(message = "營業時間不能為空")
    private String businessHours;

	/**
	 * seats 商店座位
	 */
    @NotNull(message = "座位數量不能為空")
	@Column(name = "seats")
	private int seats;

	/**
	 * logo 商店商標
	 */
    @Nullable
	private MultipartFile logo;

}

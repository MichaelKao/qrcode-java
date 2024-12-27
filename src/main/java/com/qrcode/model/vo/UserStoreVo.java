package com.qrcode.model.vo;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserStoreVo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStoreVo {

	private Long seq;

	/**
	 * userSeq 商店序號 關聯user_t.seq
	 */
	private Long storeSeq;

	/**
	 * storeName 商店名稱
	 */
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
    private String postCode;
    
	/**
	 * city 縣市
	 */
    private String city;

	/**
	 * district 鄉鎮區域
	 */
    private String district;

	/**
	 * streetAddress 詳細地址
	 */
    private String streetAddress;

	/**
	 * businessHours 商店營業時間
	 */
	private List<BusinessHourVo> businessHoursList;

	/**
	 * logo 商店商標
	 */
	private String logo;

	/**
	 * seats 商店座位
	 */
	private int seats;

	/**
	 * qrcode 商店qrcode
	 */
	private String qrcode;

	/**
	 * createTime 建立時間
	 */
	private LocalDateTime createTime;

	/**
	 * updateTime 修改時間
	 */
	private LocalDateTime updateTime;

	private List<UserProductVo> userProductVoList;

	private List<QrCodeVo> qrCodeVoList;

}

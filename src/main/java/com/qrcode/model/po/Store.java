package com.qrcode.model.po;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Store 商店資訊
 */
@Data
@Entity
@Table(name = "store_t")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Store {

	/**
	 * seq 序號
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq")
	private Long seq;

	/**
	 * userSeq 商店序號 關聯user_t.seq
	 */
	@Column(name = "store_seq")
	private Long storeSeq;

	/**
	 * storeName 商店名稱
	 */
	@Column(name = "store_name")
	private String storeName;

	/**
	 * description 商店描述
	 */
	@Column(name = "description")
	private String description;

	/**
	 * phone 商店號碼
	 */
	@Column(name = "phone")
	private String phone;

	/**
	 * email 商店信箱
	 */
	@Column(name = "email")
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
	 * logo 商店商標
	 */
	@Column(name = "logo")
	private String logo;
	
	/**
	 * seats 商店座位
	 */
	@Column(name = "seats")
	private int seats;

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

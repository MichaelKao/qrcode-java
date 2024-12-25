package com.qrcode.model.vo;

import java.util.List;

import com.qrcode.model.po.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * StoreAndProductVo 商店與商品資訊
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreAndProductVo {

	private StoreVo storeVo;

	private List<Product> productList;

}

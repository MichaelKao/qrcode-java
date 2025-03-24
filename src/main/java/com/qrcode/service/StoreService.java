package com.qrcode.service;

import java.io.IOException;

import com.qrcode.model.ResponseResult;
import com.qrcode.model.po.Store;
import com.qrcode.model.vo.ProductVo;
import com.qrcode.model.vo.StoreVo;
import com.qrcode.model.vo.UserDetailVo;

public interface StoreService {

	/**
	 * createStore 創建商店
	 * 
	 * @param storeVo 商店資訊
	 * @throws Exception
	 */
	ResponseResult<UserDetailVo> createStore(StoreVo storeVo) throws Exception;

	/**
	 * viewStore 檢視商店
	 * 
	 * @param seq 商店序號
	 * @return 商店資訊
	 */
	ResponseResult<Store> viewStore(Long seq);

	/**
	 * updateStore 更新商店
	 * 
	 * @param storeVo
	 * @throws IOException
	 */
	ResponseResult<UserDetailVo> updateStore(StoreVo storeVo) throws Exception;

	/**
	 * createProduct 創建商品
	 * 
	 * @param product 商品資訊
	 * @throws IOException
	 */
	ResponseResult<UserDetailVo> createProduct(ProductVo product) throws IOException;

	/**
	 * updateProduct 更新商品資訊
	 * 
	 * @param product 商品
	 * @return
	 * @throws IOException
	 */
	ResponseResult<UserDetailVo> updateProduct(ProductVo product) throws IOException;

	/**
	 * deleteProduct 刪除商品資訊
	 * 
	 * @param seq 商品序號
	 * @return
	 */
	ResponseResult<Void> deleteProduct(Long seq);

}

package com.qrcode.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.qrcode.model.QrCodeResponse;
import com.qrcode.model.po.Store;
import com.qrcode.model.vo.ProductVo;
import com.qrcode.model.vo.StoreAndProductVo;
import com.qrcode.model.vo.StoreVo;
import com.qrcode.model.vo.UserDetailVo;

public interface StoreService {

	/**
	 * createStore 創建商店
	 * 
	 * @param storeVo 商店資訊
	 * @throws Exception
	 */
	UserDetailVo createStore(StoreVo storeVo) throws Exception;

	Store viewStore(Long seq);

	/**
	 * updateStore 更新商店
	 * 
	 * @param storeVo 商店資訊
	 * @throws IOException
	 */
	void updateStore(StoreVo storeVo) throws IOException;

	/**
	 * getStoreQRCode 獲取qrcode
	 * 
	 * @param seq 商店資訊序號
	 * @return qrcode
	 */
	ResponseEntity<List<QrCodeResponse>> getStoreQRCode(Long seq) throws MalformedURLException;

	/**
	 * getStoreLogo 獲取logo
	 * 
	 * @param seq 商店資訊序號
	 * @return logo
	 */
	ResponseEntity<Resource> getStoreLogo(Long seq) throws MalformedURLException;

	/**
	 * getProductLogo 獲取商品圖片
	 * 
	 * @param seq 商品資訊序號
	 * @return 商品圖片
	 * @throws MalformedURLException
	 */
	ResponseEntity<Resource> getProductLogo(Long seq) throws MalformedURLException;

	/**
	 * viewProduct 檢視商品資訊
	 * 
	 * @param seq 商店序號
	 * @return 商品資訊
	 */
	StoreAndProductVo viewProduct(Long seq);

	/**
	 * createProduct 創建商品
	 * 
	 * @param products 商品資訊
	 * @param files    商品圖片
	 * @throws IOException
	 */
	void createProduct(List<ProductVo> products, List<MultipartFile> files) throws IOException;

}

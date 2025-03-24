package com.qrcode.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qrcode.model.ResponseResult;
import com.qrcode.model.po.Store;
import com.qrcode.model.vo.ProductVo;
import com.qrcode.model.vo.StoreVo;
import com.qrcode.model.vo.UserDetailVo;
import com.qrcode.service.StoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/qrcode/store")
public class StoreController {

	@Autowired
	private StoreService storeService;

	/**
	 * createStore 創建商店
	 * 
	 * @param storeVo 商店資訊
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/createStore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "創建商店", description = "創建商店")
	@ApiResponse(responseCode = "200", description = "OK")
	public ResponseEntity<ResponseResult<UserDetailVo>> createStore(@ModelAttribute @Valid StoreVo storeVo)
			throws Exception {

		// 創建商店
		return ResponseEntity.ok(storeService.createStore(storeVo));
	}

	/**
	 * viewStore 檢視商店
	 * 
	 * @param seq 商店序號
	 * @return
	 * @throws IOException
	 */
	@GetMapping(value = "/viewStore/{seq}")
	@Operation(summary = "檢視商店", description = "檢視商店")
	@ApiResponse(responseCode = "200", description = "OK")
	public ResponseEntity<ResponseResult<Store>> viewStore(@PathVariable Long seq) throws IOException {

		// 檢視商店
		return ResponseEntity.ok(storeService.viewStore(seq));
	}

	/**
	 * updateStore 更新商店
	 * 
	 * @param storeVo 商店資訊
	 * @return
	 * @throws Exception
	 */
	@PutMapping(value = "/updateStore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "更新商店", description = "更新商店")
	@ApiResponse(responseCode = "200", description = "OK")
	public ResponseEntity<ResponseResult<UserDetailVo>> updateStore(@ModelAttribute StoreVo storeVo) throws Exception {

		// 更新商店
		return ResponseEntity.ok(storeService.updateStore(storeVo));
	}

	/**
	 * createProduct 創建商品資訊
	 * 
	 * @param productVo 商品
	 * @return
	 * @throws IOException
	 */
	@PostMapping(value = "/createProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "創建商品", description = "創建商品資訊")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "參數錯誤") })
	public ResponseEntity<ResponseResult<UserDetailVo>> createProduct(@ModelAttribute ProductVo productVo)
			throws IOException {

		return ResponseEntity.ok(storeService.createProduct(productVo));
	}

	/**
	 * updateProduct 更新商品資訊
	 * 
	 * @param productVo 商品
	 * @return
	 * @throws IOException
	 */
	@PutMapping(value = "/updateProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "更新商品", description = "更新商品")
	@ApiResponse(responseCode = "200", description = "OK")
	public ResponseEntity<ResponseResult<UserDetailVo>> updateProduct(@ModelAttribute ProductVo product)
			throws IOException {
		// 更新商品
		return ResponseEntity.ok(storeService.updateProduct(product));
	}

	/**
	 * deleteProduct 刪除商品資訊
	 * 
	 * @param productVo 商品
	 * @return
	 * @throws IOException
	 */
	@DeleteMapping(value = "/deleteProduct/{seq}")
	@Operation(summary = "更新商品", description = "更新商品")
	@ApiResponse(responseCode = "200", description = "OK")
	public ResponseEntity<ResponseResult<Void>> deleteProduct(@PathVariable Long seq) throws IOException {
		// 刪除商品
		return ResponseEntity.ok(storeService.deleteProduct(seq));
	}

}

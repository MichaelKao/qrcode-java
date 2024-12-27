package com.qrcode.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.qrcode.model.QrCodeResponse;
import com.qrcode.model.ResponseResult;
import com.qrcode.model.po.Store;
import com.qrcode.model.vo.ProductVo;
import com.qrcode.model.vo.StoreAndProductVo;
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
		return ResponseEntity.ok(ResponseResult.success(storeService.createStore(storeVo)));
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
		return ResponseEntity.ok(ResponseResult.success(storeService.viewStore(seq)));
	}

	/**
	 * updateStore 更新商店
	 * 
	 * @param storeVo 商店資訊
	 * @return
	 * @throws IOException
	 */
	@PutMapping(value = "/updateStore", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "更新商店", description = "更新商店")
	@ApiResponse(responseCode = "200", description = "OK")
	public ResponseEntity<ResponseResult<String>> updateStore(@ModelAttribute @Valid StoreVo storeVo)
			throws IOException {
		// 更新商店
		storeService.updateStore(storeVo);

		return ResponseEntity.ok(ResponseResult.success(""));
	}

	/**
	 * getProductLogo 獲取商品圖片
	 * 
	 * @param seq 商店資訊序號
	 * @return 商品圖片
	 * @throws IOException
	 */
	@GetMapping("/getProductLogo/{seq}")
	@Operation(summary = "獲取商品圖片", description = "獲取商品圖片")
	public ResponseEntity<Resource> getProductLogo(@PathVariable Long seq) throws IOException {

		return storeService.getProductLogo(seq);
	}

	@PostMapping(value = "/createProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "創建商品", description = "創建商品資訊")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
			@ApiResponse(responseCode = "400", description = "參數錯誤") })
	public ResponseEntity<ResponseResult<String>> createProduct(@RequestPart("products") List<ProductVo> products,
			@RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {

		storeService.createProduct(products, files);

		return ResponseEntity.ok(ResponseResult.success("創建成功"));
	}

	/**
	 * viewProduct 檢視商店商品資訊
	 * 
	 * @param seq 商店序號
	 * @return
	 * @throws IOException
	 */
	@GetMapping(value = "/viewProduct/{seq}")
	@Operation(summary = "檢視商品", description = "檢視商品")
	@ApiResponse(responseCode = "200", description = "OK")
	public ResponseEntity<ResponseResult<StoreAndProductVo>> viewProduct(@PathVariable Long seq) throws IOException {
		// 檢視商品資訊
		return ResponseEntity.ok(ResponseResult.success(storeService.viewProduct(seq)));
	}

//	/**
//	 * updateProduct 更新商品資訊
//	 * 
//	 * @param store 商店資訊
//	 * @return
//	 * @throws IOException
//	 */
//	@PostMapping(value = "/updateProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	@Operation(summary = "更新商品", description = "更新商品")
//	@ApiResponse(responseCode = "200", description = "OK")
//	public ResponseEntity<ResponseResult<String>> updateProduct(
//			@ModelAttribute @Valid List<ProductVo> createProductVoList) throws IOException {
//		// 創建商店
//		storeService.createProduct(createProductVoList);
//
//		return ResponseEntity.ok(ResponseResult.success(""));
//	}

}

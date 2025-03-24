package com.qrcode.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qrcode.model.ResponseResult;
import com.qrcode.model.vo.OrderVo;
import com.qrcode.model.vo.UserDetailVo;
import com.qrcode.service.LoginService;
import com.qrcode.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/qrcode/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private LoginService loginService;

	@GetMapping(value = "/getShopInfo/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "取得商店商品資訊", description = "取得商店商品資訊")
	@ApiResponse(responseCode = "200", description = "OK")
	public ResponseEntity<ResponseResult<UserDetailVo>> getShopInfo(@PathVariable Long userId) throws IOException {

		return ResponseEntity.ok(loginService.getUserDetailBySeq(userId));

	}

	@GetMapping(value = "/getOrderInfo/{storeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "取得商店商品資訊", description = "取得商店商品資訊")
	@ApiResponse(responseCode = "200", description = "OK")
	public ResponseEntity<ResponseResult<List<OrderVo>>> getOrderInfo(@PathVariable Long storeId) throws IOException {
		// 寄送密碼
		return ResponseEntity.ok(orderService.getOrderInfo(storeId));

	}

}

package com.qrcode.service;

import java.util.List;

import com.qrcode.model.ResponseResult;
import com.qrcode.model.vo.OrderVo;

public interface OrderService {

	ResponseResult<List<OrderVo>> getOrderInfo(Long storeId);

}

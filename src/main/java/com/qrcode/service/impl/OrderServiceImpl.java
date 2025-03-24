package com.qrcode.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qrcode.model.ResponseResult;
import com.qrcode.model.po.Order;
import com.qrcode.model.po.OrderItem;
import com.qrcode.model.vo.OrderItemVo;
import com.qrcode.model.vo.OrderVo;
import com.qrcode.repository.OrderItemRepository;
import com.qrcode.repository.OrderRepository;
import com.qrcode.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Override
	public ResponseResult<List<OrderVo>> getOrderInfo(Long storeId) {

		List<Order> orderList = orderRepository.findByStoreSeq(storeId);

		if (orderList.isEmpty()) {
			return ResponseResult.<List<OrderVo>>builder().code(401).message("查無資料").build();
		}

		List<OrderVo> result = orderList.stream().map(order -> {
			OrderVo orderVo = new OrderVo();
			
			BeanUtils.copyProperties(order, orderVo);
			
			orderVo.setStatus(order.getStatus().toString().toLowerCase());

			List<OrderItem> orderItems = orderItemRepository.findByOrderSeq(order.getSeq());

			List<OrderItemVo> orderItemVos = orderItems.stream().map(item -> {
				OrderItemVo itemVo = new OrderItemVo();
				BeanUtils.copyProperties(item, itemVo);
				return itemVo;
			}).toList();

			orderVo.setItems(orderItemVos);
			return orderVo;
		}).toList();

		return ResponseResult.<List<OrderVo>>builder().code(200).data(result).build();

	}

}

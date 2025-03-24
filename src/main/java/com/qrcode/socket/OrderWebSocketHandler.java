package com.qrcode.socket;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qrcode.model.OrderStatus;
import com.qrcode.model.dto.OrderStatusUpdateDto;
import com.qrcode.model.po.Order;
import com.qrcode.model.po.OrderItem;
import com.qrcode.model.vo.OrderVo;
import com.qrcode.repository.OrderItemRepository;
import com.qrcode.repository.OrderRepository;

@Controller
public class OrderWebSocketHandler {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	private final ObjectMapper objectMapper;

	public OrderWebSocketHandler(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@MessageMapping("/order")
	@SendTo("/topic/orders")
	public OrderVo handleOrder(String orderJson) throws Exception {
		OrderVo orderVo = objectMapper.readValue(orderJson, OrderVo.class);
		// 訂單編號
		String orderId = orderVo.getOrderId();
		// 商店序號
		Long storeSeq = orderVo.getStoreSeq();

		Integer tableNum = orderVo.getTableNum();
		// 訂單總金額
		BigDecimal totalAmount = orderVo.getTotalAmount();
		// 確保新訂單狀態為 PENDING
		orderVo.setStatus(OrderStatus.PENDING.toString());
		// 訂單狀態 (PENDING/ACCEPTED/REJECTED/COMPLETED/CANCELLED)
		OrderStatus newStatus = OrderStatus.valueOf(orderVo.getStatus().toUpperCase());
		// 建立時間
		LocalDateTime createTime = LocalDateTime.now();
		// 新增訂單資訊
		Order order = orderRepository.save(Order.builder().orderId(orderId).storeSeq(storeSeq).tableNum(tableNum)
				.totalAmount(totalAmount).status(newStatus).createTime(createTime).updateTime(createTime).build());
		// 訂單序號 關聯orders_t.seq
		Long orderSeq = order.getSeq();
		// 新增訂單明細
		orderItemRepository.saveAll(orderVo.getItems().stream().map(orderItem -> {
			// 商品序號
			Long productSeq = orderItem.getProductSeq();
			// 商品名稱
			String productName = orderItem.getProductName();
			// 數量
			Integer quantity = orderItem.getQuantity();
			// 單價
			BigDecimal price = orderItem.getPrice();
			// 辣度(none/small/medium/large)
			String spicyLevel = orderItem.getSpicyLevel();
			// 是否要香菜
			Boolean wantsCoriander = orderItem.getWantsCoriander() != null ? orderItem.getWantsCoriander() : false;
			// 是否要醋
			Boolean wantsVinegar = orderItem.getWantsVinegar() != null ? orderItem.getWantsVinegar() : false;
			;

			return OrderItem.builder().orderSeq(orderSeq).productSeq(productSeq).productName(productName)
					.quantity(quantity).price(price).spicyLevel(spicyLevel).wantsCoriander(wantsCoriander)
					.wantsVinegar(wantsVinegar).createTime(createTime).build();
		}).toList());

		;

		System.out.println("收到新訂單：" + orderVo);

		return orderVo;
	}

	@MessageMapping("/order/status")
	@SendTo("/topic/order-status")
	public OrderStatusUpdateDto updateOrderStatus(OrderStatusUpdateDto statusDto) {
		// 查找訂單
		Order order = orderRepository.findByOrderId(statusDto.getOrderId())
				.orElseThrow(() -> new OrderNotFoundException(statusDto.getOrderId()));

		try {
			// 驗證狀態轉換
			OrderStatus newStatus = OrderStatus.valueOf(statusDto.getStatus().toUpperCase());
			OrderStatus currentStatus = order.getStatus();

			if (!isValidStatusTransition(currentStatus, newStatus)) {
				throw new InvalidStatusTransitionException(currentStatus, newStatus);
			}
			// 更新訂單狀態
			order.setStatus(newStatus);
			order.setUpdateTime(LocalDateTime.now());

			if (statusDto.getReason() != null) {
				order.setRejectReason(statusDto.getReason());
			}
			// 保存更新
			orderRepository.save(order);
			// 更新回傳的 DTO
			statusDto.setUpdateTime(order.getUpdateTime());

			return statusDto;

		} catch (Exception e) {
			System.out.println("訂單狀態更新失敗: " + e.getMessage());
			throw e;
		}
	}

	private boolean isValidStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
		switch (currentStatus) {
		case PENDING:
			return newStatus == OrderStatus.ACCEPTED || newStatus == OrderStatus.REJECTED;
		case ACCEPTED:
			return newStatus == OrderStatus.COMPLETED || newStatus == OrderStatus.CANCELLED;
		case REJECTED:
		case COMPLETED:
		case CANCELLED:
			return false;
		default:
			return false;
		}
	}

	public class OrderNotFoundException extends RuntimeException {
		public OrderNotFoundException(String orderId) {
			super("找不到訂單：" + orderId);
		}
	}

	public class InvalidStatusTransitionException extends RuntimeException {
		public InvalidStatusTransitionException(OrderStatus current, OrderStatus newStatus) {
			super("無效的狀態轉換：從 " + current + " 到 " + newStatus);
		}
	}

}
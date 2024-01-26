package com.app.payloads.response;

import com.app.entites.Order;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AddItemResponse {
	boolean success;

	@Builder
	static class Data {
		private String orderId;
		private Order currentOrder;
	}
}

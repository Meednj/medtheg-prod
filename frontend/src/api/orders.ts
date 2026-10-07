import { apiGet, apiRequest } from "./client";
import type {
  CreateOrderRequest,
  CreatePaymentResponseDto,
  OrderHistoryResponseDto,
  OrderResponseDto,
} from "./dto";

export function createOrder(request: CreateOrderRequest) {
  return apiRequest<OrderResponseDto>("/orders", {
    method: "POST",
    body: JSON.stringify(request),
  });
}

export function createPayment(orderId: string) {
  return apiRequest<CreatePaymentResponseDto>(`/orders/${orderId}/payment`, {
    method: "POST",
  });
}

export function getOrders(page = 0, size = 12) {
  return apiGet<OrderHistoryResponseDto>(`/orders?page=${page}&size=${size}`);
}

export function getOrder(orderId: string) {
  return apiGet<OrderResponseDto>(`/orders/${orderId}`);
}

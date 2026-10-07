import { useMutation } from "@tanstack/react-query";
import { createOrder, createPayment } from "../../api/orders";
import type {
  CreateOrderRequest,
  CreatePaymentResponseDto,
} from "../../api/dto";

export function useCheckoutMutation() {
  return useMutation<CreatePaymentResponseDto, Error, CreateOrderRequest>({
    mutationFn: async (request) => {
      const order = await createOrder(request);
      return createPayment(order.id);
    },
  });
}

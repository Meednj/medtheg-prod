import { useQuery } from "@tanstack/react-query";
import { getOrder } from "../../api/orders";
import { queryKeys } from "../../api/queryKeys";

export function useOrderQuery(orderId: string | null, enabled = true) {
  return useQuery({
    queryKey: queryKeys.order(orderId ?? ""),
    queryFn: () => getOrder(orderId ?? ""),
    enabled: Boolean(orderId) && enabled,
  });
}

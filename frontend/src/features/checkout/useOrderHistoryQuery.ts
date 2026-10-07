import { useQuery } from "@tanstack/react-query";
import { getOrders } from "../../api/orders";
import { queryKeys } from "../../api/queryKeys";

export function useOrderHistoryQuery(page = 0, size = 12, enabled = true) {
  return useQuery({
    queryKey: queryKeys.orderHistory(page, size),
    queryFn: () => getOrders(page, size),
    enabled,
  });
}

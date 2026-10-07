import { useQuery } from "@tanstack/react-query";
import { getAdminProduct } from "../../api/admin";
import { queryKeys } from "../../api/queryKeys";

export function useAdminProductQuery(productId: string | undefined) {
  return useQuery({
    queryKey: queryKeys.adminProduct(productId ?? ""),
    queryFn: () => getAdminProduct(productId ?? ""),
    enabled: Boolean(productId),
  });
}

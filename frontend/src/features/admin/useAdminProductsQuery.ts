import { useQuery } from "@tanstack/react-query";
import { getAdminProducts } from "../../api/admin";
import type { AdminProductQuery } from "../../api/dto";
import { queryKeys } from "../../api/queryKeys";

export function useAdminProductsQuery(query: AdminProductQuery, enabled = true) {
  return useQuery({
    queryKey: queryKeys.adminProducts(query),
    queryFn: () => getAdminProducts(query),
    enabled,
    placeholderData: (previous) => previous,
  });
}

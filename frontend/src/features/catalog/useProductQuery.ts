import { useQuery } from "@tanstack/react-query";
import { apiGet } from "../../api/client";
import type { ProductDto } from "../../api/dto";
import { queryKeys } from "../../api/queryKeys";

export function useProductQuery(id: string | undefined) {
  return useQuery({
    queryKey: queryKeys.product(id ?? ""),
    queryFn: () => apiGet<ProductDto>(`/products/${id}`),
    enabled: Boolean(id),
  });
}

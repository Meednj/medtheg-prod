import { useQuery } from "@tanstack/react-query";
import { apiGet } from "../../api/client";
import type { CatalogQuery, ProductPageDto } from "../../api/dto";
import { queryKeys } from "../../api/queryKeys";

function toQueryString(query: CatalogQuery) {
  const params = new URLSearchParams();
  for (const [key, value] of Object.entries(query)) {
    if (value !== undefined && value !== "") params.set(key, String(value));
  }
  return params.toString();
}

export function useProductsQuery(query: CatalogQuery) {
  return useQuery({
    queryKey: queryKeys.products(query),
    queryFn: () => apiGet<ProductPageDto>(`/products?${toQueryString(query)}`),
  });
}

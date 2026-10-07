import { useQuery } from "@tanstack/react-query";
import { apiGet } from "../../api/client";
import type { CoverUrlDto } from "../../api/dto";
import { queryKeys } from "../../api/queryKeys";

export function useProductCoverQuery(
  productId: string,
  assetId: string | null,
) {
  return useQuery({
    queryKey: queryKeys.cover(productId, assetId ?? ""),
    queryFn: () =>
      apiGet<CoverUrlDto>(`/products/${productId}/assets/${assetId}/cover`),
    enabled: Boolean(assetId),
    staleTime: 10 * 60 * 1000,
  });
}

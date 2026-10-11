import { useQuery } from "@tanstack/react-query";
import { getAdminAssetPreview } from "../../api/admin";
import { queryKeys } from "../../api/queryKeys";

export function AdminProductArtwork({
  productId,
  assetId,
  title,
}: {
  productId: string;
  assetId: string | null;
  title: string;
}) {
  const query = useQuery({
    queryKey: queryKeys.adminAssetPreview(productId, assetId ?? ""),
    queryFn: () => getAdminAssetPreview(productId, assetId ?? "", "IMAGE"),
    enabled: Boolean(assetId),
    staleTime: 10 * 60 * 1000,
  });
  return (
    <span className="admin-product-artwork">
      {query.data?.url ? (
        <img src={query.data.url} alt={`${title} cover`} loading="lazy" />
      ) : (
        <span aria-hidden="true">{query.isError ? "IMAGE" : "NO COVER"}</span>
      )}
    </span>
  );
}

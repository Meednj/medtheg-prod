import { EditorialArtwork } from "./EditorialArtwork";
import { useProductCoverQuery } from "../../features/catalog/useProductCoverQuery";

type ProductArtworkProps = {
  productId: string;
  coverAssetId: string | null;
  index: string;
  label: string;
  metadata: string;
  reverse?: boolean;
};

export function ProductArtwork({
  productId,
  coverAssetId,
  index,
  label,
  metadata,
  reverse,
}: ProductArtworkProps) {
  const coverQuery = useProductCoverQuery(productId, coverAssetId);

  return (
    <EditorialArtwork
      index={index}
      label={label}
      metadata={metadata}
      reverse={reverse}
      artwork={{ alt: label, src: coverQuery.data?.url }}
    />
  );
}

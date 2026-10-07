import type { AssetType } from "../../api/dto";

export function AssetTypeLabel({ type }: { type: AssetType }) {
  return <span className="tech-label">{type.replaceAll("_", " ")}</span>;
}

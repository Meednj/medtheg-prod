import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import type { AssetType, ProductAssetDto } from "../../api/dto";
import { getAdminAssetPreview } from "../../api/admin";
import { getErrorMessage } from "../../api/errors";
import { queryKeys } from "../../api/queryKeys";
import { useAdminProductMutations } from "../../features/admin/useAdminProductMutations";
import { AssetTypeLabel } from "./AssetTypeLabel";

const assetTypes: AssetType[] = ["IMAGE", "AUDIO_PREVIEW", "AUDIO_MP3", "AUDIO_WAV", "STEMS", "MIDI", "PDF", "VIDEO", "OTHER"];
const acceptedExtensions: Record<AssetType, string[]> = {
  IMAGE: [".jpg", ".jpeg", ".png", ".webp", ".avif", ".gif"],
  AUDIO_PREVIEW: [".mp3", ".wav", ".ogg", ".m4a", ".aac"],
  AUDIO_MP3: [".mp3"],
  AUDIO_WAV: [".wav"],
  STEMS: [".zip", ".7z", ".rar"],
  MIDI: [".mid", ".midi"],
  PDF: [".pdf"],
  VIDEO: [".mp4", ".mov", ".webm"],
  OTHER: [],
};
const maxFileSize = 250 * 1024 * 1024;

function fileMatchesType(file: File, type: AssetType) {
  const name = file.name.toLowerCase();
  const extensionMatch = acceptedExtensions[type].some((extension) => name.endsWith(extension));
  if (type === "OTHER") return true;
  const mimePrefix = type === "IMAGE" ? "image/" : type.startsWith("AUDIO") ? "audio/" : type === "VIDEO" ? "video/" : "";
  if (mimePrefix && file.type.startsWith(mimePrefix)) return true;
  return extensionMatch;
}

function formatBytes(size: number) {
  return new Intl.NumberFormat(undefined, { style: "unit", unit: "megabyte", maximumFractionDigits: 1 }).format(size / (1024 * 1024));
}

function PreviewPlayer({ productId, assetId }: { productId: string; assetId: string }) {
  const [duration, setDuration] = useState<number | null>(null);
  const query = useQuery({
    queryKey: queryKeys.adminAssetPreview(productId, assetId),
    queryFn: () => getAdminAssetPreview(productId, assetId, "AUDIO_PREVIEW"),
    staleTime: 10 * 60 * 1000,
  });
  return (
    <div className="admin-preview-player">
      {query.data?.url ? (
        <audio controls preload="none" src={query.data.url} onLoadedMetadata={(event) => setDuration(Number.isFinite(event.currentTarget.duration) ? event.currentTarget.duration : null)} aria-label="Audio preview playback" />
      ) : query.isError ? (
        <p className="field-error">{getErrorMessage(query.error, "Preview unavailable.")}</p>
      ) : (
        <p className="admin-muted">Preparing preview…</p>
      )}
      {duration !== null && <span className="tech-label">{Math.floor(duration / 60)}:{String(Math.floor(duration % 60)).padStart(2, "0")}</span>}
    </div>
  );
}

function AssetRow({
  productId,
  asset,
  onRemove,
  pending,
}: {
  productId: string;
  asset: ProductAssetDto;
  onRemove: (id: string) => void;
  pending: boolean;
}) {
  return (
    <li className="admin-asset-row">
      <div className="admin-asset-row-main">
        <AssetTypeLabel type={asset.type} />
        <span className="admin-asset-id">{asset.id}</span>
        {asset.type === "AUDIO_PREVIEW" && <PreviewPlayer productId={productId} assetId={asset.id} />}
      </div>
      <button className="button button--ghost" type="button" disabled={pending} onClick={() => onRemove(asset.id)} aria-label={`Remove ${asset.type} asset ${asset.id}`}>
        Remove
      </button>
    </li>
  );
}

export function AssetUploader({ productId, assets }: { productId: string; assets: ProductAssetDto[] }) {
  const mutations = useAdminProductMutations(productId);
  const [type, setType] = useState<AssetType>("IMAGE");
  const [message, setMessage] = useState<string | null>(null);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const inputId = `asset-file-${productId}`;

  async function selectFile(file: File | undefined) {
    if (!file) return;
    setMessage(null);
    if (file.size <= 0 || file.size > maxFileSize) {
      setMessage("Choose a non-empty file no larger than 250 MB.");
      return;
    }
    if (!fileMatchesType(file, type)) {
      setMessage(`The selected file does not match ${type}. Choose a supported ${type.toLowerCase().replaceAll("_", " ")} file.`);
      return;
    }
    setSelectedFile(file);
    try {
      const result = await mutations.upload.mutateAsync({ file, type });
      setMessage(`Uploaded ${file.name} (${formatBytes(file.size)}) as ${result.type}.`);
      setSelectedFile(null);
    } catch (error) {
      setMessage(getErrorMessage(error, "Asset upload failed. The selected file is still available to retry."));
    }
  }

  function removeAsset(assetId: string) {
    if (window.confirm("Remove this asset reference from the product? The file itself may remain in object storage.")) {
      mutations.removeAsset.mutate(assetId, {
        onSuccess: () => setMessage("Asset reference removed."),
        onError: (error) => setMessage(getErrorMessage(error, "Asset could not be removed.")),
      });
    }
  }

  return (
    <section className="admin-assets" aria-labelledby="admin-assets-title">
      <div className="admin-assets-heading">
        <div>
          <p className="eyebrow">Media library</p>
          <h2 id="admin-assets-title">{assets.length} registered assets</h2>
        </div>
      </div>
      <div className="asset-uploader">
        <label className="field" htmlFor={`${inputId}-type`}>
          <span className="field-label">Asset type</span>
          <select id={`${inputId}-type`} className="select" value={type} disabled={mutations.upload.isPending} onChange={(event) => setType(event.target.value as AssetType)}>
            {assetTypes.map((value) => <option key={value} value={value}>{value.replaceAll("_", " ")}</option>)}
          </select>
        </label>
        <label className="button button--secondary asset-file-label" htmlFor={inputId}>
          {mutations.upload.isPending ? "Uploading…" : "Choose and upload file"}
        </label>
        <input
          id={inputId}
          className="visually-hidden-input"
          type="file"
          disabled={mutations.upload.isPending}
          accept={acceptedExtensions[type].join(",") || undefined}
          onChange={(event) => {
            const input = event.currentTarget;
            const file = input.files?.[0];
            input.value = "";
            void selectFile(file);
          }}
        />
        {mutations.upload.isPending && <p className="admin-muted" role="status">Uploading {selectedFile?.name ?? "asset"}…</p>}
        {message && <p className="admin-status" role="status" aria-live="polite">{message}</p>}
      </div>
      {assets.length === 0 ? <p className="admin-muted">No asset references yet.</p> : (
        <ul className="admin-asset-list">
          {assets.map((asset) => <AssetRow key={asset.id} productId={productId} asset={asset} pending={mutations.removeAsset.isPending} onRemove={removeAsset} />)}
        </ul>
      )}
      <p className="admin-muted">File names and sizes are shown at upload time only; the API stores and returns asset ID and type. Duration is read from the preview audio metadata when available.</p>
    </section>
  );
}

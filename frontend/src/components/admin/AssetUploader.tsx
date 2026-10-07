import { useState } from "react";
import type { AssetType } from "../../api/dto";
import { getErrorMessage } from "../../api/errors";
import { useAdminProductMutations } from "../../features/admin/useAdminProductMutations";

export function AssetUploader({ productId }: { productId: string }) {
  const upload = useAdminProductMutations(productId).upload;
  const [message, setMessage] = useState<string | null>(null);
  const [type, setType] = useState<AssetType>("IMAGE");
  async function selectFile(file: File | undefined) {
    if (!file) return;
    setMessage(null);
    try {
      const result = await upload.mutateAsync({ file, type });
      setMessage(`Asset uploaded and registered as ${result.type}.`);
    } catch (error) {
      setMessage(getErrorMessage(error, "Asset upload failed."));
    }
  }
  return (
    <div className="asset-uploader">
      <label className="field">
        <span className="field-label">Asset type</span>
        <select
          className="select"
          value={type}
          disabled={upload.isPending}
          onChange={(event) => setType(event.target.value as AssetType)}
        >
          <option value="IMAGE">Image</option>
          <option value="AUDIO_PREVIEW">Audio preview</option>
          <option value="AUDIO_MP3">MP3 download</option>
          <option value="AUDIO_WAV">WAV download</option>
          <option value="STEMS">Stems archive</option>
          <option value="MIDI">MIDI</option>
          <option value="PDF">PDF</option>
          <option value="VIDEO">Video</option>
          <option value="OTHER">Other</option>
        </select>
      </label>
      <label className="button button--secondary" htmlFor="asset-file">
        {upload.isPending ? "Uploading..." : "Upload asset"}
      </label>
      <input
        id="asset-file"
        className="visually-hidden-input"
        type="file"
        disabled={upload.isPending}
        onChange={(event) => {
          const input = event.currentTarget;
          const file = input.files?.[0];
          input.value = "";
          void selectFile(file);
        }}
      />
      {message && (
        <p className="admin-status" aria-live="polite">
          {message}
        </p>
      )}
    </div>
  );
}

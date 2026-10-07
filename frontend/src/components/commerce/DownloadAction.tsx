import { useState } from "react";
import { getLibraryAssetDownloadUrl } from "../../api/library";
import { getErrorMessage } from "../../api/errors";

export function DownloadAction({
  assetId,
  label,
}: {
  assetId: string;
  label: string;
}) {
  const [isPending, setIsPending] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function download() {
    if (isPending) return;
    setIsPending(true);
    setError(null);
    try {
      const { url } = await getLibraryAssetDownloadUrl(assetId);
      window.location.assign(url);
    } catch (downloadError) {
      setError(
        getErrorMessage(downloadError, "The download could not be started."),
      );
    } finally {
      setIsPending(false);
    }
  }

  return (
    <span className="download-action">
      <button
        className="button button--ghost"
        type="button"
        disabled={isPending}
        aria-busy={isPending}
        onClick={() => void download()}
      >
        {isPending ? "Preparing..." : `Download ${label}`}
      </button>
      {error && (
        <span className="download-error" role="alert">
          {error}
        </span>
      )}
    </span>
  );
}

import { Link } from "react-router-dom";
import { getErrorMessage } from "../api/errors";
import { Breadcrumbs } from "../components/foundation/Breadcrumbs";
import { DownloadAction } from "../components/commerce/DownloadAction";
import { useLibraryQuery } from "../features/library/useLibraryQuery";

function formatDate(value: string) {
  return new Intl.DateTimeFormat(undefined, { dateStyle: "medium" }).format(
    new Date(value),
  );
}

export function LibraryPage() {
  const libraryQuery = useLibraryQuery();
  return (
    <section
      className="commerce-page library-page"
      aria-labelledby="library-title"
    >
      <Breadcrumbs
        items={[{ label: "MedTheG", to: "/" }, { label: "Private library" }]}
      />
      <div className="commerce-heading">
        <p className="eyebrow">Customer / 06</p>
        <h1 id="library-title">Private library</h1>
        <p className="library-intro">
          Your acquired production goods, held as a quiet release archive.
        </p>
      </div>
      {libraryQuery.isPending && (
        <div className="library-list" aria-live="polite">
          <span className="skeleton library-skeleton" />
          <span className="skeleton library-skeleton" />
        </div>
      )}
      {libraryQuery.isError && (
        <div className="state-panel" role="alert">
          <h2>Library unavailable</h2>
          <p>{getErrorMessage(libraryQuery.error)}</p>
          <button
            className="button button--secondary"
            type="button"
            onClick={() => void libraryQuery.refetch()}
          >
            Try again
          </button>
        </div>
      )}
      {libraryQuery.isSuccess && libraryQuery.data.length === 0 && (
        <div className="state-panel">
          <h2>No purchases in your library yet</h2>
          <p>Purchased releases will appear here after payment is confirmed.</p>
          <Link className="text-link" to="/beats">
            Browse releases
          </Link>
        </div>
      )}
      {libraryQuery.isSuccess && libraryQuery.data.length > 0 && (
        <div className="library-list">
          {libraryQuery.data.map((item) => (
            <article className="library-entry" key={item.entitlementId}>
              <div className="library-entry-heading">
                <div>
                  <p className="eyebrow">
                    {item.type} / Granted {formatDate(item.grantedAt)}
                  </p>
                  <h2>
                    <Link
                      className="library-entry-title-link"
                      to={`/library/${item.productId}`}
                    >
                      {item.title}
                    </Link>
                  </h2>
                  <p>{item.description}</p>
                </div>
                <span className="tech-label">Order / {item.orderId}</span>
              </div>
              <div className="library-assets">
                <p className="eyebrow">Authorized assets</p>
                {item.assets.length > 0 ? (
                  item.assets.map((asset) => (
                    <div className="library-asset" key={asset.assetId}>
                      <span className="tech-label">{asset.type}</span>
                      <DownloadAction
                        assetId={asset.assetId}
                        label={asset.type}
                      />
                    </div>
                  ))
                ) : (
                  <p>No downloadable assets are available for this release.</p>
                )}
              </div>
            </article>
          ))}
        </div>
      )}
    </section>
  );
}

import { Link, useParams } from "react-router-dom";
import { getErrorMessage } from "../api/errors";
import { Breadcrumbs } from "../components/foundation/Breadcrumbs";
import { DownloadAction } from "../components/commerce/DownloadAction";
import { useLibraryQuery } from "../features/library/useLibraryQuery";

function formatDate(value: string) {
  return new Intl.DateTimeFormat(undefined, { dateStyle: "medium" }).format(
    new Date(value),
  );
}

export function LibraryProductPage() {
  const { productId } = useParams();
  const query = useLibraryQuery();
  const item = query.data?.find((entry) => entry.productId === productId);

  if (query.isPending)
    return (
      <section className="commerce-page" aria-live="polite">
        <span className="skeleton library-skeleton" />
      </section>
    );

  if (query.isError)
    return (
      <section className="commerce-page">
        <div className="state-panel" role="alert">
          <h1>Library unavailable</h1>
          <p>{getErrorMessage(query.error)}</p>
          <button
            className="button button--secondary"
            type="button"
            onClick={() => void query.refetch()}
          >
            Try again
          </button>
        </div>
      </section>
    );

  if (!item)
    return (
      <section className="commerce-page">
        <Breadcrumbs
          items={[
            { label: "MedTheG", to: "/" },
            { label: "Private library", to: "/library" },
            { label: "Release unavailable" },
          ]}
        />
        <div className="state-panel">
          <h1>Release not in your library</h1>
          <p>This release is not available in your active entitlements.</p>
          <Link className="text-link" to="/library">
            Return to your library
          </Link>
        </div>
      </section>
    );

  return (
    <section className="commerce-page library-page" aria-labelledby="library-release-title">
      <Breadcrumbs
        items={[
          { label: "MedTheG", to: "/" },
          { label: "Private library", to: "/library" },
          { label: item.title },
        ]}
      />
      <div className="commerce-heading">
        <p className="eyebrow">Private library / {item.type}</p>
        <h1 id="library-release-title">{item.title}</h1>
        <p className="library-intro">{item.description}</p>
      </div>
      <article className="library-entry">
        <div className="library-entry-heading">
          <div>
            <p className="eyebrow">Granted {formatDate(item.grantedAt)}</p>
            <p>Order / {item.orderId}</p>
          </div>
          <span className="tech-label">{item.currency} {item.price}</span>
        </div>
        <div className="library-assets">
          <p className="eyebrow">Authorized assets</p>
          {item.assets.length > 0 ? (
            item.assets.map((asset) => (
              <div className="library-asset" key={asset.assetId}>
                <span className="tech-label">{asset.type}</span>
                <DownloadAction assetId={asset.assetId} label={asset.type} />
              </div>
            ))
          ) : (
            <p>No downloadable assets are available for this release.</p>
          )}
        </div>
      </article>
      <Link className="text-link" to="/library">
        Return to your library
      </Link>
    </section>
  );
}

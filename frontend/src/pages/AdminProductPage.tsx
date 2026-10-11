import { Link, useParams, useLocation } from "react-router-dom";
import { Breadcrumbs } from "../components/foundation/Breadcrumbs";
import { AssetUploader } from "../components/admin/AssetUploader";
import { AdminProductArtwork } from "../components/admin/AdminProductArtwork";
import { AdminProductNavigation } from "../components/admin/AdminProductNavigation";
import { getErrorMessage } from "../api/errors";
import { useAdminProductQuery } from "../features/admin/useAdminProductQuery";
import { useAdminProductMutations } from "../features/admin/useAdminProductMutations";

export function AdminProductPage() {
  const { id } = useParams();
  const location = useLocation();
  const query = useAdminProductQuery(id);
  const mutations = useAdminProductMutations(id);
  const product = query.data;
  const actionError =
    mutations.publish.error ??
    mutations.archive.error ??
    mutations.removeAsset.error;
  if (query.isPending)
    return (
      <section className="admin-page">
        <span className="skeleton admin-skeleton" />
      </section>
    );
  if (query.isError || !product)
    return (
      <section className="admin-page">
        <div className="state-panel" role="alert">
          <h1>Product unavailable</h1>
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
  const isPublished = product.status === "PUBLISHED";
  return (
    <section className="admin-page" aria-labelledby="admin-product-title">
      <Breadcrumbs
        items={[
          { label: "MedTheG", to: "/" },
          { label: "Admin", to: `/admin/products${location.search}` },
          { label: product.title },
        ]}
      />
      <div className="admin-product-heading">
        <div>
          <p className="eyebrow">Product workbench / {product.status}</p>
          <h1 id="admin-product-title">{product.title}</h1>
          <p className="tech-label">
            {product.id} / {product.type} / {product.currency} {product.price}
          </p>
        </div>
        <Link
          className="button button--secondary"
          to={`/admin/products/${product.id}/edit${location.search}`}
        >
          Edit product
        </Link>
      </div>
      <AdminProductNavigation product={product} />
      <div className="admin-product-actions">
        <button
          className="button button--primary"
          type="button"
          disabled={isPublished || mutations.publish.isPending}
          onClick={() => mutations.publish.mutate()}
        >
          Publish
        </button>
        <button
          className="button button--secondary"
          type="button"
          disabled={!isPublished || mutations.archive.isPending}
          onClick={() => {
            if (window.confirm("Archive this published product?"))
              mutations.archive.mutate();
          }}
        >
          Archive
        </button>
      </div>
      {actionError && (
        <div className="state-panel" role="alert">
          <p>{getErrorMessage(actionError, "The product change could not be saved.")}</p>
        </div>
      )}
      <div className="admin-product-grid">
        <div className="admin-product-overview">
          <AdminProductArtwork productId={product.id} assetId={product.coverAssetId} title={product.title} />
          <p className="eyebrow">Description</p>
          <p className="admin-description">
            {product.description || "No description provided."}
          </p>
          <p className="eyebrow">Categories</p>
          <p className="tech-label">
            {product.categories.join(" / ") || "No categories assigned"}
          </p>
        </div>
        <AssetUploader productId={product.id} assets={product.assets} />
      </div>
    </section>
  );
}

import { Link, useSearchParams } from "react-router-dom";
import { AdminProductArtwork } from "../components/admin/AdminProductArtwork";
import { ProductIdLookup } from "../components/admin/ProductIdLookup";
import type { AdminProductQuery, ProductStatus, ProductType } from "../api/dto";
import { getErrorMessage } from "../api/errors";
import { useAdminProductsQuery } from "../features/admin/useAdminProductsQuery";

const types: ProductType[] = ["BEAT", "SAMPLE_PACK", "DRUM_KIT", "COURSE", "BUNDLE"];
const statuses: ProductStatus[] = ["DRAFT", "PUBLISHED", "ARCHIVED"];
const sortFields = ["updatedAt", "title", "price"] as const;

function parseQuery(params: URLSearchParams): AdminProductQuery {
  const page = Number(params.get("page") ?? "0");
  const size = Number(params.get("size") ?? "20");
  const sortBy = params.get("sortBy");
  const direction = params.get("direction");
  return {
    page: Number.isInteger(page) && page >= 0 ? page : 0,
    size: Number.isInteger(size) && size >= 1 && size <= 100 ? size : 20,
    status: statuses.find((value) => value === params.get("status")),
    type: types.find((value) => value === params.get("type")),
    search: params.get("search") || undefined,
    sortBy: sortFields.find((value) => value === sortBy) ?? "updatedAt",
    direction: direction === "asc" ? "asc" : "desc",
  };
}

export function AdminProductsPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const query = parseQuery(searchParams);
  const products = useAdminProductsQuery(query);
  const selectedId = searchParams.get("selected");

  function submitSearch(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    updateParams({ search: String(formData.get("search") ?? "").trim(), page: null });
  }

  function updateParams(values: Record<string, string | number | null>) {
    const next = new URLSearchParams(searchParams);
    for (const [key, value] of Object.entries(values)) {
      if (value === null || value === "") next.delete(key);
      else next.set(key, String(value));
    }
    setSearchParams(next);
  }

  const page = query.page ?? 0;
  const pageCount = products.data?.totalPages ?? 0;
  const from = products.data && products.data.totalElements > 0 ? page * query.size! + 1 : 0;
  const to = Math.min((page + 1) * query.size!, products.data?.totalElements ?? 0);

  return (
    <section className="admin-page admin-products-page" aria-labelledby="admin-products-title">
      <div className="admin-page-heading">
        <div>
          <p className="eyebrow">Studio / Admin</p>
          <h1 id="admin-products-title">Product index</h1>
          <p className="admin-lede">Search and manage drafts, published releases, and archived work from one paginated index.</p>
        </div>
        <Link className="button button--primary" to="/admin/products/new">Create product</Link>
      </div>

      <form className="admin-catalog-filters" onSubmit={submitSearch} role="search">
        <label className="field admin-catalog-search">
          <span className="field-label">Find a product</span>
          <input className="input" key={query.search ?? ""} name="search" defaultValue={query.search} maxLength={100} placeholder="Title or description" />
        </label>
        <button className="button button--secondary" type="submit">Search</button>
        <label className="field">
          <span className="field-label">Status</span>
          <select className="select" value={query.status ?? ""} onChange={(event) => updateParams({ status: event.target.value, page: null })}>
            <option value="">All statuses</option>
            {statuses.map((status) => <option key={status}>{status}</option>)}
          </select>
        </label>
        <label className="field">
          <span className="field-label">Product type</span>
          <select className="select" value={query.type ?? ""} onChange={(event) => updateParams({ type: event.target.value, page: null })}>
            <option value="">All types</option>
            {types.map((type) => <option key={type}>{type}</option>)}
          </select>
        </label>
        <label className="field">
          <span className="field-label">Sort</span>
          <select className="select" value={query.sortBy} onChange={(event) => updateParams({ sortBy: event.target.value, page: null })}>
            <option value="updatedAt">Recently updated</option>
            <option value="title">Title</option>
            <option value="price">Price</option>
          </select>
        </label>
        <label className="field">
          <span className="field-label">Direction</span>
          <select className="select" value={query.direction} onChange={(event) => updateParams({ direction: event.target.value, page: null })}>
            <option value="desc">Descending</option>
            <option value="asc">Ascending</option>
          </select>
        </label>
      </form>

      {products.isPending && <div className="admin-product-list-state" role="status"><span className="skeleton admin-skeleton" />Loading product index…</div>}
      {products.isError && <div className="state-panel" role="alert"><h2>Product index unavailable</h2><p>{getErrorMessage(products.error)}</p><button className="button button--secondary" type="button" onClick={() => void products.refetch()}>Retry</button></div>}
      {products.isSuccess && products.data.content.length === 0 && <div className="state-panel"><h2>No products match these filters</h2><p>Try another status, type, or search term.</p><button className="button button--secondary" type="button" onClick={() => setSearchParams(new URLSearchParams())}>Clear filters</button></div>}
      {products.data && products.data.content.length > 0 && (
        <>
          <p className="admin-result-count" aria-live="polite">Showing {from}–{to} of {products.data.totalElements} products</p>
          <ul className="admin-product-list" aria-label="Products">
            {products.data.content.map((product, index) => {
              const params = new URLSearchParams(searchParams);
              params.set("selected", product.id);
              const updatedAt = product.updatedAt ? new Date(product.updatedAt) : null;
              const updatedLabel = updatedAt && !Number.isNaN(updatedAt.valueOf())
                ? updatedAt.toLocaleString(undefined, { dateStyle: "medium", timeStyle: "short" })
                : "Not recorded";
              return (
                <li key={product.id}>
                <Link
                  className={`admin-product-list-row${selectedId === product.id ? " is-selected" : ""}`}
                  to={`/admin/products/${product.id}?${params.toString()}`}
                  aria-current={selectedId === product.id ? "page" : undefined}
                  style={{ "--row-delay": `${Math.min(index, 8) * 24}ms` } as React.CSSProperties}
                >
                  <AdminProductArtwork productId={product.id} assetId={product.coverAssetId} title={product.title} />
                  <span className="admin-product-list-main">
                    <span className="admin-product-list-title">{product.title}</span>
                    <span className="admin-product-list-meta">
                      <span>{product.type} <span aria-hidden="true">·</span> {product.categories.length ? product.categories.join(" / ") : "Uncategorized"}</span>
                      <span>Updated {updatedLabel}</span>
                    </span>
                  </span>
                  <span className="admin-product-list-price">{new Intl.NumberFormat(undefined, { style: "currency", currency: product.currency }).format(product.price)}</span>
                  <span className={`admin-status-badge admin-status-badge--${product.status.toLowerCase()}`}>{product.status}</span>
                  <span className="admin-product-list-open">Open <span aria-hidden="true">→</span></span>
                </Link>
                </li>
              );
            })}
          </ul>
          <nav className="admin-pagination" aria-label="Product pages">
            <button className="button button--secondary" type="button" disabled={page <= 0 || products.isFetching} onClick={() => updateParams({ page: page - 1 })}>← Previous</button>
            <span>Page {page + 1} of {Math.max(pageCount, 1)}</span>
            <button className="button button--secondary" type="button" disabled={page + 1 >= pageCount || products.isFetching} onClick={() => updateParams({ page: page + 1 })}>Next →</button>
          </nav>
        </>
      )}

      <details className="admin-direct-access">
        <summary>Open a product by ID</summary>
        <p className="admin-muted">Use direct access when you already have a product reference.</p>
        <ProductIdLookup />
      </details>
    </section>
  );
}

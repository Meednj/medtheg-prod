import { Link, useSearchParams } from "react-router-dom";
import { Badge } from "../components/foundation/Badge";
import { Breadcrumbs } from "../components/foundation/Breadcrumbs";
import { ProductArtwork } from "../components/editorial/ProductArtwork";
import { LineIcon } from "../components/icons/LineIcon";
import type { ProductCategory, ProductType } from "../api/dto";
import { getErrorMessage } from "../api/errors";
import { useProductsQuery } from "../features/catalog/useProductsQuery";

const categoryDetails: Record<
  ProductCategory,
  { title: string; eyebrow: string; detail: string }
> = {
  BEATS: {
    title: "Beats",
    eyebrow: "Catalogue / 01",
    detail: "Independent instrumentals for the next session.",
  },
  KITS: {
    title: "Kits",
    eyebrow: "Catalogue / 02",
    detail: "Production tools gathered with intent.",
  },
  COURSES: {
    title: "Courses",
    eyebrow: "Catalogue / 03",
    detail: "Producer-led material for deeper practice.",
  },
  BUNDLES: {
    title: "Bundles",
    eyebrow: "Catalogue / 04",
    detail: "Selected production goods, gathered together.",
  },
};

const productTypes: ProductType[] = [
  "BEAT",
  "SAMPLE_PACK",
  "DRUM_KIT",
  "COURSE",
  "BUNDLE",
];
const sortOptions = ["createdAt", "title", "price"] as const;

function formatPrice(price: number, currency: string) {
  return new Intl.NumberFormat(undefined, {
    style: "currency",
    currency,
  }).format(price);
}

export function CatalogPage({ category }: { category: ProductCategory }) {
  const [searchParams, setSearchParams] = useSearchParams();
  const details = categoryDetails[category];
  const requestedPage = Number(searchParams.get("page") ?? "0");
  const page = Number.isInteger(requestedPage) && requestedPage >= 0 ? requestedPage : 0;
  const search = searchParams.get("search") ?? "";
  const requestedType = searchParams.get("type");
  const type = productTypes.find((value) => value === requestedType);
  const requestedSort = searchParams.get("sortBy");
  const sortBy = sortOptions.find((value) => value === requestedSort) ?? "createdAt";
  const direction: "asc" | "desc" =
    searchParams.get("direction") === "asc" ? "asc" : "desc";
  const query = {
    page,
    size: 12,
    category,
    search,
    type,
    sortBy,
    direction,
  };
  const productsQuery = useProductsQuery(query);

  function submitSearch(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const form = new FormData(event.currentTarget);
    const next = new URLSearchParams(searchParams);
    const value = String(form.get("search") ?? "").trim();
    if (value) next.set("search", value);
    else next.delete("search");
    next.delete("page");
    setSearchParams(next);
  }

  function changePage(nextPage: number) {
    const next = new URLSearchParams(searchParams);
    if (nextPage > 0) next.set("page", String(nextPage));
    else next.delete("page");
    setSearchParams(next);
  }

  function updateFilter(key: "type" | "sortBy" | "direction", value: string) {
    const next = new URLSearchParams(searchParams);
    if (value) next.set(key, value);
    else next.delete(key);
    next.delete("page");
    setSearchParams(next);
  }

  return (
    <section className="catalog-page" aria-labelledby="catalog-title">
      <Breadcrumbs
        items={[{ label: "MedTheG", to: "/" }, { label: details.title }]}
      />
      <div className="catalog-heading">
        <div>
          <p className="eyebrow">{details.eyebrow}</p>
          <h1 id="catalog-title">{details.title}</h1>
          <p>{details.detail}</p>
        </div>
        <Badge>
          {productsQuery.data
            ? `${productsQuery.data.totalElements} releases`
            : "Public index"}
        </Badge>
      </div>

      <form className="catalog-filter" onSubmit={submitSearch} role="search">
        <div className="catalog-filter-primary">
          <label className="field">
            <span className="field-label">Search releases</span>
            <input
              className="input"
              name="search"
              defaultValue={search}
              maxLength={100}
              placeholder="Title or description"
            />
          </label>
          <button className="button button--secondary" type="submit">
            Search <LineIcon name="arrow" />
          </button>
        </div>
        <div className="catalog-filter-options">
          <label className="field">
            <span className="field-label">Product type</span>
            <select
              className="select"
              value={type ?? ""}
              onChange={(event) => updateFilter("type", event.target.value)}
            >
              <option value="">All types</option>
              {productTypes.map((value) => <option key={value} value={value}>{value}</option>)}
            </select>
          </label>
          <label className="field">
            <span className="field-label">Sort by</span>
            <select
              className="select"
              value={sortBy}
              onChange={(event) => updateFilter("sortBy", event.target.value)}
            >
              <option value="createdAt">Recently added</option>
              <option value="title">Title</option>
              <option value="price">Price</option>
            </select>
          </label>
          <label className="field">
            <span className="field-label">Direction</span>
            <select
              className="select"
              value={direction}
              onChange={(event) => updateFilter("direction", event.target.value)}
            >
              <option value="desc">Descending</option>
              <option value="asc">Ascending</option>
            </select>
          </label>
        </div>
      </form>

      {productsQuery.isPending && (
        <div className="catalog-state" aria-live="polite">
          <span className="skeleton catalog-skeleton" />
          <span className="skeleton catalog-skeleton" />
          <span className="skeleton catalog-skeleton" />
        </div>
      )}
      {productsQuery.isError && (
        <div className="state-panel" role="alert">
          <h2>Release index unavailable</h2>
          <p>{getErrorMessage(productsQuery.error)}</p>
        </div>
      )}
      {productsQuery.isSuccess && productsQuery.data.content.length === 0 && (
        <div className="state-panel">
          <h2>No releases found</h2>
          <p>
            Try another search or return to the full{" "}
            {details.title.toLowerCase()} index.
          </p>
        </div>
      )}
      {productsQuery.isSuccess && productsQuery.data.content.length > 0 && (
        <>
          <div className="catalog-grid">
            {productsQuery.data.content.map((product, index) => (
              <article className="catalog-item" key={product.id}>
                <Link
                  className="catalog-artwork-link"
                  to={`/products/${product.id}`}
                  aria-label={`Open ${product.title}`}
                >
                  <ProductArtwork
                    productId={product.id}
                    coverAssetId={product.coverAssetId}
                    index={String(index + 1).padStart(2, "0")}
                    label={`${product.title} artwork`}
                    metadata={product.type}
                  />
                </Link>
                <div className="catalog-item-meta">
                  <div>
                    <Link
                      className="catalog-item-title"
                      to={`/products/${product.id}`}
                    >
                      {product.title}
                    </Link>
                    <p className="tech-label">
                      {product.type} / {product.categories.join(", ")}
                    </p>
                  </div>
                  <span className="catalog-price">
                    {formatPrice(product.price, product.currency)}
                  </span>
                </div>
              </article>
            ))}
          </div>
          <nav className="catalog-pagination" aria-label="Catalog pages">
            <button
              className="button button--ghost"
              type="button"
              disabled={page === 0}
              onClick={() => changePage(page - 1)}
            >
              Previous
            </button>
            <span className="tech-label">
              Page {page + 1} / {Math.max(productsQuery.data.totalPages, 1)}
            </span>
            <button
              className="button button--ghost"
              type="button"
              disabled={page + 1 >= productsQuery.data.totalPages}
              onClick={() => changePage(page + 1)}
            >
              Next
            </button>
          </nav>
        </>
      )}
    </section>
  );
}

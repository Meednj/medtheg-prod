import { Link, useLocation, useSearchParams } from "react-router-dom";
import type { AdminProductQuery, ProductDto } from "../../api/dto";
import { useAdminProductsQuery } from "../../features/admin/useAdminProductsQuery";

const types = ["BEAT", "SAMPLE_PACK", "DRUM_KIT", "COURSE", "BUNDLE"] as const;
const statuses = ["DRAFT", "PUBLISHED", "ARCHIVED"] as const;

function readQuery(params: URLSearchParams): AdminProductQuery {
  const requestedType = params.get("type");
  const requestedStatus = params.get("status");
  const requestedSort = params.get("sortBy");
  const page = Number(params.get("page") ?? "0");
  const size = Number(params.get("size") ?? "20");
  return {
    page: Number.isInteger(page) && page >= 0 ? page : 0,
    size: Number.isInteger(size) && size >= 1 && size <= 100 ? size : 20,
    status: statuses.find((value) => value === requestedStatus),
    type: types.find((value) => value === requestedType),
    search: params.get("search") || undefined,
    sortBy: requestedSort === "title" || requestedSort === "price" ? requestedSort : "updatedAt",
    direction: params.get("direction") === "asc" ? "asc" : "desc",
  };
}

export function AdminProductNavigation({ product }: { product: ProductDto }) {
  const location = useLocation();
  const [params] = useSearchParams();
  const query = readQuery(params);
  const current = useAdminProductsQuery(query);
  const previousPage = useAdminProductsQuery(
    { ...query, page: Math.max(0, (query.page ?? 0) - 1) },
    Boolean((query.page ?? 0) > 0 && current.data),
  );
  const nextPage = useAdminProductsQuery(
    { ...query, page: (query.page ?? 0) + 1 },
    Boolean(current.data && (query.page ?? 0) + 1 < current.data.totalPages),
  );
  const items = current.data?.content ?? [];
  const index = items.findIndex((item) => item.id === product.id);
  const previous: ProductDto | undefined = index < 0 ? undefined : index > 0
    ? items[index - 1]
    : (query.page ?? 0) > 0 ? previousPage.data?.content.at(-1) : undefined;
  const next: ProductDto | undefined = index < 0 ? undefined : index < items.length - 1
    ? items[index + 1]
    : nextPage.data?.content[0];
  const isEditRoute = location.pathname.endsWith("/edit");

  function href(item: ProductDto | undefined, page: number) {
    if (!item) return undefined;
    const path = `/admin/products/${item.id}${isEditRoute ? "/edit" : ""}`;
    const state = new URLSearchParams(params);
    if (page > 0) state.set("page", String(page));
    else state.delete("page");
    state.set("selected", item.id);
    return `${path}?${state.toString()}`;
  }

  const previousHref = href(previous, index > 0 ? query.page ?? 0 : Math.max(0, (query.page ?? 0) - 1));
  const nextHref = href(next, index >= 0 && index < items.length - 1 ? query.page ?? 0 : (query.page ?? 0) + 1);
  return (
    <nav className="admin-product-nav" aria-label="Navigate products">
      {previousHref ? <Link className="button button--secondary" to={previousHref} rel="prev">← Previous product</Link> : <button className="button button--secondary" type="button" disabled>← Previous product</button>}
      <span className="admin-product-nav-position" aria-live="polite">
        {current.isPending ? "Loading product index" : index >= 0 && current.data ? `${(query.page ?? 0) * (query.size ?? 20) + index + 1} of ${current.data.totalElements}` : "Outside current result set"}
      </span>
      {nextHref ? <Link className="button button--secondary" to={nextHref} rel="next">Next product →</Link> : <button className="button button--secondary" type="button" disabled>Next product →</button>}
    </nav>
  );
}

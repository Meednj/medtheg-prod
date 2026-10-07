import { useState } from "react";
import { Link } from "react-router-dom";
import { Breadcrumbs } from "../components/foundation/Breadcrumbs";
import { getErrorMessage } from "../api/errors";
import { useOrderHistoryQuery } from "../features/checkout/useOrderHistoryQuery";

function formatMoney(value: number, currency: string) {
  return new Intl.NumberFormat(undefined, {
    style: "currency",
    currency,
  }).format(value);
}

function formatDate(value: string) {
  return new Intl.DateTimeFormat(undefined, { dateStyle: "medium" }).format(
    new Date(value),
  );
}

export function OrdersPage() {
  const [page, setPage] = useState(0);
  const ordersQuery = useOrderHistoryQuery(page, 12);
  const orders = ordersQuery.data?.content ?? [];

  return (
    <section
      className="commerce-page orders-page"
      aria-labelledby="orders-title"
    >
      <Breadcrumbs
        items={[{ label: "MedTheG", to: "/" }, { label: "Orders" }]}
      />
      <div className="commerce-heading">
        <p className="eyebrow">Customer / 05</p>
        <h1 id="orders-title">Order archive</h1>
      </div>
      {ordersQuery.isPending && (
        <div className="order-list" aria-live="polite">
          <span className="skeleton order-skeleton" />
          <span className="skeleton order-skeleton" />
          <span className="skeleton order-skeleton" />
        </div>
      )}
      {ordersQuery.isError && (
        <div className="state-panel" role="alert">
          <h2>Orders unavailable</h2>
          <p>{getErrorMessage(ordersQuery.error)}</p>
          <button
            className="button button--secondary"
            type="button"
            onClick={() => void ordersQuery.refetch()}
          >
            Try again
          </button>
        </div>
      )}
      {ordersQuery.isSuccess && orders.length === 0 && (
        <div className="state-panel">
          <h2>No orders yet</h2>
          <p>Your acquired releases will appear here after checkout.</p>
          <Link className="text-link" to="/beats">
            Browse releases
          </Link>
        </div>
      )}
      {orders.length > 0 && (
        <>
          <div className="order-list">
            {orders.map((order) => (
              <article className="order-row" key={order.id}>
                <Link
                  className="order-row-main"
                  to={`/orders/${order.id}`}
                >
                  <span className="tech-label">
                    {formatDate(order.createdAt)}
                  </span>
                  <strong>{order.id}</strong>
                </Link>
                <span className="order-status">
                  <span className="field-label">Status</span>
                  {order.status}
                </span>
                <span className="order-items">
                  <span className="field-label">Items</span>
                  {order.items
                    .map((item) => `${item.productTitle} × ${item.quantity}`)
                    .join(", ")}
                </span>
                <span className="order-total">
                  <span className="field-label">Total</span>
                  {formatMoney(order.total, order.currency)}
                  {order.paidAt && (
                    <small>Paid {formatDate(order.paidAt)}</small>
                  )}
                </span>
              </article>
            ))}
          </div>
          <nav className="order-pagination" aria-label="Order pages">
            <button
              className="button button--ghost"
              type="button"
              disabled={page === 0}
              onClick={() => setPage((current) => current - 1)}
            >
              Previous
            </button>
            <span className="tech-label">
              Page {page + 1} / {Math.max(ordersQuery.data?.totalPages ?? 0, 1)}
            </span>
            <button
              className="button button--ghost"
              type="button"
              disabled={page + 1 >= (ordersQuery.data?.totalPages ?? 0)}
              onClick={() => setPage((current) => current + 1)}
            >
              Next
            </button>
          </nav>
        </>
      )}
    </section>
  );
}

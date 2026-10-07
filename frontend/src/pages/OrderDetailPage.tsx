import { Link, useParams } from "react-router-dom";
import { getErrorMessage } from "../api/errors";
import { Breadcrumbs } from "../components/foundation/Breadcrumbs";
import { useOrderQuery } from "../features/checkout/useOrderQuery";

const uuidPattern =
  /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;

function formatMoney(value: number, currency: string) {
  return new Intl.NumberFormat(undefined, {
    style: "currency",
    currency,
  }).format(value);
}

function formatDate(value: string) {
  return new Intl.DateTimeFormat(undefined, {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(new Date(value));
}

export function OrderDetailPage() {
  const { orderId } = useParams();
  const validOrderId = Boolean(orderId && uuidPattern.test(orderId));
  const query = useOrderQuery(orderId ?? null, validOrderId);

  if (!validOrderId)
    return (
      <section className="commerce-page">
        <div className="state-panel" role="alert">
          <h1>Order reference unavailable</h1>
          <p>Enter a valid order reference from your order archive.</p>
          <Link className="text-link" to="/orders">
            Return to orders
          </Link>
        </div>
      </section>
    );

  if (query.isPending)
    return (
      <section className="commerce-page" aria-live="polite">
        <span className="skeleton order-skeleton" />
      </section>
    );

  if (query.isError || !query.data)
    return (
      <section className="commerce-page">
        <div className="state-panel" role="alert">
          <h1>Order unavailable</h1>
          <p>{getErrorMessage(query.error, "This order could not be found.")}</p>
          <button
            className="button button--secondary"
            type="button"
            onClick={() => void query.refetch()}
          >
            Try again
          </button>
          <Link className="text-link" to="/orders">
            Return to orders
          </Link>
        </div>
      </section>
    );

  const order = query.data;

  return (
    <section className="commerce-page" aria-labelledby="order-detail-title">
      <Breadcrumbs
        items={[
          { label: "MedTheG", to: "/" },
          { label: "Orders", to: "/orders" },
          { label: "Order details" },
        ]}
      />
      <div className="commerce-heading">
        <p className="eyebrow">Customer / Order {order.status}</p>
        <h1 id="order-detail-title">Order details</h1>
      </div>
      <div className="order-list">
        {order.items.map((item) => (
          <article className="order-row" key={item.id}>
            <span className="order-row-main">
              <strong>{item.productTitle}</strong>
              <span className="tech-label">Product / {item.productId}</span>
            </span>
            <span className="order-status">
              <span className="field-label">Quantity</span>
              {item.quantity}
            </span>
            <span className="order-items">
              <span className="field-label">Unit price</span>
              {formatMoney(item.unitPrice, item.currency)}
            </span>
            <span className="order-total">
              <span className="field-label">Subtotal</span>
              {formatMoney(item.subtotal, item.currency)}
            </span>
          </article>
        ))}
      </div>
      <aside className="commerce-summary">
        <div className="summary-total">
          <span>Order total</span>
          <strong>{formatMoney(order.total, order.currency)}</strong>
        </div>
        <p>Status / {order.status}</p>
        <p>Placed / {formatDate(order.createdAt)}</p>
        {order.paidAt && <p>Paid / {formatDate(order.paidAt)}</p>}
        <p className="tech-label">Order reference / {order.id}</p>
        <Link className="button button--secondary" to="/orders">
          Return to orders
        </Link>
      </aside>
    </section>
  );
}

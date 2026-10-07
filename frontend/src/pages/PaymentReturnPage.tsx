import { useEffect, useState } from "react";
import { useQueryClient } from "@tanstack/react-query";
import { Link, useSearchParams } from "react-router-dom";
import { getErrorMessage, isApiError } from "../api/errors";
import { queryKeys } from "../api/queryKeys";
import { Breadcrumbs } from "../components/foundation/Breadcrumbs";
import { useOrderQuery } from "../features/checkout/useOrderQuery";

const uuidPattern =
  /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;

export function PaymentReturnPage({
  cancelled = false,
}: {
  cancelled?: boolean;
}) {
  const [params] = useSearchParams();
  const queryClient = useQueryClient();
  const orderId = params.get("orderId");
  const validOrderId = Boolean(orderId && uuidPattern.test(orderId));
  const orderQuery = useOrderQuery(orderId, validOrderId);
  const refetchOrder = orderQuery.refetch;
  const [refreshes, setRefreshes] = useState(0);
  const order = orderQuery.data;

  useEffect(() => {
    if (order?.status !== "PAID") return;
    void queryClient.invalidateQueries({ queryKey: queryKeys.ordersRoot });
    void queryClient.invalidateQueries({ queryKey: queryKeys.library });
  }, [order?.status, queryClient]);

  useEffect(() => {
    if (
      cancelled ||
      !validOrderId ||
      order?.status !== "PENDING" ||
      refreshes >= 3
    )
      return;
    const timer = window.setTimeout(() => {
      setRefreshes((count) => count + 1);
      void refetchOrder();
    }, 3000);
    return () => window.clearTimeout(timer);
  }, [cancelled, validOrderId, order?.status, refreshes, refetchOrder]);

  let title = cancelled ? "Checkout returned" : "Payment confirmation";
  let detail = cancelled
    ? "The payment flow was canceled or returned. Your order has not been reinterpreted by this page."
    : "We are checking the order state with the backend.";
  if (!cancelled && !validOrderId) {
    title = "Order reference unavailable";
    detail = "The payment return did not include a valid order reference.";
  } else if (
    !cancelled &&
    isApiError(orderQuery.error) &&
    orderQuery.error.status === 404
  ) {
    title = "Order not found";
    detail =
      "The order could not be retrieved from the backend. This page cannot infer a payment outcome.";
  } else if (!cancelled && orderQuery.isError) {
    detail = getErrorMessage(
      orderQuery.error,
      "The order state could not be checked.",
    );
  } else if (!cancelled && order?.status === "PAID") {
    title = "Payment confirmed";
    detail = "The backend confirms that this order is paid.";
  } else if (!cancelled && order?.status && order.status !== "PENDING") {
    title = `Order ${order.status.toLowerCase()}`;
    detail = `The backend reports this order as ${order.status.toLowerCase()}.`;
  } else if (!cancelled && refreshes >= 3) {
    detail =
      "Payment confirmation is still processing. Check your account later for the authoritative order state.";
  } else if (cancelled && order?.status) {
    detail = `The checkout flow was canceled or returned. The backend currently reports this order as ${order.status.toLowerCase()}.`;
  }

  return (
    <section className="commerce-page" aria-labelledby="payment-title">
      <Breadcrumbs
        items={[
          { label: "MedTheG", to: "/" },
          { label: cancelled ? "Checkout canceled" : "Payment return" },
        ]}
      />
      <div className="state-panel payment-return">
        <p className="eyebrow">Checkout / Return</p>
        <h1 id="payment-title">{title}</h1>
        <p>{detail}</p>
        {orderId && <p className="tech-label">Order reference / {orderId}</p>}
        {orderQuery.isFetching && (
          <p aria-live="polite">Checking order state...</p>
        )}
        <div className="payment-actions">
          <Link className="button button--primary" to="/cart">
            Return to cart
          </Link>
          <Link className="button button--ghost" to="/orders">
            Account / orders
          </Link>
        </div>
      </div>
    </section>
  );
}

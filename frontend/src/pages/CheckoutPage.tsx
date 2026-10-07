import { Link, useNavigate } from "react-router-dom";
import { getErrorMessage } from "../api/errors";
import { Breadcrumbs } from "../components/foundation/Breadcrumbs";
import { useCheckoutMutation } from "../features/checkout/useCheckoutMutation";
import { useCartStore } from "../state/cartStore";

export function CheckoutPage() {
  const navigate = useNavigate();
  const items = useCartStore((state) => state.items);
  const clear = useCartStore((state) => state.clear);
  const checkout = useCheckoutMutation();

  async function submit() {
    if (checkout.isPending || items.length === 0) return;
    try {
      const payment = await checkout.mutateAsync({
        items: items.map(({ productId, quantity }) => ({
          productId,
          quantity,
        })),
      });
      clear();
      window.location.assign(payment.checkoutUrl);
    } catch {
      // The mutation error is rendered below; the cart remains intact.
    }
  }

  if (items.length === 0)
    return (
      <section className="commerce-page">
        <Breadcrumbs
          items={[{ label: "MedTheG", to: "/" }, { label: "Checkout" }]}
        />
        <div className="state-panel">
          <h1>Nothing to check out</h1>
          <p>Your cart is empty.</p>
          <Link className="text-link" to="/cart">
            Return to cart
          </Link>
        </div>
      </section>
    );

  return (
    <section className="commerce-page" aria-labelledby="checkout-title">
      <Breadcrumbs
        items={[
          { label: "MedTheG", to: "/" },
          { label: "Cart", to: "/cart" },
          { label: "Checkout" },
        ]}
      />
      <div className="checkout-heading">
        <p className="eyebrow">Customer / 04</p>
        <h1 id="checkout-title">Secure checkout</h1>
        <p>
          Review your releases, then continue to Stripe. No card information is
          collected here.
        </p>
      </div>
      <div className="checkout-review">
        <div>
          {items.map((item) => (
            <div className="checkout-line" key={item.productId}>
              <span>{item.title}</span>
              <span className="tech-label">
                {item.quantity} × {item.currency} estimate
              </span>
            </div>
          ))}
        </div>
        <p className="checkout-note">
          The server will validate product availability, current pricing,
          currency, and the final order total.
        </p>
        {checkout.isError && (
          <div className="state-panel" role="alert">
            <h2>Checkout could not start</h2>
            <p>
              {getErrorMessage(
                checkout.error,
                "The order or payment session could not be created. Your cart was preserved.",
              )}
            </p>
          </div>
        )}
        <button
          className="button button--primary"
          type="button"
          disabled={checkout.isPending}
          aria-busy={checkout.isPending}
          onClick={submit}
        >
          {checkout.isPending
            ? "Preparing secure checkout..."
            : "Continue to secure checkout"}
        </button>
        <button
          className="button button--ghost"
          type="button"
          onClick={() => navigate("/cart")}
        >
          Return to cart
        </button>
      </div>
    </section>
  );
}

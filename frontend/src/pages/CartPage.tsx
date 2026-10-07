import { Link } from "react-router-dom";
import { CartLine } from "../components/commerce/CartLine";
import { Breadcrumbs } from "../components/foundation/Breadcrumbs";
import { useCartStore } from "../state/cartStore";

function formatPrice(price: number, currency: string) {
  return new Intl.NumberFormat(undefined, {
    style: "currency",
    currency,
  }).format(price);
}

export function CartPage() {
  const items = useCartStore((state) => state.items);
  const setQuantity = useCartStore((state) => state.setQuantity);
  const removeItem = useCartStore((state) => state.removeItem);
  const total = items.reduce(
    (sum, item) => sum + item.price * item.quantity,
    0,
  );
  const currency = items[0]?.currency ?? "EUR";

  return (
    <section className="commerce-page" aria-labelledby="cart-title">
      <Breadcrumbs items={[{ label: "MedTheG", to: "/" }, { label: "Cart" }]} />
      <div className="commerce-heading">
        <p className="eyebrow">Customer / 03</p>
        <h1 id="cart-title">Your cart</h1>
      </div>
      {items.length === 0 ? (
        <div className="state-panel">
          <h2>Your cart is empty</h2>
          <p>Choose a release from the public index to begin.</p>
          <Link className="text-link" to="/beats">
            Browse releases
          </Link>
        </div>
      ) : (
        <div className="commerce-layout">
          <div className="commerce-list">
            {items.map((item) => (
              <CartLine
                key={item.productId}
                item={item}
                onQuantityChange={(quantity) =>
                  setQuantity(item.productId, quantity)
                }
                onRemove={() => removeItem(item.productId)}
              />
            ))}
          </div>
          <aside className="commerce-summary">
            <p className="eyebrow">Order estimate</p>
            <div className="summary-total">
              <span>Estimated total</span>
              <strong>{formatPrice(total, currency)}</strong>
            </div>
            <p>
              Final product availability, prices, currency, and totals are
              validated by the server when the order is created.
            </p>
            <Link className="button button--primary" to="/checkout">
              Continue to checkout
            </Link>
          </aside>
        </div>
      )}
    </section>
  );
}

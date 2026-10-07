import { Link } from "react-router-dom";
import { ProductArtwork } from "../editorial/ProductArtwork";
import { useProductQuery } from "../../features/catalog/useProductQuery";
import type { CartItem } from "../../state/cartStore";

function formatPrice(price: number, currency: string) {
  return new Intl.NumberFormat(undefined, {
    style: "currency",
    currency,
  }).format(price);
}

export function CartLine({
  item,
  onQuantityChange,
  onRemove,
}: {
  item: CartItem;
  onQuantityChange: (quantity: number) => void;
  onRemove: () => void;
}) {
  const productQuery = useProductQuery(item.productId);
  const product = productQuery.data;
  const subtotal = item.price * item.quantity;

  if (productQuery.isError || (product && product.status !== "PUBLISHED")) {
    return (
      <article className="commerce-line commerce-line--unavailable">
        <div>
          <p className="eyebrow">Unavailable release</p>
          <h2>{item.title}</h2>
          <p>This release can no longer be resolved from the public catalog.</p>
        </div>
        <button
          className="button button--ghost"
          type="button"
          onClick={onRemove}
        >
          Remove
        </button>
      </article>
    );
  }

  return (
    <article className="commerce-line">
      <Link
        className="commerce-line-artwork"
        to={`/products/${item.productId}`}
        aria-label={`Open ${product?.title ?? item.title}`}
      >
        <ProductArtwork
          productId={item.productId}
          coverAssetId={product?.coverAssetId ?? null}
          index="01"
          label={`${product?.title ?? item.title} artwork`}
          metadata={product?.type ?? "Release"}
        />
      </Link>
      <div className="commerce-line-copy">
        <p className="eyebrow">
          {product?.type ?? "Checking release"} /{" "}
          {product?.categories.join(" / ") ?? ""}
        </p>
        <h2>{product?.title ?? item.title}</h2>
        <p className="tech-label">
          {product
            ? formatPrice(product.price, product.currency)
            : "Checking current price"}{" "}
          / server validated at checkout
        </p>
      </div>
      <div className="commerce-quantity">
        <span className="field-label">Quantity</span>
        <div className="quantity-controls">
          <button
            type="button"
            aria-label={`Decrease quantity for ${item.title}`}
            disabled={item.quantity <= 1}
            onClick={() => onQuantityChange(item.quantity - 1)}
          >
            −
          </button>
          <output aria-label={`Quantity ${item.quantity}`}>
            {item.quantity}
          </output>
          <button
            type="button"
            aria-label={`Increase quantity for ${item.title}`}
            onClick={() => onQuantityChange(item.quantity + 1)}
          >
            +
          </button>
        </div>
      </div>
      <div className="commerce-line-total">
        <span className="field-label">Estimate</span>
        <strong>{formatPrice(subtotal, item.currency)}</strong>
        <button
          className="button button--ghost"
          type="button"
          onClick={onRemove}
        >
          Remove
        </button>
      </div>
    </article>
  );
}

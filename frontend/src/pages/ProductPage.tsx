import { Link, useParams } from "react-router-dom";
import { Badge } from "../components/foundation/Badge";
import { Breadcrumbs } from "../components/foundation/Breadcrumbs";
import { ProductArtwork } from "../components/editorial/ProductArtwork";
import { getErrorMessage } from "../api/errors";
import { useProductQuery } from "../features/catalog/useProductQuery";
import { useCartStore } from "../state/cartStore";
import { AudioPlayer } from "../features/player/AudioPlayer";

function formatPrice(price: number, currency: string) {
  return new Intl.NumberFormat(undefined, {
    style: "currency",
    currency,
  }).format(price);
}

export function ProductPage() {
  const { id } = useParams();
  const productQuery = useProductQuery(id);
  const addItem = useCartStore((state) => state.addItem);
  const inCart = useCartStore((state) =>
    Boolean(id && state.items.some((item) => item.productId === id)),
  );
  const product = productQuery.data;

  if (productQuery.isPending)
    return (
      <section className="product-page">
        <span className="skeleton product-skeleton" />
      </section>
    );
  if (productQuery.isError || !product)
    return (
      <section className="product-page">
        <div className="state-panel" role="alert">
          <h1>Release unavailable</h1>
          <p>
            {getErrorMessage(
              productQuery.error,
              "This release could not be found.",
            )}
          </p>
          <Link className="text-link" to="/beats">
            Return to the index
          </Link>
        </div>
      </section>
    );

  return (
    <section className="product-page" aria-labelledby="product-title">
      <Breadcrumbs
        items={[
          { label: "MedTheG", to: "/" },
          { label: product.categories[0] ?? "Release", to: "/beats" },
          { label: product.title },
        ]}
      />
      <div className="product-layout">
        <div className="product-artwork">
          <ProductArtwork
            productId={product.id}
            coverAssetId={product.coverAssetId}
            index="01"
            label={`${product.title} artwork`}
            metadata={product.type}
          />
        </div>
        <div className="product-copy">
          <Badge>{product.categories.join(" / ")}</Badge>
          <h1 id="product-title">{product.title}</h1>
          <p className="product-type tech-label">{product.type}</p>
          <p className="product-price">
            {formatPrice(product.price, product.currency)}
          </p>
          <button
            className="button button--primary product-cart-button"
            type="button"
            onClick={() =>
              addItem({
                productId: product.id,
                title: product.title,
                price: product.price,
                currency: product.currency,
              })
            }
          >
            {inCart ? "In cart" : "Add to cart"}
          </button>
          <div className="product-preview">
            <p className="eyebrow">Listening preview</p>
            {product.previewAssetId ? (
              <AudioPlayer
                track={{
                  id: `${product.id}:${product.previewAssetId}`,
                  title: product.title,
                  category: product.type,
                  productId: product.id,
                  assetId: product.previewAssetId,
                }}
              />
            ) : (
              <p>No preview audio is available for this release.</p>
            )}
          </div>
          <p className="product-description">{product.description}</p>
          <div className="product-assets">
            <p className="eyebrow">Included asset types</p>
            {product.assets.length > 0 ? (
              <ul>
                {product.assets.map((asset) => (
                  <li key={asset.id}>{asset.type}</li>
                ))}
              </ul>
            ) : (
              <p>No asset details are available.</p>
            )}
          </div>
        </div>
      </div>
    </section>
  );
}

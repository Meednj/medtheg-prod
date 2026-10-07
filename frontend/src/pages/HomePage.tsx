import { useState } from "react";
import { Link } from "react-router-dom";
import { Badge } from "../components/foundation/Badge";
import { EditorialRule } from "../components/editorial/EditorialRule";
import { ProductArtwork } from "../components/editorial/ProductArtwork";
import { LineIcon } from "../components/icons/LineIcon";
import { HeroDustTitle } from "../components/motion/HeroDustTitle";
import { ArchiveItem } from "../features/archive/ArchiveItem";
import { useProductsQuery } from "../features/catalog/useProductsQuery";
import { AudioPlayer } from "../features/player/AudioPlayer";

const categories = [
  { name: "Beats", number: "01", to: "/beats" },
  { name: "Kits", number: "02", to: "/kits" },
  { name: "Courses", number: "03", to: "/courses" },
  { name: "Bundles", number: "04", to: "/bundles" },
];

export function HomePage() {
  const productsQuery = useProductsQuery({
    page: 0,
    size: 8,
    sortBy: "createdAt",
    direction: "desc",
  });
  const [heroFormed, setHeroFormed] = useState(false);
  const [selectedPressing, setSelectedPressing] = useState<string | null>(null);
  const products = productsQuery.data?.content ?? [];
  const featured = products[0];

  return (
    <div className="home-page">
      <div className="home-meta-row">
        <span className="eyebrow">
          Independent production goods / public issue
        </span>
        <span className="tech-label">A home for sound in progress</span>
      </div>
      <section
        className={`home-hero${heroFormed ? " is-formed" : ""}`}
        aria-labelledby="home-title"
      >
        <div className="hero-copy">
          <p className="eyebrow">Production starts after hours</p>
          <HeroDustTitle
            lines={["Build after", "dark."]}
            onFormed={() => setHeroFormed(true)}
          />
          <p className="hero-description">
            Source material for the sessions that stay with you.
          </p>
          <Link className="hero-cta" to="/beats">
            Open the release index <LineIcon name="arrow" />
          </Link>
        </div>
        <EditorialRule className="hero-grid-rule" extend={heroFormed} />
        <div className="hero-artwork">
          {featured ? (
            <ProductArtwork
              productId={featured.id}
              coverAssetId={featured.coverAssetId}
              index="01"
              label={`${featured.title} artwork`}
              metadata={featured.type}
            />
          ) : (
            <span className="artwork-unavailable">Artwork unavailable</span>
          )}
        </div>
      </section>
      <section
        className="featured-transport"
        aria-label="Featured listening transport"
      >
        {featured?.previewAssetId ? (
          <AudioPlayer
            track={{
              id: `${featured.id}:${featured.previewAssetId}`,
              title: featured.title,
              category: featured.type,
              productId: featured.id,
              assetId: featured.previewAssetId,
            }}
          />
        ) : (
          <div className="state-panel">
            <p>
              {productsQuery.isPending
                ? "Loading the public release index."
                : "No preview audio is available for the current releases."}
            </p>
          </div>
        )}
      </section>
      <section className="editorial-section" aria-labelledby="latest-title">
        <div className="section-heading">
          <div>
            <p className="eyebrow">Index / latest public releases</p>
            <h2 id="latest-title">Latest pressings</h2>
          </div>
          <Badge>
            {productsQuery.data
              ? `${productsQuery.data.totalElements} releases`
              : "Public index"}
          </Badge>
        </div>
        <div className="pressing-grid">
          {products.slice(0, 4).map((product, index) => (
            <ArchiveItem
              key={product.id}
              item={{
                title: product.title,
                index: String(index + 1).padStart(2, "0"),
                metadata: [product.type, product.categories.join(" / ")],
                reverse: index % 2 === 1,
                productId: product.id,
                coverAssetId: product.coverAssetId,
                artwork: { alt: `${product.title} artwork` },
              }}
              selected={selectedPressing === product.id}
              onSelect={() =>
                setSelectedPressing(
                  selectedPressing === product.id ? null : product.id,
                )
              }
            />
          ))}
        </div>
        {productsQuery.isError && (
          <div className="state-panel" role="alert">
            <p>Public releases are temporarily unavailable.</p>
          </div>
        )}
        {products.length === 0 && productsQuery.isSuccess && (
          <div className="state-panel">
            <p>No published releases are available yet.</p>
          </div>
        )}
      </section>
      <section className="editorial-section" aria-labelledby="category-title">
        <div className="section-heading">
          <div>
            <p className="eyebrow">Find your raw material</p>
            <h2 id="category-title">The index</h2>
          </div>
          <span className="section-index">04 DEPARTMENTS</span>
        </div>
        <nav className="category-index" aria-label="Product categories">
          {categories.map((category) => (
            <Link
              className="category-link"
              key={category.to}
              to={category.to}
              viewTransition
            >
              <span className="category-number">{category.number}</span>
              <span className="category-name">{category.name}</span>
              <LineIcon className="category-arrow" name="arrow" />
            </Link>
          ))}
        </nav>
      </section>
      {featured && (
        <section className="editorial-section" aria-labelledby="feature-title">
          <div className="section-heading">
            <div>
              <p className="eyebrow">Public release / {featured.type}</p>
              <h2 id="feature-title">{featured.title}</h2>
            </div>
            <span className="section-index">
              {featured.categories.join(" / ")}
            </span>
          </div>
          <div className="editorial-feature">
            <div className="feature-artwork">
              <ProductArtwork
                productId={featured.id}
                coverAssetId={featured.coverAssetId}
                index="01"
                label={`${featured.title} artwork`}
                metadata={featured.type}
              />
            </div>
            <div className="feature-copy">
              <span className="eyebrow">{featured.type}</span>
              <h2>{featured.title}</h2>
              <p>{featured.description}</p>
              <Link className="text-link" to={`/products/${featured.id}`}>
                Open the release <span aria-hidden="true">↗</span>
              </Link>
            </div>
          </div>
        </section>
      )}
    </div>
  );
}

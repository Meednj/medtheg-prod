import { Link } from "react-router-dom";
import { ProductIdLookup } from "../components/admin/ProductIdLookup";

export function AdminProductsPage() {
  return (
    <section className="admin-page" aria-labelledby="admin-products-title">
      <p className="eyebrow">Studio / Admin</p>
      <h1 id="admin-products-title">Product index</h1>
      <p className="admin-lede">
        The backend has no all-status product list. Open a known product
        directly or create a new draft.
      </p>
      <div className="admin-actions">
        <Link className="button button--primary" to="/admin/products/new">
          Create product
        </Link>
      </div>
      <div className="admin-lookup-section">
        <p className="eyebrow">Product ID lookup</p>
        <ProductIdLookup />
      </div>
    </section>
  );
}

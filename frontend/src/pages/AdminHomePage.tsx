import { Link } from "react-router-dom";
import { ProductIdLookup } from "../components/admin/ProductIdLookup";

export function AdminHomePage() {
  return (
    <section className="admin-page" aria-labelledby="admin-title">
      <p className="eyebrow">Studio / Admin</p>
      <h1 id="admin-title">Production desk</h1>
      <p className="admin-lede">
        Create, revise, and release production goods through the available
        catalog operations.
      </p>
      <div className="admin-actions">
        <Link className="button button--primary" to="/admin/products/new">
          Create product
        </Link>
        <Link className="button button--secondary" to="/admin/products">
          Product index
        </Link>
      </div>
      <div className="admin-lookup-section">
        <p className="eyebrow">Direct product access</p>
        <h2>Open a product by ID</h2>
        <ProductIdLookup />
      </div>
    </section>
  );
}

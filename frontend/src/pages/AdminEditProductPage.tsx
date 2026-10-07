import { Link, useNavigate, useParams } from "react-router-dom";
import { Breadcrumbs } from "../components/foundation/Breadcrumbs";
import { ProductForm } from "../components/admin/ProductForm";
import { getErrorMessage } from "../api/errors";
import { useAdminProductQuery } from "../features/admin/useAdminProductQuery";
import { useAdminProductMutations } from "../features/admin/useAdminProductMutations";

export function AdminEditProductPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const query = useAdminProductQuery(id);
  const update = useAdminProductMutations(id).update;
  if (query.isPending)
    return (
      <section className="admin-page">
        <span className="skeleton admin-skeleton" />
      </section>
    );
  if (query.isError || !query.data)
    return (
      <section className="admin-page">
        <div className="state-panel" role="alert">
          <h1>Product unavailable</h1>
          <p>{getErrorMessage(query.error)}</p>
        </div>
      </section>
    );
  return (
    <section className="admin-page" aria-labelledby="edit-product-title">
      <Breadcrumbs
        items={[
          { label: "MedTheG", to: "/" },
          { label: "Admin", to: "/admin" },
          { label: query.data.title, to: `/admin/products/${query.data.id}` },
          { label: "Edit" },
        ]}
      />
      <p className="eyebrow">Studio / Edit release</p>
      <h1 id="edit-product-title">Edit product</h1>
      <ProductForm
        key={query.data.id}
        product={query.data}
        pending={update.isPending}
        onSubmit={async (input) => {
          await update.mutateAsync(input);
          navigate(`/admin/products/${query.data?.id}`);
        }}
      />
      <Link className="text-link" to={`/admin/products/${query.data.id}`}>
        Return to workbench
      </Link>
    </section>
  );
}

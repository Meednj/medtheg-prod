import { useNavigate } from "react-router-dom";
import { Breadcrumbs } from "../components/foundation/Breadcrumbs";
import { ProductForm } from "../components/admin/ProductForm";
import { useAdminProductMutations } from "../features/admin/useAdminProductMutations";

export function AdminNewProductPage() {
  const navigate = useNavigate();
  const create = useAdminProductMutations().create;
  return (
    <section className="admin-page" aria-labelledby="new-product-title">
      <Breadcrumbs
        items={[
          { label: "MedTheG", to: "/" },
          { label: "Admin", to: "/admin" },
          { label: "New product" },
        ]}
      />
      <p className="eyebrow">Studio / New draft</p>
      <h1 id="new-product-title">Create product</h1>
      <ProductForm
        pending={create.isPending}
        onSubmit={async (input) => {
          const product = await create.mutateAsync(input);
          navigate(`/admin/products/${product.id}`);
        }}
      />
    </section>
  );
}

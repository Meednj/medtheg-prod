import { useBlocker, useNavigate } from "react-router-dom";
import { useState } from "react";
import { Breadcrumbs } from "../components/foundation/Breadcrumbs";
import { ProductForm } from "../components/admin/ProductForm";
import { useAdminProductMutations } from "../features/admin/useAdminProductMutations";

export function AdminNewProductPage() {
  const navigate = useNavigate();
  const [dirty, setDirty] = useState(false);
  const create = useAdminProductMutations().create;
  const blocker = useBlocker(dirty && !create.isPending);
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
      {blocker.state === "blocked" && (
        <div className="state-panel admin-unsaved-prompt" role="alertdialog" aria-labelledby="new-unsaved-title">
          <h2 id="new-unsaved-title">Unsaved draft</h2>
          <p>Leave this page and discard the product details you have entered?</p>
          <div className="admin-form-actions">
            <button className="button button--secondary" type="button" onClick={() => blocker.reset()}>Keep editing</button>
            <button className="button button--ghost" type="button" onClick={() => { setDirty(false); blocker.proceed(); }}>Leave without saving</button>
          </div>
        </div>
      )}
      <ProductForm
        pending={create.isPending}
        onDirtyChange={setDirty}
        onSubmit={async (input) => {
          const product = await create.mutateAsync(input);
          setDirty(false);
          navigate(`/admin/products/${product.id}`);
        }}
      />
    </section>
  );
}

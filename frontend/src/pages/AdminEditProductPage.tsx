import { Link, useNavigate, useParams, useBlocker, useLocation } from "react-router-dom";
import { useState } from "react";
import { Breadcrumbs } from "../components/foundation/Breadcrumbs";
import { ProductForm } from "../components/admin/ProductForm";
import { AdminProductNavigation } from "../components/admin/AdminProductNavigation";
import { getErrorMessage } from "../api/errors";
import { useAdminProductQuery } from "../features/admin/useAdminProductQuery";
import { useAdminProductMutations } from "../features/admin/useAdminProductMutations";

export function AdminEditProductPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const [dirty, setDirty] = useState(false);
  const query = useAdminProductQuery(id);
  const update = useAdminProductMutations(id).update;
  const blocker = useBlocker(dirty && !update.isPending);
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
          { label: "Admin", to: `/admin/products${location.search}` },
          { label: query.data.title, to: `/admin/products/${query.data.id}${location.search}` },
          { label: "Edit" },
        ]}
      />
      <p className="eyebrow">Studio / Edit release</p>
      <h1 id="edit-product-title">Edit product</h1>
      <AdminProductNavigation product={query.data} />
      {blocker.state === "blocked" && (
        <div className="state-panel admin-unsaved-prompt" role="alertdialog" aria-labelledby="unsaved-title" aria-describedby="unsaved-description">
          <h2 id="unsaved-title">Unsaved product changes</h2>
          <p id="unsaved-description">Leave this editor and discard the changes you have not saved?</p>
          <div className="admin-form-actions">
            <button className="button button--secondary" type="button" onClick={() => blocker.reset?.()}>Keep editing</button>
            <button className="button button--ghost" type="button" onClick={() => { setDirty(false); blocker.proceed?.(); }}>Leave without saving</button>
          </div>
        </div>
      )}
      <ProductForm
        key={query.data.id}
        product={query.data}
        latestProduct={query.data}
        pending={update.isPending}
        onDirtyChange={setDirty}
        onSubmit={async (input) => {
          await update.mutateAsync(input);
          setDirty(false);
          navigate(`/admin/products/${query.data?.id}`);
        }}
      />
      <Link className="text-link" to={`/admin/products/${query.data.id}${location.search}`}>
        Return to workbench
      </Link>
    </section>
  );
}

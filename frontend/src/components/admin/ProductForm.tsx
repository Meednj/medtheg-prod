import { useEffect, useState, type FormEvent } from "react";
import type { ProductCategory, ProductDto, ProductType } from "../../api/dto";
import type { ProductInput } from "../../api/admin";
import { getErrorMessage } from "../../api/errors";

const categories: ProductCategory[] = ["BEATS", "KITS", "COURSES", "BUNDLES"];
const types: ProductType[] = ["BEAT", "SAMPLE_PACK", "DRUM_KIT", "COURSE", "BUNDLE"];

export function ProductForm({
  product,
  latestProduct = product,
  pending,
  onSubmit,
  onDirtyChange,
}: {
  product?: ProductDto;
  latestProduct?: ProductDto;
  pending: boolean;
  onSubmit: (input: ProductInput) => Promise<void>;
  onDirtyChange?: (dirty: boolean) => void;
}) {
  const [title, setTitle] = useState(product?.title ?? "");
  const [description, setDescription] = useState(product?.description ?? "");
  const [type, setType] = useState<ProductType>(product?.type ?? "BEAT");
  const [price, setPrice] = useState(String(product?.price ?? "0.00"));
  const [selectedCategories, setSelectedCategories] = useState<ProductCategory[]>(product?.categories ?? []);
  const [baseline, setBaseline] = useState<ProductDto | undefined>(product);
  const [error, setError] = useState<string | null>(null);
  const [fieldErrors, setFieldErrors] = useState<{ title?: string; price?: string }>({});
  const saved = baseline;
  const dirty = saved
    ? title !== saved.title || description !== (saved.description ?? "") || Number(price) !== saved.price || JSON.stringify([...selectedCategories].sort()) !== JSON.stringify([...saved.categories].sort())
    : Boolean(title.trim() || description || Number(price) !== 0 || selectedCategories.length > 0 || type !== "BEAT");
  const stale = Boolean(saved && latestProduct && (
    latestProduct.title !== saved.title || latestProduct.description !== saved.description ||
    latestProduct.price !== saved.price || latestProduct.type !== saved.type ||
    JSON.stringify([...latestProduct.categories].sort()) !== JSON.stringify([...saved.categories].sort())
  ));

  useEffect(() => {
    onDirtyChange?.(dirty);
  }, [dirty, onDirtyChange]);

  useEffect(() => {
    if (!dirty) return;
    const handleBeforeUnload = (event: BeforeUnloadEvent) => {
      event.preventDefault();
      event.returnValue = "";
    };
    window.addEventListener("beforeunload", handleBeforeUnload);
    return () => window.removeEventListener("beforeunload", handleBeforeUnload);
  }, [dirty]);

  function toggleCategory(category: ProductCategory) {
    setSelectedCategories((current) => current.includes(category)
      ? current.filter((value) => value !== category)
      : [...current, category]);
  }

  function reset(values = saved) {
    setBaseline(values);
    setTitle(values?.title ?? "");
    setDescription(values?.description ?? "");
    setType(values?.type ?? "BEAT");
    setPrice(String(values?.price ?? "0.00"));
    setSelectedCategories(values?.categories ?? []);
    setError(null);
    setFieldErrors({});
  }

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const numericPrice = Number(price);
    const nextErrors: { title?: string; price?: string } = {};
    if (!title.trim()) nextErrors.title = "Enter a product title.";
    if (!price.trim() || !Number.isFinite(numericPrice) || numericPrice < 0 || !/^\d+(\.\d{1,2})?$/.test(price)) {
      nextErrors.price = "Enter a valid non-negative price with up to two decimal places.";
    }
    setFieldErrors(nextErrors);
    if (Object.keys(nextErrors).length > 0) return;
    if (stale) {
      setError("This product changed on the server while you were editing. Load the latest values before saving.");
      return;
    }
    setError(null);
    try {
      await onSubmit({
        title: title.trim(), description, type, price: numericPrice, categories: selectedCategories,
      });
    } catch (submitError) {
      setError(getErrorMessage(submitError, "The product could not be saved."));
    }
  }

  return (
    <form className="admin-form" onSubmit={submit} noValidate>
      {stale && (
        <div className="state-panel" role="alert">
          <p>The saved product changed since this form loaded. Your draft remains here; saving is paused to avoid overwriting the newer version.</p>
          <button className="button button--secondary" type="button" onClick={() => reset(latestProduct)}>Discard edits and load latest</button>
        </div>
      )}
      {error && <div className="state-panel" role="alert"><p>{error}</p></div>}
      <label className="field" htmlFor="product-title">
        <span className="field-label">Title</span>
        <input id="product-title" className="input" value={title} maxLength={200} aria-invalid={Boolean(fieldErrors.title)} aria-describedby={fieldErrors.title ? "product-title-error" : undefined} onChange={(event) => { setTitle(event.target.value); setFieldErrors((current) => ({ ...current, title: undefined })); }} />
        {fieldErrors.title && <span className="field-error" id="product-title-error">{fieldErrors.title}</span>}
      </label>
      <label className="field" htmlFor="product-description">
        <span className="field-label">Description</span>
        <textarea id="product-description" className="input admin-textarea" value={description} onChange={(event) => setDescription(event.target.value)} />
      </label>
      <div className="admin-form-pair">
        <label className="field" htmlFor="product-type">
          <span className="field-label">Product type</span>
          {product ? <><input id="product-type" className="input" value={type} readOnly aria-describedby="product-type-note" /><span className="admin-muted" id="product-type-note">Product type is fixed when the product is created.</span></> : (
            <select id="product-type" className="select" value={type} onChange={(event) => setType(event.target.value as ProductType)}>
              {types.map((value) => <option key={value} value={value}>{value.replaceAll("_", " ")}</option>)}
            </select>
          )}
        </label>
        <label className="field" htmlFor="product-price">
          <span className="field-label">Price / EUR</span>
          <input id="product-price" className="input" type="number" min="0" step="0.01" inputMode="decimal" value={price} aria-invalid={Boolean(fieldErrors.price)} aria-describedby={fieldErrors.price ? "product-price-error" : undefined} onChange={(event) => { setPrice(event.target.value); setFieldErrors((current) => ({ ...current, price: undefined })); }} />
          {fieldErrors.price && <span className="field-error" id="product-price-error">{fieldErrors.price}</span>}
        </label>
      </div>
      <fieldset className="admin-categories">
        <legend className="field-label">Categories</legend>
        {categories.map((category) => <label key={category}><input type="checkbox" checked={selectedCategories.includes(category)} onChange={() => toggleCategory(category)} />{category}</label>)}
      </fieldset>
      <div className="admin-form-actions">
        <button className="button button--primary" type="submit" disabled={pending || !dirty || stale}>{pending ? "Saving…" : "Save changes"}</button>
        <button className="button button--secondary" type="button" disabled={pending || !dirty} onClick={() => reset()}>Reset to saved values</button>
        <span className="admin-form-state" aria-live="polite">{pending ? "Saving product" : dirty ? "Unsaved changes" : "All changes saved"}</span>
      </div>
    </form>
  );
}

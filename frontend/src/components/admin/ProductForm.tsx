import { useState, type FormEvent } from "react";
import type { ProductCategory, ProductDto, ProductType } from "../../api/dto";
import type { ProductInput } from "../../api/admin";
import { getErrorMessage } from "../../api/errors";

const categories: ProductCategory[] = ["BEATS", "KITS", "COURSES", "BUNDLES"];
const types: ProductType[] = [
  "BEAT",
  "SAMPLE_PACK",
  "DRUM_KIT",
  "COURSE",
  "BUNDLE",
];

export function ProductForm({
  product,
  pending,
  onSubmit,
}: {
  product?: ProductDto;
  pending: boolean;
  onSubmit: (input: ProductInput) => Promise<void>;
}) {
  const [title, setTitle] = useState(product?.title ?? "");
  const [description, setDescription] = useState(product?.description ?? "");
  const [type, setType] = useState<ProductType>(product?.type ?? "BEAT");
  const [price, setPrice] = useState(String(product?.price ?? "0.00"));
  const [selectedCategories, setSelectedCategories] = useState<
    ProductCategory[]
  >(product?.categories ?? []);
  const [error, setError] = useState<string | null>(null);

  function toggleCategory(category: ProductCategory) {
    setSelectedCategories((current) =>
      current.includes(category)
        ? current.filter((value) => value !== category)
        : [...current, category],
    );
  }

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const numericPrice = Number(price);
    if (!title.trim() || !Number.isFinite(numericPrice) || numericPrice < 0) {
      setError("Enter a title and a non-negative price.");
      return;
    }
    setError(null);
    try {
      await onSubmit({
        title: title.trim(),
        description,
        type,
        price: numericPrice,
        categories: selectedCategories,
      });
    } catch (submitError) {
      setError(getErrorMessage(submitError, "The product could not be saved."));
    }
  }

  return (
    <form className="admin-form" onSubmit={submit}>
      {error && (
        <div className="state-panel" role="alert">
          <p>{error}</p>
        </div>
      )}
      <label className="field">
        <span className="field-label">Title</span>
        <input
          className="input"
          value={title}
          onChange={(event) => setTitle(event.target.value)}
          required
        />
      </label>
      <label className="field">
        <span className="field-label">Description</span>
        <textarea
          className="input admin-textarea"
          value={description}
          onChange={(event) => setDescription(event.target.value)}
        />
      </label>
      <label className="field">
        <span className="field-label">Product type</span>
        <select
          className="select"
          value={type}
          onChange={(event) => setType(event.target.value as ProductType)}
        >
          {types.map((value) => (
            <option key={value}>{value}</option>
          ))}
        </select>
      </label>
      <label className="field">
        <span className="field-label">Price</span>
        <input
          className="input"
          type="number"
          min="0"
          step="0.01"
          value={price}
          onChange={(event) => setPrice(event.target.value)}
          required
        />
      </label>
      <fieldset className="admin-categories">
        <legend className="field-label">Categories</legend>
        {categories.map((category) => (
          <label key={category}>
            <input
              type="checkbox"
              checked={selectedCategories.includes(category)}
              onChange={() => toggleCategory(category)}
            />{" "}
            {category}
          </label>
        ))}
      </fieldset>
      <button
        className="button button--primary"
        type="submit"
        disabled={pending}
      >
        {pending ? "Saving..." : "Save draft"}
      </button>
    </form>
  );
}

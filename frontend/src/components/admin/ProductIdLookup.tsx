import { useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";

const uuidPattern =
  /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;

export function ProductIdLookup() {
  const [value, setValue] = useState("");
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const productId = value.trim();
    if (!uuidPattern.test(productId)) {
      setError("Enter a valid product UUID.");
      return;
    }
    setError(null);
    navigate(`/admin/products/${productId}`);
  }

  return (
    <form className="admin-lookup" onSubmit={submit}>
      <label className="field">
        <span className="field-label">Product ID</span>
        <input
          className="input"
          value={value}
          onChange={(event) => setValue(event.target.value)}
          placeholder="UUID"
          aria-describedby={error ? "product-id-error" : undefined}
        />
      </label>
      <button className="button button--secondary" type="submit">
        Open product
      </button>
      {error && (
        <p id="product-id-error" className="field-error" role="alert">
          {error}
        </p>
      )}
    </form>
  );
}

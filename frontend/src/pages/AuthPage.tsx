import { useState, type FormEvent } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { loginRequest, registerRequest } from "../api/auth";
import { getErrorMessage } from "../api/errors";
import { useAuthStore } from "../state/authStore";

export function AuthPage({ mode }: { mode: "login" | "register" }) {
  const isLogin = mode === "login";
  const navigate = useNavigate();
  const location = useLocation();
  const setSession = useAuthStore((state) => state.setSession);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  function getReturnDestination() {
    const returnTo = new URLSearchParams(location.search).get("returnTo");
    return returnTo?.startsWith("/") && !returnTo.startsWith("//")
      ? returnTo
      : "/";
  }

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setMessage(null);
    setIsSubmitting(true);
    try {
      if (isLogin) {
        const session = await loginRequest(email, password);
        setSession(session);
        navigate(getReturnDestination());
      } else {
        await registerRequest(email, password);
        navigate(
          `/login?returnTo=${encodeURIComponent(getReturnDestination())}`,
          { replace: true },
        );
      }
    } catch (error) {
      setMessage(
        getErrorMessage(
          error,
          isLogin
            ? "Sign in failed. Check your details and try again."
            : "Account creation failed. Check your details and try again.",
        ),
      );
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <section className="auth-page" aria-labelledby="auth-title">
      <div className="auth-copy">
        <p className="eyebrow">Customer / {isLogin ? "01" : "02"}</p>
        <h1 id="auth-title">{isLogin ? "Sign in" : "Create account"}</h1>
        <p>
          {isLogin
            ? "Return to your releases and private library."
            : "Create a customer account for checkout and your private library."}
        </p>
      </div>
      <form className="auth-form" onSubmit={submit}>
        {message && (
          <p className="state-panel" role="alert">
            {message}
          </p>
        )}
        <label className="field">
          <span className="field-label">Email</span>
          <input
            className="input"
            type="email"
            autoComplete="email"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            required
          />
        </label>
        <label className="field">
          <span className="field-label">Password</span>
          <input
            className="input"
            type="password"
            autoComplete={isLogin ? "current-password" : "new-password"}
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            required
          />
        </label>
        <button
          className="button button--primary"
          type="submit"
          disabled={isSubmitting}
        >
          {isSubmitting ? "Working..." : isLogin ? "Sign in" : "Create account"}
        </button>
        <p className="auth-switch">
          {isLogin ? "Need an account?" : "Already registered?"}{" "}
          <Link to={isLogin ? "/register" : "/login"}>
            {isLogin ? "Create one" : "Sign in"}
          </Link>
        </p>
      </form>
    </section>
  );
}

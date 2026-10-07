import { Navigate, useLocation } from "react-router-dom";
import { useAuthStore } from "../state/authStore";

export function RequireAuth({
  children,
  role,
}: {
  children: React.ReactNode;
  role?: "ADMIN" | "CUSTOMER";
}) {
  const location = useLocation();
  const user = useAuthStore((state) => state.user);
  if (!user) {
    const returnTo = `${location.pathname}${location.search}`;
    return (
      <Navigate
        replace
        to={`/login?returnTo=${encodeURIComponent(returnTo)}`}
      />
    );
  }
  if (role && user.role !== role) return <Navigate replace to="/" />;
  return children;
}

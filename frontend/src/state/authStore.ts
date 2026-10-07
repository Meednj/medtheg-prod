import { create } from "zustand";
import { setApiTokenProvider, setApiUnauthorizedHandler } from "../api/client";
import type { AuthResponseDto } from "../api/dto";
import { queryClient } from "../api/queryClient";
import { queryKeys } from "../api/queryKeys";

type AuthState = {
  accessToken: string | null;
  user: Pick<AuthResponseDto, "userId" | "email" | "role"> | null;
  setSession: (session: AuthResponseDto) => void;
  clearSession: () => void;
};

const storageKey = "medtheg.auth";

function clearUserQueries() {
  queryClient.removeQueries({ queryKey: queryKeys.ordersRoot });
  queryClient.removeQueries({ queryKey: queryKeys.library });
  queryClient.removeQueries({ queryKey: queryKeys.entitlements });
  queryClient.removeQueries({ queryKey: queryKeys.adminProductsRoot });
}

function readStoredSession(): Pick<AuthState, "accessToken" | "user"> {
  try {
    const stored = localStorage.getItem(storageKey);
    if (!stored) return { accessToken: null, user: null };
    return JSON.parse(stored) as Pick<AuthState, "accessToken" | "user">;
  } catch {
    return { accessToken: null, user: null };
  }
}

export const useAuthStore = create<AuthState>((set) => ({
  ...readStoredSession(),
  setSession: (session) => {
    clearUserQueries();
    const next = {
      accessToken: session.accessToken,
      user: {
        userId: session.userId,
        email: session.email,
        role: session.role,
      },
    };
    localStorage.setItem(storageKey, JSON.stringify(next));
    set(next);
  },
  clearSession: () => {
    clearUserQueries();
    localStorage.removeItem(storageKey);
    set({ accessToken: null, user: null });
  },
}));

setApiTokenProvider(() => useAuthStore.getState().accessToken);
setApiUnauthorizedHandler(() => useAuthStore.getState().clearSession());

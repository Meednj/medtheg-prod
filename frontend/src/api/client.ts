import { ApiError } from "./errors";
import type { ApiErrorDto } from "./dto";

const apiBaseUrl = (import.meta.env.VITE_API_BASE_URL?.trim() || "/api").replace(
  /\/$/,
  "",
);

export type TokenProvider = () => string | null;
export type UnauthorizedHandler = () => void;

let tokenProvider: TokenProvider = () => null;
let unauthorizedHandler: UnauthorizedHandler = () => undefined;

export function setApiTokenProvider(provider: TokenProvider) {
  tokenProvider = provider;
}

export function setApiUnauthorizedHandler(handler: UnauthorizedHandler) {
  unauthorizedHandler = handler;
}

async function parseResponseBody(response: Response): Promise<unknown> {
  const contentType = response.headers.get("content-type") ?? "";
  if (!/\bapplication\/(?:[\w.-]+\+)?json\b/i.test(contentType)) return null;
  try {
    return await response.json();
  } catch {
    return null;
  }
}

export async function apiRequest<T>(
  path: string,
  init: RequestInit = {},
): Promise<T> {
  const headers = new Headers(init.headers);
  headers.set("Accept", "application/json");
  if (typeof init.body === "string" && !headers.has("Content-Type"))
    headers.set("Content-Type", "application/json");
  const token = tokenProvider();
  if (token) headers.set("Authorization", `Bearer ${token}`);

  let response: Response;
  try {
    response = await fetch(`${apiBaseUrl}${path}`, { ...init, headers });
  } catch {
    throw new ApiError(
      "The network is unavailable. Check your connection and try again.",
    );
  }

  const body = await parseResponseBody(response);
  if (!response.ok) {
    if (response.status === 401 && token) unauthorizedHandler();
    const details = (
      body && typeof body === "object" ? body : {}
    ) as ApiErrorDto;
    throw new ApiError(
      details.message ?? `Request failed with status ${response.status}.`,
      {
        ...details,
        status: details.status ?? response.status,
      },
    );
  }
  return body as T;
}

export function apiGet<T>(path: string, init?: RequestInit) {
  return apiRequest<T>(path, { ...init, method: "GET" });
}

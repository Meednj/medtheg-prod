import type { ApiErrorDto } from "./dto";

export class ApiError extends Error {
  readonly status: number | null;
  readonly code: string | null;
  readonly path: string | null;
  readonly fields: Record<string, string>;

  constructor(message: string, details: ApiErrorDto = {}) {
    super(message);
    this.name = "ApiError";
    this.status = details.status ?? null;
    this.code = details.code ?? details.error ?? null;
    this.path = details.path ?? null;
    this.fields = details.errors ?? {};
  }
}

export function isApiError(error: unknown): error is ApiError {
  return error instanceof ApiError;
}

export function getErrorMessage(
  error: unknown,
  fallback = "Something went wrong. Please try again.",
) {
  if (error instanceof ApiError) return error.message;
  if (error instanceof Error && error.message) return error.message;
  return fallback;
}

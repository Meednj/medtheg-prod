import { apiRequest } from "./client";
import type { AuthResponseDto, RegisterResponseDto } from "./dto";

export function loginRequest(email: string, password: string) {
  return apiRequest<AuthResponseDto>("/auth/login", {
    method: "POST",
    body: JSON.stringify({ email, password }),
  });
}

export function registerRequest(email: string, password: string) {
  return apiRequest<RegisterResponseDto>("/auth/register", {
    method: "POST",
    body: JSON.stringify({ email, password }),
  });
}

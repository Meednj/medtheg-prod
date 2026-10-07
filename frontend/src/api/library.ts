import { apiGet } from "./client";
import type { DownloadUrlDto, LibraryItemDto } from "./dto";

export function getLibrary() {
  return apiGet<LibraryItemDto[]>("/library");
}

export function getLibraryAssetDownloadUrl(assetId: string) {
  return apiGet<DownloadUrlDto>(`/library/assets/${assetId}/download`);
}

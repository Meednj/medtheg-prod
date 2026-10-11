import { apiRequest } from "./client";
import type {
  AssetType,
  ProductCategory,
  ProductDto,
  ProductType,
  AdminProductQuery,
  ProductPageDto,
  PreviewUrlDto,
} from "./dto";

export type ProductInput = {
  title: string;
  description: string;
  type: ProductType;
  price: number;
  categories: ProductCategory[];
};

export function getAdminProduct(productId: string) {
  return apiRequest<ProductDto>(`/products/admin/${productId}`);
}

export function getAdminProducts(query: AdminProductQuery) {
  const params = new URLSearchParams();
  for (const [key, value] of Object.entries(query)) {
    if (value !== undefined && value !== "") params.set(key, String(value));
  }
  return apiRequest<ProductPageDto>(`/products/admin?${params.toString()}`);
}

export function getAdminAssetPreview(productId: string, assetId: string, type: "IMAGE" | "AUDIO_PREVIEW") {
  const params = new URLSearchParams({ type });
  return apiRequest<PreviewUrlDto>(`/products/admin/${productId}/assets/${assetId}/preview?${params.toString()}`);
}

export function createProduct(input: ProductInput) {
  return apiRequest<ProductDto>("/products", {
    method: "POST",
    body: JSON.stringify(input),
  });
}

export function updateProduct(productId: string, input: ProductInput) {
  return apiRequest<ProductDto>(`/products/${productId}`, {
    method: "PUT",
    body: JSON.stringify(input),
  });
}

export function publishProduct(productId: string) {
  return apiRequest<ProductDto>(`/products/${productId}/publish`, {
    method: "POST",
  });
}

export function archiveProduct(productId: string) {
  return apiRequest<ProductDto>(`/products/${productId}/archive`, {
    method: "POST",
  });
}

export function deleteProductAsset(productId: string, assetId: string) {
  return apiRequest<ProductDto>(`/products/${productId}/assets/${assetId}`, {
    method: "DELETE",
  });
}

export async function uploadProductAsset(
  productId: string,
  file: File,
  type: AssetType,
) {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("type", type);
  const response = await apiRequest<{ assetId: string; type: AssetType }>(
    `/products/${productId}/assets/upload`,
    {
      method: "POST",
      body: formData,
    },
  );
  return { assetId: response.assetId, type: response.type };
}

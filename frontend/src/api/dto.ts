export type ProductCategory = "BEATS" | "KITS" | "COURSES" | "BUNDLES";
export type ProductType =
  | "BEAT"
  | "SAMPLE_PACK"
  | "DRUM_KIT"
  | "COURSE"
  | "BUNDLE";
export type ProductStatus = "DRAFT" | "PUBLISHED" | "ARCHIVED";
export type AssetType =
  | "IMAGE"
  | "AUDIO_PREVIEW"
  | "AUDIO_MP3"
  | "AUDIO_WAV"
  | "STEMS"
  | "MIDI"
  | "PDF"
  | "VIDEO"
  | "OTHER";

export type ProductAssetDto = {
  id: string;
  type: AssetType;
};

export type ProductDto = {
  id: string;
  title: string;
  description: string;
  type: ProductType;
  price: number;
  currency: string;
  status: ProductStatus;
  categories: ProductCategory[];
  coverAssetId: string | null;
  previewAssetId: string | null;
  assets: ProductAssetDto[];
};

export type ProductPageDto = {
  content: ProductDto[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
};

export type CatalogQuery = {
  page?: number;
  size?: number;
  type?: ProductType;
  category?: ProductCategory;
  search?: string;
  sortBy?: string;
  direction?: "asc" | "desc";
};

export type AuthResponseDto = {
  userId: string;
  email: string;
  role: "CUSTOMER" | "ADMIN";
  accessToken: string;
};

export type RegisterResponseDto = {
  id: string;
  email: string;
  role: "CUSTOMER" | "ADMIN";
  status: "ACTIVE" | "SUSPENDED";
};

export type PreviewUrlDto = { url: string };
export type CoverUrlDto = { url: string };

export type CreateOrderItemRequest = {
  productId: string;
  quantity: number;
};

export type CreateOrderRequest = {
  items: CreateOrderItemRequest[];
};

export type OrderResponseDto = {
  id: string;
  customerId: string;
  status: "PENDING" | "PAID" | "CANCELLED" | "REFUNDED";
  total: number;
  currency: string;
  createdAt: string;
  paidAt: string | null;
  items: Array<{
    id: string;
    productId: string;
    productTitle: string;
    unitPrice: number;
    currency: string;
    quantity: number;
    subtotal: number;
  }>;
};

export type OrderHistoryResponseDto = {
  content: OrderResponseDto[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
};

export type CreatePaymentResponseDto = {
  orderId: string;
  paymentId: string;
  checkoutUrl: string;
};

export type LibraryAssetDto = {
  assetId: string;
  productId: string;
  type:
    | "IMAGE"
    | "AUDIO_PREVIEW"
    | "AUDIO_MP3"
    | "AUDIO_WAV"
    | "STEMS"
    | "MIDI"
    | "PDF"
    | "VIDEO"
    | "OTHER";
};

export type LibraryItemDto = {
  entitlementId: string;
  productId: string;
  orderId: string;
  title: string;
  description: string;
  type: string;
  price: number;
  currency: string;
  grantedAt: string;
  assets: LibraryAssetDto[];
};

export type DownloadUrlDto = { url: string };

export type ApiErrorDto = {
  timestamp?: string;
  status?: number;
  code?: string;
  error?: string;
  message?: string;
  path?: string;
  errors?: Record<string, string>;
};

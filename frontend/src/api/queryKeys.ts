export const queryKeys = {
  products: (query: Record<string, unknown> = {}) =>
    ["products", query] as const,
  product: (id: string) => ["product", id] as const,
  productsRoot: ["products"] as const,
  adminProduct: (id: string) => ["admin-product", id] as const,
  adminProductsRoot: ["admin-product"] as const,
  adminProducts: (query: Record<string, unknown> = {}) => ["admin-products", query] as const,
  adminProductsListRoot: ["admin-products"] as const,
  adminAssetPreview: (productId: string, assetId: string) => ["admin-asset-preview", productId, assetId] as const,
  cover: (productId: string, assetId: string) =>
    ["product-cover", productId, assetId] as const,
  coversRoot: (productId?: string) =>
    productId ? (["product-cover", productId] as const) : (["product-cover"] as const),
  ordersRoot: ["orders"] as const,
  orderHistory: (page: number, size: number) => ["orders", page, size] as const,
  order: (orderId: string) => ["orders", "detail", orderId] as const,
  library: ["library"] as const,
  entitlements: ["entitlements"] as const,
};

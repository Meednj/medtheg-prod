import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  archiveProduct,
  createProduct,
  deleteProductAsset,
  publishProduct,
  updateProduct,
  uploadProductAsset,
  type ProductInput,
} from "../../api/admin";
import { queryKeys } from "../../api/queryKeys";
import type { AssetType } from "../../api/dto";

function useInvalidateProductData(productId?: string) {
  const queryClient = useQueryClient();
  return async () => {
    const invalidations = [
      queryClient.invalidateQueries({ queryKey: queryKeys.productsRoot }),
      queryClient.invalidateQueries({ queryKey: queryKeys.adminProductsListRoot }),
    ];
    if (productId) {
      invalidations.push(
        queryClient.invalidateQueries({ queryKey: queryKeys.adminProduct(productId) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.product(productId) }),
        queryClient.invalidateQueries({ queryKey: queryKeys.coversRoot(productId) }),
      );
    }
    await Promise.all(invalidations);
  };
}

export function useAdminProductMutations(productId?: string) {
  const invalidate = useInvalidateProductData(productId);
  return {
    create: useMutation({
      mutationFn: (input: ProductInput) => createProduct(input),
      onSuccess: invalidate,
    }),
    update: useMutation({
      mutationFn: (input: ProductInput) =>
        updateProduct(productId ?? "", input),
      onSuccess: invalidate,
    }),
    publish: useMutation({
      mutationFn: () => publishProduct(productId ?? ""),
      onSuccess: invalidate,
    }),
    archive: useMutation({
      mutationFn: () => archiveProduct(productId ?? ""),
      onSuccess: invalidate,
    }),
    removeAsset: useMutation({
      mutationFn: (assetId: string) =>
        deleteProductAsset(productId ?? "", assetId),
      onSuccess: invalidate,
    }),
    upload: useMutation({
      mutationFn: ({ file, type }: { file: File; type: AssetType }) =>
        uploadProductAsset(productId ?? "", file, type),
      onSuccess: invalidate,
    }),
  };
}

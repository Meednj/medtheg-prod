/**
 * Build the stable View Transition name for a real API product artwork asset.
 * Only call this with a persisted ProductResponse.id; temporary homepage preview
 * entries must not use this helper.
 */
export function releaseArtworkTransitionName(productId: string): string {
  const safeId = productId.trim().replace(/[^a-zA-Z0-9_-]/g, '-')
  if (!safeId) throw new Error('A persisted product ID is required for an artwork transition.')
  return `release-artwork-${safeId}`
}

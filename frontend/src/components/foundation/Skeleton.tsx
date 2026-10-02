type SkeletonProps = { width?: string; height?: string; className?: string }

export function Skeleton({ width = '100%', height = '16px', className = '' }: SkeletonProps) {
  return (
    <span
      className={`skeleton ${className}`.trim()}
      aria-hidden="true"
      style={{ width, minHeight: height }}
    />
  )
}

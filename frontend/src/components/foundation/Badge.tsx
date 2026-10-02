import type { HTMLAttributes, ReactNode } from 'react'

type BadgeProps = HTMLAttributes<HTMLSpanElement> & {
  variant?: 'outline' | 'inverse'
  children: ReactNode
}

export function Badge({ variant = 'outline', className = '', children, ...props }: BadgeProps) {
  return <span className={`badge badge--${variant} ${className}`.trim()} {...props}>{children}</span>
}

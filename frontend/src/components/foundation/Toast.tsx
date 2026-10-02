import type { HTMLAttributes, ReactNode } from 'react'

type ToastProps = HTMLAttributes<HTMLDivElement> & {
  children: ReactNode
  urgent?: boolean
}

export function Toast({ children, urgent = false, className = '', ...props }: ToastProps) {
  return (
    <div className={`toast ${className}`.trim()} role={urgent ? 'alert' : 'status'} {...props}>
      {children}
    </div>
  )
}

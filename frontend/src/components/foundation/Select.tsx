import type { ReactNode, SelectHTMLAttributes } from 'react'

type SelectProps = SelectHTMLAttributes<HTMLSelectElement> & {
  label: string
  children: ReactNode
}

export function Select({ id, label, children, className = '', ...props }: SelectProps) {
  const selectId = id ?? props.name
  return (
    <div className="field">
      <label className="field-label" htmlFor={selectId}>{label}</label>
      <select id={selectId} className={`select ${className}`.trim()} {...props}>
        {children}
      </select>
    </div>
  )
}

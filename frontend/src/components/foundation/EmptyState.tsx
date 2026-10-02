import type { ReactNode } from 'react'

type EmptyStateProps = { title: string; children: ReactNode; action?: ReactNode }

export function EmptyState({ title, children, action }: EmptyStateProps) {
  return (
    <section className="state-panel" aria-labelledby="empty-state-title">
      <h2 id="empty-state-title">{title}</h2>
      <p>{children}</p>
      {action && <div className="state-panel-action">{action}</div>}
    </section>
  )
}

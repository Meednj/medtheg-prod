import type { ReactNode } from 'react'

type ErrorStateProps = { title: string; children: ReactNode; action?: ReactNode }

export function ErrorState({ title, children, action }: ErrorStateProps) {
  return (
    <section className="state-panel" role="alert" aria-labelledby="error-state-title">
      <h2 id="error-state-title">{title}</h2>
      <p>{children}</p>
      {action && <div className="state-panel-action">{action}</div>}
    </section>
  )
}

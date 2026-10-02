import { useParams } from 'react-router-dom'
import { Badge } from '../components/foundation/Badge'
import { Breadcrumbs } from '../components/foundation/Breadcrumbs'

type PlaceholderPageProps = { eyebrow: string; title: string; detail: string; rhythm?: 'beats' | 'kits' | 'courses' | 'bundles' }

export function PlaceholderPage({ eyebrow, title, detail, rhythm }: PlaceholderPageProps) {
  const { id, productId } = useParams()
  const identifier = id ?? productId

  return (
    <section className={`route-placeholder${rhythm ? ` route-placeholder--${rhythm}` : ''}`} aria-labelledby="placeholder-title">
      <div className="route-placeholder-copy">
        <Breadcrumbs items={[{ label: 'MedTheG', to: '/' }, { label: title }]} />
        <p className="eyebrow">{eyebrow}</p>
        <h1 id="placeholder-title">{title}</h1>
        <p>{detail}</p>
        <div className="state-panel-action"><Badge variant="inverse">Phase 01 / Shell study</Badge></div>
      </div>
      <div className="route-placeholder-rule" />
      <div className="route-placeholder-meta tech-label">
        <span>{identifier ? `Reference / ${identifier}` : 'Feature content is not connected yet'}</span>
        <span>MedTheG / Production goods</span>
      </div>
    </section>
  )
}

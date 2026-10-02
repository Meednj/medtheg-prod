import { Link } from 'react-router-dom'

export function NotFoundPage() {
  return (
    <section className="not-found" aria-labelledby="not-found-title">
      <div>
        <p className="not-found-code">404 / OUT OF INDEX</p>
        <h1 id="not-found-title">This groove isn’t in the archive.</h1>
        <p>The address may have shifted. Return to the MedTheG front page and find another starting point.</p>
        <div className="state-panel-action"><Link className="button button--primary" to="/">Return to the front page</Link></div>
      </div>
    </section>
  )
}

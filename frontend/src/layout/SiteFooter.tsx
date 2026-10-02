import { Link } from 'react-router-dom'

export function SiteFooter() {
  return (
    <footer className="site-footer">
      <Link className="footer-wordmark" to="/">MEDTHEG</Link>
      <p className="tech-label">Independent production goods · Issue 01</p>
    </footer>
  )
}

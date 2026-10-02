import { Outlet, useLocation } from 'react-router-dom'
import { StoreHeader } from '../components/navigation/StoreHeader'
import { GlobalMiniPlayer } from '../features/player/GlobalMiniPlayer'
import { SiteFooter } from './SiteFooter'
import { useVisualPlayer } from '../features/player/useVisualPlayer'

export function AppShell() {
  const location = useLocation()
  const { track } = useVisualPlayer()
  const path = location.pathname
  const routeKind = path === '/' ? 'home'
    : path.startsWith('/products/') ? 'release'
      : path.startsWith('/library') || path.startsWith('/orders') ? 'private'
        : path.startsWith('/admin') ? 'studio'
          : path.startsWith('/login') || path.startsWith('/register') || path.startsWith('/cart') || path.startsWith('/checkout') || path.startsWith('/payment') ? 'account'
            : 'catalog'
  return (
    <div className={`app-frame${track ? ' app-frame--has-player' : ''}`}>
      <a className="skip-link" href="#main-content">Skip to content</a>
      <StoreHeader />
      <main id="main-content" className="page-main" tabIndex={-1}>
        <div key={location.pathname} className={`route-stage route-stage--${routeKind}`}><Outlet /></div>
      </main>
      <GlobalMiniPlayer />
      <SiteFooter />
    </div>
  )
}

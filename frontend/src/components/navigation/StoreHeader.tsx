import { useState, type FormEvent } from 'react'
import { Link, NavLink, useNavigate } from 'react-router-dom'
import { Button } from '../foundation/Button'
import { Dialog } from '../foundation/Dialog'
import { Input } from '../foundation/Input'
import { LineIcon } from '../icons/LineIcon'
import { MobileNavigation } from './MobileNavigation'

const categories = [
  { label: 'Beats', to: '/beats' },
  { label: 'Kits', to: '/kits' },
  { label: 'Courses', to: '/courses' },
  { label: 'Bundles', to: '/bundles' },
]

export function StoreHeader() {
  const [searchOpen, setSearchOpen] = useState(false)
  const [menuOpen, setMenuOpen] = useState(false)
  const [query, setQuery] = useState('')
  const navigate = useNavigate()

  function submitSearch(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    const search = query.trim()
    if (!search) return
    setSearchOpen(false)
    setMenuOpen(false)
    navigate(`/beats?search=${encodeURIComponent(search)}`)
  }

  return (
    <>
      <header className="site-header">
        <div className="masthead">
          <Link className="brand-lockup" to="/" aria-label="MedTheG home">
            <img className="brand-mark" src="/medtheg-logo.svg" alt="" aria-hidden="true" />
            <span className="brand-type-lockup">
            <span className="brand-wordmark">MEDTHEG</span>
            <span className="brand-caption">Production goods / independent issue 01</span>
            </span>
          </Link>

          <nav className="desktop-nav" aria-label="Main navigation">
            {categories.map((category) => (
              <NavLink key={category.to} className="nav-link" to={category.to} viewTransition>
                {category.label}
              </NavLink>
            ))}
          </nav>

          <div className="header-actions">
            <button className="header-action" type="button" onClick={() => setSearchOpen(true)}>
              <LineIcon name="search" /> Search
            </button>
            <NavLink className="header-action" to="/login">Account</NavLink>
            <NavLink className="header-action" to="/cart" aria-label="Cart, 0 items">
              <LineIcon name="bag" /> Cart <span className="header-cart-count">0</span>
            </NavLink>
          </div>

          <div className="mobile-header-actions">
            <button className="mobile-menu-trigger" type="button" onClick={() => setMenuOpen(true)} aria-label="Open menu">
              <LineIcon name="menu" /> Menu
            </button>
            <NavLink className="header-action" to="/cart" aria-label="Cart, 0 items">
              <LineIcon name="bag" /><span className="header-cart-count">0</span>
            </NavLink>
          </div>
        </div>
      </header>

      <Dialog open={searchOpen} onOpenChange={setSearchOpen} label="Search the catalogue">
        <div className="search-dialog-content">
          <div className="dialog-header">
            <h2 className="dialog-title">Search the index</h2>
            <button className="icon-button" type="button" onClick={() => setSearchOpen(false)} aria-label="Close search">
              <LineIcon name="close" />
            </button>
          </div>
          <form className="search-dialog-form" onSubmit={submitSearch}>
            <Input label="Search releases" name="search" autoFocus value={query} onChange={(event) => setQuery(event.target.value)} placeholder="Title or keyword" />
            <Button type="submit" variant="secondary">Search</Button>
          </form>
        </div>
      </Dialog>

      <MobileNavigation
        open={menuOpen}
        onOpenChange={setMenuOpen}
        query={query}
        onQueryChange={setQuery}
        onSearch={submitSearch}
      />
    </>
  )
}

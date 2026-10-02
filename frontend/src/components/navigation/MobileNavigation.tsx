import type { ChangeEvent, FormEvent } from 'react'
import { Link, NavLink } from 'react-router-dom'
import { Dialog } from '../foundation/Dialog'
import { Input } from '../foundation/Input'
import { LineIcon } from '../icons/LineIcon'

const categories = [
  { label: 'Beats', to: '/beats', number: '01' },
  { label: 'Kits', to: '/kits', number: '02' },
  { label: 'Courses', to: '/courses', number: '03' },
  { label: 'Bundles', to: '/bundles', number: '04' },
]

type MobileNavigationProps = {
  open: boolean
  onOpenChange: (open: boolean) => void
  query: string
  onQueryChange: (value: string) => void
  onSearch: (event: FormEvent<HTMLFormElement>) => void
}

export function MobileNavigation({ open, onOpenChange, query, onQueryChange, onSearch }: MobileNavigationProps) {
  function changeQuery(event: ChangeEvent<HTMLInputElement>) {
    onQueryChange(event.target.value)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange} label="Main navigation" className="dialog--full-screen">
      <div className="mobile-menu-content">
        <div className="dialog-header">
          <span className="eyebrow">The MedTheG index</span>
          <button className="icon-button" type="button" onClick={() => onOpenChange(false)} aria-label="Close menu">
            <LineIcon name="close" />
          </button>
        </div>
        <Link className="mobile-menu-brand" to="/" onClick={() => onOpenChange(false)} aria-label="MedTheG home">
          <img className="mobile-brand-mark" src="/medtheg-logo.svg" alt="" aria-hidden="true" />
          <span>MEDTHEG</span>
        </Link>
        <nav className="mobile-nav-list" aria-label="Product categories">
          {categories.map((category) => (
            <NavLink key={category.to} to={category.to} viewTransition onClick={() => onOpenChange(false)}>
              <span>{category.label}</span><span className="category-number">{category.number}</span>
            </NavLink>
          ))}
        </nav>
        <form className="mobile-search" onSubmit={onSearch}>
          <Input label="Search releases" name="mobile-search" value={query} onChange={changeQuery} placeholder="Title or keyword" />
          <button className="search-submit" type="submit" aria-label="Search releases"><LineIcon name="search" /></button>
        </form>
        <div className="mobile-menu-bottom">
          <Link to="/login" onClick={() => onOpenChange(false)}>Account</Link>
          <span className="tech-label">Independent production goods</span>
        </div>
      </div>
    </Dialog>
  )
}

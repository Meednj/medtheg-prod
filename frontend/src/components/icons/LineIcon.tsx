import type { ReactNode, SVGProps } from 'react'

type IconName = 'search' | 'bag' | 'menu' | 'close' | 'arrow' | 'play'

const paths: Record<IconName, ReactNode> = {
  search: <><circle cx="10.5" cy="10.5" r="6.25" /><path d="m15.2 15.2 5 5" /></>,
  bag: <><path d="M4 8h16l-1 13H5L4 8Z" /><path d="M9 9V6a3 3 0 0 1 6 0v3" /></>,
  menu: <><path d="M3 6h18M3 12h18M3 18h18" /></>,
  close: <><path d="m5 5 14 14M19 5 5 19" /></>,
  arrow: <><path d="M4 12h15M13 5l7 7-7 7" /></>,
  play: <path d="m8 5 11 7-11 7V5Z" />,
}

export function LineIcon({ name, ...props }: SVGProps<SVGSVGElement> & { name: IconName }) {
  return (
    <svg viewBox="0 0 24 24" aria-hidden="true" focusable="false" {...props}>
      {paths[name]}
    </svg>
  )
}

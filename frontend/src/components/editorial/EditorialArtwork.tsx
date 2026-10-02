import type { ArtworkPresentation } from './artwork-presentation'

type EditorialArtworkProps = {
  index: string
  label: string
  metadata: string
  reverse?: boolean
  artwork: ArtworkPresentation
}

export function EditorialArtwork({ index, label, metadata, reverse = false, artwork }: EditorialArtworkProps) {
  return (
    <div className={`placeholder-cover${reverse ? ' placeholder-cover--reverse' : ''}`} role="img" aria-label={label}>
      <span className="artwork-image" aria-hidden="true">
        {artwork.src ? <img src={artwork.src} alt="" /> : artwork.fallbackMark && <span className="cover-glyph">{artwork.fallbackMark}</span>}
      </span>
      <span className="artwork-mask" aria-hidden="true" />
      <span className="artwork-registration-rule" aria-hidden="true" />
      <span className="pressing-index" aria-hidden="true" data-index={index}>{index}</span>
      <span className="pressing-index--selected" aria-hidden="true">{index}</span>
      <span className="tech-label artwork-metadata">{metadata}</span>
    </div>
  )
}

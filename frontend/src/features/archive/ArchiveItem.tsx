import { EditorialArtwork } from '../../components/editorial/EditorialArtwork'
import { EditorialRule } from '../../components/editorial/EditorialRule'
import type { ArtworkPresentation } from '../../components/editorial/artwork-presentation'

export type ArchiveItemData = {
  title: string
  index: string
  metadata: string[]
  reverse?: boolean
  artwork: ArtworkPresentation
}

type ArchiveItemProps = {
  item: ArchiveItemData
  selected: boolean
  onSelect: () => void
}

export function ArchiveItem({ item, selected, onSelect }: ArchiveItemProps) {
  return (
    <article className="pressing-item" data-selected={selected || undefined}>
      <button className="pressing-select" type="button" aria-label={`Select ${item.title} artwork, ${item.metadata.join(', ')}`} aria-pressed={selected} onClick={onSelect}>
        <EditorialRule className="pressing-rule" interruption extend={selected} />
        <EditorialArtwork index={item.index} label={item.artwork.alt} metadata={item.metadata[0] ?? ''} reverse={item.reverse} artwork={item.artwork} />
        <span className="pressing-meta">
          <span className="pressing-title">{item.title}</span>
          {item.metadata.slice(1).map((value) => <span className="pressing-detail" key={value}>{value}</span>)}
        </span>
      </button>
    </article>
  )
}

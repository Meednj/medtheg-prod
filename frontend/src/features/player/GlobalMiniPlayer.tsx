import { LineIcon } from '../../components/icons/LineIcon'
import { useVisualPlayer } from './useVisualPlayer'

export function GlobalMiniPlayer() {
  const { track, isPlaying, isEnded, playbackId, toggle, finish } = useVisualPlayer()
  if (!track) return null

  return (
    <aside className={`global-player${isPlaying ? ' is-playing' : ''}${isEnded ? ' is-ended' : ''}`} aria-label="Preview transport">
      <div className="global-player-mark" aria-hidden="true">M<span>TG</span></div>
      <div className="global-player-info">
        <strong>{track.title}</strong>
        <span>{track.category} / Visual demo only — no audio source connected</span>
      </div>
      <button className="global-player-toggle" type="button" onClick={toggle} aria-label={isEnded ? 'Replay visual preview' : isPlaying ? 'Pause visual preview' : 'Resume visual preview'}>
        {isPlaying ? <span className="pause-glyph" aria-hidden="true" /> : <LineIcon name="play" />}
      </button>
      <div className="global-player-trace" aria-hidden="true">
        <span key={playbackId} aria-hidden="true" onAnimationEnd={finish} />
      </div>
    </aside>
  )
}

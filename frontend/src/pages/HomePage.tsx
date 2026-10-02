import { useState } from 'react'
import { Link } from 'react-router-dom'
import { Badge } from '../components/foundation/Badge'
import { EditorialRule } from '../components/editorial/EditorialRule'
import { LineIcon } from '../components/icons/LineIcon'
import { HeroDustTitle } from '../components/motion/HeroDustTitle'
import { ArchiveItem } from '../features/archive/ArchiveItem'
import { homepagePreviewData } from '../features/home/homepage-preview-data'
import { useVisualPlayer } from '../features/player/useVisualPlayer'

export function HomePage() {
  const { issue, statement, description, feature, pressings, categories } = homepagePreviewData
  const { track, isPlaying, isEnded, playbackId, play, toggle } = useVisualPlayer()
  const [heroFormed, setHeroFormed] = useState(false)
  const [selectedPressing, setSelectedPressing] = useState<string | null>(null)
  const isFeatureActive = track?.title === feature.title

  function togglePreview() {
    if (isFeatureActive) toggle()
    else play({ title: feature.title, category: feature.type })
  }

  return (
    <div className="home-page">
      <div className="home-meta-row">
        <span className="eyebrow">{issue}</span>
        <span className="tech-label">A home for sound in progress</span>
      </div>

      <section className={`home-hero${isFeatureActive && isPlaying ? ' home-hero--playing' : ''}${heroFormed ? ' is-formed' : ''}`} aria-labelledby="home-title">
        <div className="hero-copy">
          <p className="eyebrow">Production starts after hours</p>
          <HeroDustTitle lines={statement} onFormed={() => setHeroFormed(true)} />
          <p className="hero-description">{description}</p>
          <Link className="hero-cta" to="/beats">
            Open the release index <LineIcon name="arrow" />
          </Link>
        </div>
        <EditorialRule className="hero-grid-rule" extend={heroFormed} />
        <div className="hero-artwork" aria-label="Monochrome vinyl artwork prototype" role="img">
          <div className="artwork-topline tech-label"><span>MedTheG / 001</span><span>Side A</span></div>
          <div className="record-mark" aria-hidden="true">
            <span className="record-groove" />
            <span className="record-center">M<br />TG</span>
          </div>
          <div className="artwork-bottomline">
            <span className="artwork-title">Signal<br />/ Noise</span>
            <span className="tech-label">Cover study<br />01—04</span>
          </div>
        </div>
      </section>

      <section className="featured-transport" aria-label="Featured listening transport prototype">
        <div className={`prototype-player${isFeatureActive ? ' is-active' : ''}${isFeatureActive && isPlaying ? ' is-playing' : ''}`}>
          <button className="player-play" type="button" aria-label={isFeatureActive && isEnded ? 'Replay visual preview demo' : isFeatureActive && isPlaying ? 'Pause visual preview' : 'Play visual preview demo'} aria-pressed={Boolean(isFeatureActive && isPlaying)} onClick={togglePreview}>
            {isFeatureActive && isPlaying ? <span className="pause-glyph" aria-hidden="true" /> : <LineIcon name="play" />}
            <span>{isFeatureActive && isPlaying ? 'Pause' : 'Play'}</span>
          </button>
          <div className="player-track-info">
            <strong>{feature.title}</strong>
            <span>{feature.type} · no audio source connected</span>
          </div>
          <div className="player-timeline" aria-hidden="true">
            <div className="player-progress" aria-hidden="true"><span key={playbackId} /></div>
          </div>
          <span className="player-duration">{isFeatureActive && isEnded ? 'ENDED' : 'VISUAL DEMO'}</span>
        </div>
        <p className="phase-note" aria-live="polite">{isFeatureActive && isEnded ? 'Visual transport demo ended — no audio source connected' : isFeatureActive && isPlaying ? 'Visual transport simulation running — no audio source connected' : 'Transport layout study — visual control demo only'}</p>
      </section>

      <section className="editorial-section" aria-labelledby="latest-title">
        <div className="section-heading">
          <div>
            <p className="eyebrow">Index / 01—04</p>
            <h2 id="latest-title">Latest pressings</h2>
          </div>
          <Badge>Temporary visual data</Badge>
        </div>
        <div className="pressing-grid">
          {pressings.map((pressing, index) => (
            <ArchiveItem
              key={pressing.title}
              item={{
                title: pressing.title,
                index: String(index + 1).padStart(2, '0'),
                metadata: [pressing.type, 'Archive fragment / artwork study'],
                reverse: pressing.reverse,
                artwork: { alt: `${pressing.title} monochrome artwork placeholder`, fallbackMark: pressing.glyph },
              }}
              selected={selectedPressing === pressing.title}
              onSelect={() => setSelectedPressing(selectedPressing === pressing.title ? null : pressing.title)}
            />
          ))}
        </div>
      </section>

      <section className="editorial-section" aria-labelledby="category-title">
        <div className="section-heading">
          <div>
            <p className="eyebrow">Find your raw material</p>
            <h2 id="category-title">The index</h2>
          </div>
          <span className="section-index">04 DEPARTMENTS</span>
        </div>
        <nav className="category-index" aria-label="Product categories">
          {categories.map((category) => (
            <Link className="category-link" key={category.to} to={category.to} viewTransition>
              <span className="category-number">{category.number}</span>
              <span className="category-name">{category.name}</span>
              <LineIcon className="category-arrow" name="arrow" />
            </Link>
          ))}
        </nav>
      </section>

      <section className="editorial-section" aria-labelledby="feature-title">
        <div className="section-heading">
          <div>
            <p className="eyebrow">Field notes / production tools</p>
            <h2 id="feature-title">Built for the next session</h2>
          </div>
          <span className="section-index">EDITORIAL STUDY / 01</span>
        </div>
        <div className="editorial-feature">
          <div className="feature-artwork" role="img" aria-label="Temporary monochrome production kit artwork">
            <strong>Make<br />a mark.</strong>
          </div>
          <div className="feature-copy">
            <span className="eyebrow">A production kit / visual study</span>
            <h2>Cut from the room.</h2>
            <p>A placeholder editorial passage showing how a featured kit or course can sit within the MedTheG archive.</p>
            <Link className="text-link" to="/kits">Explore the kit index <span aria-hidden="true">↗</span></Link>
          </div>
        </div>
      </section>
      <p className="phase-note">Phase 01 prototype — all release names, metadata, and artwork are temporary structural content.</p>
    </div>
  )
}

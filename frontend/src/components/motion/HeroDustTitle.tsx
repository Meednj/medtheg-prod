import { useEffect, useLayoutEffect, useRef, useState } from 'react'

type DustParticle = {
  x: number
  y: number
  fromX: number
  fromY: number
  radius: number
  opacity: number
  delay: number
}

type HeroDustTitleProps = {
  lines: string[]
  onFormed?: () => void
}

const FORMATION_MS = 1480
const MAX_PARTICLES = 1800

function sampleGlyphs(title: HTMLElement, lines: string[]): DustParticle[] {
  const bounds = title.getBoundingClientRect()
  const ratio = Math.min(window.devicePixelRatio || 1, 2)
  const surface = document.createElement('canvas')
  surface.width = Math.max(1, Math.ceil(bounds.width * ratio))
  surface.height = Math.max(1, Math.ceil(bounds.height * ratio))

  const context = surface.getContext('2d', { willReadFrequently: true })
  if (!context) return []

  context.scale(ratio, ratio)
  context.fillStyle = '#fff'
  context.textAlign = 'left'
  context.textBaseline = 'alphabetic'

  const lineNodes = title.querySelectorAll<HTMLElement>('.hero-title-enter')
  lines.forEach((line, index) => {
    const node = lineNodes[index]
    if (!node) return
    const style = getComputedStyle(node)
    context.font = style.font
    context.letterSpacing = style.letterSpacing
    const range = document.createRange()
    range.selectNodeContents(node)
    const rect = range.getBoundingClientRect()
    const metrics = context.measureText(line.toUpperCase())
    context.fillText(
      line.toUpperCase(),
      rect.left - bounds.left,
      rect.top - bounds.top + metrics.actualBoundingBoxAscent,
    )
  })

  const pixels = context.getImageData(0, 0, surface.width, surface.height).data
  const isCompact = bounds.width < 600
  const sampleStep = Math.max(1, Math.round((isCompact ? 4 : 3) * ratio))
  const targets: Array<{ x: number; y: number }> = []
  for (let y = 0; y < surface.height; y += sampleStep) {
    for (let x = 0; x < surface.width; x += sampleStep) {
      if (pixels[(y * surface.width + x) * 4 + 3] > 96) {
        targets.push({ x: x / ratio, y: y / ratio })
      }
    }
  }

  const particleLimit = isCompact ? 900 : MAX_PARTICLES
  const stride = Math.max(1, Math.ceil(targets.length / particleLimit))
  return targets.filter((_, index) => index % stride === 0).map(({ x, y }) => {
    const angle = Math.random() * Math.PI * 2
    const distance = 28 + Math.random() * Math.min(160, bounds.width * 0.24)
    return {
      x,
      y,
      fromX: x + Math.cos(angle) * distance,
      fromY: y + Math.sin(angle) * distance * 0.62,
      radius: 0.55 + Math.random() * 0.45,
      opacity: 0.16 + Math.random() * 0.64,
      delay: Math.random() * 0.1,
    }
  })
}

export function HeroDustTitle({ lines, onFormed }: HeroDustTitleProps) {
  const titleRef = useRef<HTMLHeadingElement>(null)
  const canvasRef = useRef<HTMLCanvasElement>(null)
  const onFormedRef = useRef(onFormed)
  const [isForming, setIsForming] = useState(false)
  const [isFormed, setIsFormed] = useState(false)
  const copy = lines.join('\n')

  useEffect(() => {
    onFormedRef.current = onFormed
  }, [onFormed])

  useLayoutEffect(() => {
    const titleRefValue = titleRef.current
    const canvasRefValue = canvasRef.current
    const reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)')
    if (!titleRefValue || !canvasRefValue || reduceMotion.matches) return
    const title: HTMLHeadingElement = titleRefValue
    const canvas: HTMLCanvasElement = canvasRefValue

    let frame = 0
    let cancelled = false
    setIsForming(true)

    async function formText() {
      await document.fonts?.ready
      if (cancelled) return
      const textLines = copy.split('\n')
      const particles = sampleGlyphs(title, textLines)
      if (particles.length === 0) {
        setIsForming(false)
        setIsFormed(true)
        onFormedRef.current?.()
        return
      }

      const bounds = title.getBoundingClientRect()
      const ratio = Math.min(window.devicePixelRatio || 1, 2)
      canvas.width = Math.max(1, Math.ceil(bounds.width * ratio))
      canvas.height = Math.max(1, Math.ceil(bounds.height * ratio))
      canvas.style.width = `${bounds.width}px`
      canvas.style.height = `${bounds.height}px`
      const drawingContext = canvas.getContext('2d')
      if (!drawingContext) return
      const drawing: CanvasRenderingContext2D = drawingContext
      drawing.scale(ratio, ratio)
      const started = performance.now()

      function draw(now: number) {
        if (cancelled) return
        const progress = Math.min(1, (now - started) / FORMATION_MS)
        const formation = Math.max(0, Math.min(1, (progress - 0.08) / 0.92))
        const eased = 1 - (1 - formation) ** 3
        drawing.clearRect(0, 0, bounds.width, bounds.height)
        for (const particle of particles) {
          const local = Math.max(0, Math.min(1, (formation - particle.delay) / (1 - particle.delay)))
          const settle = 1 - (1 - local) ** 3
          const residue = (1 - settle) * 1.8
          const x = particle.fromX + (particle.x - particle.fromX) * settle + Math.sin(now * 0.003 + particle.y) * residue
          const y = particle.fromY + (particle.y - particle.fromY) * settle + Math.cos(now * 0.0028 + particle.x) * residue
          const opacity = particle.opacity * (0.32 + eased * 0.68)
          drawing.globalAlpha = opacity
          drawing.fillStyle = '#FFFFFF'
          drawing.fillRect(x, y, particle.radius * 1.5, particle.radius * 1.5)
        }
        drawing.globalAlpha = 1
        if (progress < 1) frame = requestAnimationFrame(draw)
        else {
          drawing.clearRect(0, 0, bounds.width, bounds.height)
          setIsForming(false)
          setIsFormed(true)
          onFormedRef.current?.()
        }
      }

      frame = requestAnimationFrame(draw)
    }

    void formText()
    const handleMotionPreference = (event: MediaQueryListEvent) => {
      if (!event.matches) return
      cancelled = true
      cancelAnimationFrame(frame)
      canvas.getContext('2d')?.clearRect(0, 0, canvas.width, canvas.height)
      setIsForming(false)
      setIsFormed(true)
    }
    reduceMotion.addEventListener('change', handleMotionPreference)

    return () => {
      cancelled = true
      cancelAnimationFrame(frame)
      reduceMotion.removeEventListener('change', handleMotionPreference)
    }
  }, [copy])

  return (
    <h1
      className={`hero-title${isForming ? ' hero-title--forming' : ''}${isFormed ? ' hero-title--formed' : ''}`}
      id="home-title"
      ref={titleRef}
    >
      {lines.map((line, index) => (
        <span className={`hero-title-line hero-title-line--${index + 1}`} key={`${index}-${line}`}>
          <span className="hero-title-enter">{line}</span>
        </span>
      ))}
      <canvas className="hero-dust-canvas" ref={canvasRef} aria-hidden="true" />
    </h1>
  )
}

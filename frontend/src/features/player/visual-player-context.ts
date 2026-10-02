import { createContext } from 'react'

export type VisualTrack = { title: string; category: string }
export type VisualPlayerValue = {
  track: VisualTrack | null
  isPlaying: boolean
  isEnded: boolean
  playbackId: number
  play: (track: VisualTrack) => void
  toggle: () => void
  finish: () => void
}
export const VisualPlayerContext = createContext<VisualPlayerValue | null>(null)

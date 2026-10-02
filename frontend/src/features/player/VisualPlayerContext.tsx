import { useMemo, useState, type ReactNode } from 'react'
import { VisualPlayerContext, type VisualTrack } from './visual-player-context'

export function VisualPlayerProvider({ children }: { children: ReactNode }) {
  const [track, setTrack] = useState<VisualTrack | null>(null)
  const [isPlaying, setIsPlaying] = useState(false)
  const [isEnded, setIsEnded] = useState(false)
  const [playbackId, setPlaybackId] = useState(0)
  const value = useMemo(() => ({
    track,
    isPlaying,
    isEnded,
    playbackId,
    play: (nextTrack: VisualTrack) => {
      setTrack(nextTrack)
      setIsPlaying(true)
      setIsEnded(false)
      setPlaybackId((previous) => previous + 1)
    },
    toggle: () => {
      if (isEnded) {
        setIsEnded(false)
        setIsPlaying(true)
        setPlaybackId((previous) => previous + 1)
      } else {
        setIsPlaying((playing) => !playing)
      }
    },
    finish: () => {
      setIsPlaying(false)
      setIsEnded(true)
    },
  }), [track, isPlaying, isEnded, playbackId])

  return <VisualPlayerContext.Provider value={value}>{children}</VisualPlayerContext.Provider>
}

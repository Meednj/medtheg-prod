import { useContext } from 'react'
import { VisualPlayerContext } from './visual-player-context'

export function useVisualPlayer() {
  const context = useContext(VisualPlayerContext)
  if (!context) throw new Error('useVisualPlayer must be used within VisualPlayerProvider')
  return context
}

import { BrowserRouter } from 'react-router-dom'
import { AppRoutes } from './app/AppRoutes'
import { VisualPlayerProvider } from './features/player/VisualPlayerContext'
import './App.css'

export default function App() {
  return (
    <BrowserRouter>
      <VisualPlayerProvider><AppRoutes /></VisualPlayerProvider>
    </BrowserRouter>
  )
}

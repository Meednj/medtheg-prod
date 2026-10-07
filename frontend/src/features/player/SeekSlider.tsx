import { audioController } from "./audio-controller";

export function SeekSlider({
  currentTime,
  duration,
}: {
  currentTime: number;
  duration: number;
}) {
  return (
    <input
      className="player-seek"
      type="range"
      min="0"
      max={duration || 0}
      step="0.1"
      value={Math.min(currentTime, duration || 0)}
      aria-label="Seek preview"
      disabled={!duration}
      onChange={(event) => audioController.seek(Number(event.target.value))}
    />
  );
}

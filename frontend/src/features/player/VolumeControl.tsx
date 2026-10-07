import { audioController } from "./audio-controller";

export function VolumeControl({ volume }: { volume: number }) {
  return (
    <label className="player-volume">
      <span className="tech-label">Volume</span>
      <input
        type="range"
        min="0"
        max="1"
        step="0.05"
        value={volume}
        aria-label="Preview volume"
        onChange={(event) =>
          audioController.setVolume(Number(event.target.value))
        }
      />
    </label>
  );
}

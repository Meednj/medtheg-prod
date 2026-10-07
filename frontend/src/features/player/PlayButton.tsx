import { LineIcon } from "../../components/icons/LineIcon";
import { audioController } from "./audio-controller";
import { useAudioStore, type AudioTrack } from "./audio-store";

export function PlayButton({ track }: { track: AudioTrack }) {
  const activeTrack = useAudioStore((state) => state.track);
  const status = useAudioStore((state) => state.status);
  const active = activeTrack?.id === track.id;
  const playing = active && status === "playing";

  return (
    <button
      className="player-play"
      type="button"
      aria-label={playing ? `Pause ${track.title}` : `Play ${track.title}`}
      aria-pressed={playing}
      onClick={() => audioController.toggle(track)}
    >
      {playing ? (
        <span className="pause-glyph" aria-hidden="true" />
      ) : (
        <LineIcon name="play" />
      )}
      <span>{playing ? "Pause" : "Play"}</span>
    </button>
  );
}

import { LineIcon } from "../../components/icons/LineIcon";
import { audioController } from "./audio-controller";
import { ProgressTrace } from "./ProgressTrace";
import { TimeReadout } from "./TimeReadout";
import { useAudioStore } from "./audio-store";

export function MiniPlayer() {
  const track = useAudioStore((state) => state.track);
  const status = useAudioStore((state) => state.status);
  const currentTime = useAudioStore((state) => state.currentTime);
  const duration = useAudioStore((state) => state.duration);
  if (!track) return null;
  const playing = status === "playing";
  return (
    <aside
      className={`global-player${playing ? " is-playing" : ""}${status === "ended" ? " is-ended" : ""}`}
      aria-label="Preview transport"
    >
      <div className="global-player-mark" aria-hidden="true">
        M<span>TG</span>
      </div>
      <div className="global-player-info">
        <strong>{track.title}</strong>
        <span>{track.category} / Preview</span>
      </div>
      <button
        className="global-player-toggle"
        type="button"
        onClick={() => audioController.toggle(track)}
        aria-label={playing ? `Pause ${track.title}` : `Play ${track.title}`}
      >
        {playing ? (
          <span className="pause-glyph" aria-hidden="true" />
        ) : (
          <LineIcon name="play" />
        )}
      </button>
      <TimeReadout currentTime={currentTime} duration={duration} />
      <ProgressTrace currentTime={currentTime} duration={duration} />
    </aside>
  );
}

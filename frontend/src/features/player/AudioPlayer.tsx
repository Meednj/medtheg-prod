import { PlayButton } from "./PlayButton";
import { SeekSlider } from "./SeekSlider";
import { TimeReadout } from "./TimeReadout";
import { VolumeControl } from "./VolumeControl";
import { useAudioStore, type AudioTrack } from "./audio-store";

export function AudioPlayer({ track }: { track: AudioTrack }) {
  const activeTrack = useAudioStore((state) => state.track);
  const currentTime = useAudioStore((state) => state.currentTime);
  const duration = useAudioStore((state) => state.duration);
  const volume = useAudioStore((state) => state.volume);
  const error = useAudioStore((state) => state.error);
  const active = activeTrack?.id === track.id;

  return (
    <div className="prototype-player is-active">
      <PlayButton track={track} />
      <div className="player-track-info">
        <strong>{track.title}</strong>
        <span>
          {track.category}
          {active && error ? ` / ${error}` : " / Preview"}
        </span>
      </div>
      <div className="player-timeline">
        <SeekSlider
          currentTime={active ? currentTime : 0}
          duration={active ? duration : 0}
        />
      </div>
      <TimeReadout
        currentTime={active ? currentTime : 0}
        duration={active ? duration : 0}
      />
      <VolumeControl volume={volume} />
    </div>
  );
}

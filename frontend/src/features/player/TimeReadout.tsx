function formatTime(value: number) {
  if (!Number.isFinite(value) || value < 0) return "--:--";
  const minutes = Math.floor(value / 60);
  const seconds = Math.floor(value % 60)
    .toString()
    .padStart(2, "0");
  return `${minutes}:${seconds}`;
}

export function TimeReadout({
  currentTime,
  duration,
}: {
  currentTime: number;
  duration: number;
}) {
  return (
    <span
      className="player-duration"
      aria-label={`Elapsed ${formatTime(currentTime)} of ${formatTime(duration)}`}
    >
      {formatTime(currentTime)} / {formatTime(duration)}
    </span>
  );
}

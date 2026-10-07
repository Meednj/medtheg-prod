export function ProgressTrace({
  currentTime,
  duration,
}: {
  currentTime: number;
  duration: number;
}) {
  const progress = duration > 0 ? Math.min(1, currentTime / duration) : 0;
  return (
    <div className="global-player-trace" aria-hidden="true">
      <span style={{ transform: `scaleX(${progress})` }} />
    </div>
  );
}

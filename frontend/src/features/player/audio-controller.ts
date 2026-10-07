import { apiGet } from "../../api/client";
import type { PreviewUrlDto } from "../../api/dto";
import { useAudioStore, type AudioTrack } from "./audio-store";

class AudioController {
  private readonly audio = new Audio();
  private sourceUrl: string | null = null;
  private requestId = 0;

  constructor() {
    this.audio.preload = "none";
    this.audio.addEventListener("timeupdate", () =>
      useAudioStore
        .getState()
        .setState({ currentTime: this.audio.currentTime }),
    );
    this.audio.addEventListener("loadedmetadata", () =>
      useAudioStore.getState().setState({
        duration: Number.isFinite(this.audio.duration)
          ? this.audio.duration
          : 0,
      }),
    );
    this.audio.addEventListener("play", () =>
      useAudioStore.getState().setState({ status: "playing" }),
    );
    this.audio.addEventListener("pause", () => {
      if (!this.audio.ended)
        useAudioStore.getState().setState({ status: "paused" });
    });
    this.audio.addEventListener("ended", () =>
      useAudioStore
        .getState()
        .setState({ status: "ended", currentTime: this.audio.duration }),
    );
    this.audio.addEventListener("error", () => {
      this.sourceUrl = null;
      useAudioStore.getState().setState({
        status: "error",
        error: "Preview audio could not be loaded.",
      });
    });
  }

  async play(track: AudioTrack) {
    const current = useAudioStore.getState();
    if (current.track?.id === track.id && this.sourceUrl) {
      try {
        await this.audio.play();
      } catch {
        this.sourceUrl = null;
        useAudioStore.getState().setState({
          status: "error",
          error: "Preview link expired. Press play again to request a fresh preview.",
        });
      }
      return;
    }

    const currentRequest = ++this.requestId;
    this.audio.pause();
    this.audio.removeAttribute("src");
    this.audio.load();
    this.sourceUrl = null;
    useAudioStore.getState().setState({
      track,
      status: "loading",
      currentTime: 0,
      duration: 0,
      error: null,
    });

    try {
      const response = await apiGet<PreviewUrlDto>(
        `/products/${track.productId}/assets/${track.assetId}/preview`,
      );
      if (currentRequest !== this.requestId) return;
      this.sourceUrl = response.url;
      this.audio.src = response.url;
      this.audio.volume = useAudioStore.getState().volume;
      await this.audio.play();
    } catch {
      if (currentRequest === this.requestId) {
        this.sourceUrl = null;
        useAudioStore.getState().setState({
          status: "error",
          error: "Preview audio could not be loaded.",
        });
      }
    }
  }

  pause() {
    this.audio.pause();
  }

  toggle(track: AudioTrack) {
    const current = useAudioStore.getState();
    if (current.track?.id !== track.id || !this.sourceUrl) {
      void this.play(track);
      return;
    }
    if (this.audio.paused) {
      if (current.status === "ended") this.audio.currentTime = 0;
      void this.audio.play().catch(() => {
        this.sourceUrl = null;
        useAudioStore.getState().setState({
          status: "error",
          error: "Preview link expired. Press play again to request a fresh preview.",
        });
      });
      return;
    }
    this.audio.pause();
  }

  seek(value: number) {
    if (Number.isFinite(this.audio.duration)) this.audio.currentTime = value;
  }

  setVolume(value: number) {
    const volume = Math.min(1, Math.max(0, value));
    this.audio.volume = volume;
    useAudioStore.getState().setState({ volume });
  }

  clear() {
    ++this.requestId;
    this.audio.pause();
    this.audio.removeAttribute("src");
    this.audio.load();
    this.sourceUrl = null;
    useAudioStore.getState().clear();
  }
}

export const audioController = new AudioController();

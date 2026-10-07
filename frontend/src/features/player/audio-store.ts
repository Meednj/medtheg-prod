import { create } from "zustand";

export type AudioTrack = {
  id: string;
  title: string;
  category: string;
  productId: string;
  assetId: string;
};

export type AudioStatus =
  | "idle"
  | "loading"
  | "playing"
  | "paused"
  | "ended"
  | "error";

type AudioState = {
  track: AudioTrack | null;
  status: AudioStatus;
  currentTime: number;
  duration: number;
  volume: number;
  error: string | null;
  setState: (state: Partial<AudioState>) => void;
  clear: () => void;
};

export const useAudioStore = create<AudioState>((set) => ({
  track: null,
  status: "idle",
  currentTime: 0,
  duration: 0,
  volume: 1,
  error: null,
  setState: (state) => set(state),
  clear: () =>
    set({
      track: null,
      status: "idle",
      currentTime: 0,
      duration: 0,
      error: null,
    }),
}));

export const AudioStore = useAudioStore;
